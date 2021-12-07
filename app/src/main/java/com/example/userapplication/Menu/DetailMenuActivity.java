package com.example.userapplication.Menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.userapplication.Classes.OrderMenu;
import com.example.userapplication.Classes.Type;
import com.example.userapplication.DAO.AppDatabase;
import com.example.userapplication.Classes.Like;
import com.example.userapplication.Classes.Menu;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.Order.OrderCartFragment;
import com.example.userapplication.R;
import com.skydoves.expandablelayout.ExpandableLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailMenuActivity extends AppCompatActivity implements AddLikeAsync.AddLikeCallback, DeleteLikeAsync.DeleteLikeCallback,
        LoadLikeAsync.LoadLikeCallback, AddOrderAsync.AddOrderCallback, LoadCartAsync.LoadCartCallback {
    ImageView imgV, imgLike;
    TextView txtNama, txtJenis, txtHrg, txtDesc, txtJum, txtSub;
    TextView txtRating, txtOrder;
    ImageView btnAdd, btnSub, btnAddtoCart;
    AppCompatImageView btnBack;
    Intent i;
    boolean like;
    Menu menu;
    UserApp user;
    ArrayList<Like> arrLike;
    List<OrderMenu> cartMenu;
    Like likes;

    int jum=1, subtotal;
    String nama, desc;
    boolean canAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);

        //review
        ExpandableLayout ex = findViewById(R.id.expandable);
        final int[] g = {0};
        ex.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(g[0] ==0) {
                    g[0] = 1;
                    ex.expand();
                } else {
                    g[0] = 0;
                    ex.collapse();
                }
            }
        });


        imgV=findViewById(R.id.detailImage);
        imgLike=findViewById(R.id.detail_btn_like);
        txtNama=findViewById(R.id.detail_name);
        txtJenis=findViewById(R.id.detail_jenis);
        txtHrg=findViewById(R.id.detail_price);
        txtDesc=findViewById(R.id.detail_desc);

        txtOrder=findViewById(R.id.detail_jum_orders);
        txtRating=findViewById(R.id.detail_rate);

        txtJum=findViewById(R.id.detail_jum_order);
        txtSub=findViewById(R.id.detail_sub_price);
        btnAdd=findViewById(R.id.btn_plus);
        btnSub=findViewById(R.id.btn_minus);
        btnAddtoCart=findViewById(R.id.detail_add_cart);
        btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ada = false;
                for (int i = 0; i < cartMenu.size(); i++) {
                    if (cartMenu.get(i).getId().equals(menu.getId()))
                        ada = true;
                }

                if (ada){
                    Toast.makeText(DetailMenuActivity.this, "This item already in your cart.", Toast.LENGTH_SHORT).show();
                }else{
                    new AddOrderAsync(DetailMenuActivity.this, DetailMenuActivity.this,
                            new OrderMenu(menu.getId(), menu.getNama_menu(), menu.getHarga_menu(), menu.getDeskripsi_menu(),
                                    menu.getJenis_menu(), menu.getStatus_menu(),menu.getRating(), user.getId(), jum,"-",
                                    0, menu.getGambar())).execute();
                }
            }
        });

        btnBack=findViewById(R.id.detail_back);

        txtJum.setText(jum+"");

        i=getIntent();
        if (i.getExtras()!=null){
            if (i.hasExtra("menu")){
                menu = i.getParcelableExtra("menu");
            }
            if (i.hasExtra("user")){
                user = i.getParcelableExtra("user");
            }
        }
        txtNama.setText(menu.getNama_menu());
        txtJenis.setText(menu.getJenis_menu());
        txtHrg.setText(currency(menu.getHarga_menu()+""));
        txtDesc.setText(menu.getDeskripsi_menu());
        txtRating.setText(menu.getRating()+"");
//        System.out.println(menu.getGambar());
        Glide.with(this).load(menu.getGambar()).into(imgV);

        getJumOrder(menu.getId());

        subtotal = Integer.parseInt(menu.getHarga_menu());
        txtSub.setText("Subtotal: "+currency(subtotal+""));

        like = false;
        canAction = false;
        arrLike = new ArrayList<>();
        new LoadLikeAsync(this, DetailMenuActivity.this, user.getId()+"").execute();

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!like){
                    //ngelike
                    if (canAction) {
                        canAction = false;
                        new AddLikeAsync(DetailMenuActivity.this, DetailMenuActivity.this, user.getId()+"", menu.getId()).execute();
                    }
                }else{
                    //ngunlike
                    if (canAction){
                        canAction = false;
                        new DeleteLikeAsync(DetailMenuActivity.this, DetailMenuActivity.this, likes).execute();
                    }
                }
            }
        });

        cartMenu = new ArrayList<>();
        new LoadCartAsync(this, DetailMenuActivity.this, user.getId()+"").execute();
    }

    public void add(View v){
        jum++;
        txtJum.setText(jum+"");
        updateSub();
    }

    public void sub(View v){
        if(jum>1){
            jum--;
            txtJum.setText(jum+"");
        }
        updateSub();
    }

    public void updateSub(){
        int sub=Integer.parseInt(menu.getHarga_menu())*jum;
        txtSub.setText("Subtotal: "+currency(sub+""));
    }

    private String currency(String angkaAwal){
        String hasil = "";
        if (angkaAwal.length()>=3){
            int ctr = 1;
            for (int i = angkaAwal.length()-1; i >= 0; i--) {
                hasil = angkaAwal.charAt(i) + hasil;
                if (ctr%3==0 && ctr<angkaAwal.length()) hasil = "."+hasil;
                ctr++;
            }
        }else{
            hasil = angkaAwal;
        }
        return "Rp. "+hasil;
    }

    public void back(View v){
        finish();
    }

    @Override
    public void preExecuteAdd() {

    }

    @Override
    public void postExecuteAdd() {
        like = true;
        imgLike.setImageResource(R.drawable.liked);
        new LoadLikeAsync(this, DetailMenuActivity.this, user.getId()+"").execute();
    }

    @Override
    public void preExecuteDelete() {

    }

    @Override
    public void postExecuteDelete() {
        like = false;
        likes = null;
        imgLike.setImageResource(R.drawable.like);
        new LoadLikeAsync(this, DetailMenuActivity.this, user.getId()+"").execute();
    }

    @Override
    public void preExecuteLoad() {

    }

    @Override
    public void postExecuteLoad(List<Like> listLike) {
        arrLike.clear();
        arrLike.addAll(listLike);
        boolean ada = false;
        for (int i = 0; i < arrLike.size(); i++) {
            if (arrLike.get(i).getMenu().equals(menu.getId())){
                ada = true;
                likes = arrLike.get(i);
            }
        }

        if (ada){
            like = true;
            canAction = true;
            imgLike.setImageResource(R.drawable.liked);
        }
        else{
            like = false;
            canAction = true;
            imgLike.setImageResource(R.drawable.like);
        }
    }

    private void getJumOrder(String id){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"transaction/countMenu",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String jumOrder = jsonObject.getString("count");
                            txtOrder.setText(jumOrder);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                //untuk handle error
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailMenuActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void preExecuteAddO() {

    }

    @Override
    public void postExecuteAddO() {
        Toast.makeText(this, "Your orders have been added to your cart.", Toast.LENGTH_SHORT).show();
        new LoadCartAsync(this, DetailMenuActivity.this, user.getId()+"").execute();
    }

    @Override
    public void preExecuteLoadO() {

    }

    @Override
    public void postExecuteLoadO(List<OrderMenu> listMenu) {
        cartMenu.clear();
        cartMenu.addAll(listMenu);
//        for (int i = 0; i < listMenu.size(); i++) {
//            System.out.println(listMenu.get(i).getGambarID());
//        }
    }
}

class LoadLikeAsync{
    private final WeakReference<Context> weakContext;
    private final WeakReference<LoadLikeAsync.LoadLikeCallback> weakCallback;
    private String id;

    LoadLikeAsync(Context context, LoadLikeAsync.LoadLikeCallback loadLikeCallback, String id) {
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(loadLikeCallback);
        this.id = id;
    }

    void execute() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        weakCallback.get().preExecuteLoad();
        executorService.execute(() -> {
            Context context = weakContext.get();
            List<Like> likeList = AppDatabase.database.likeDAO().getAllLike(id);
            handler.post(() -> weakCallback.get().postExecuteLoad(likeList));
        });
    }

    interface LoadLikeCallback{
        void preExecuteLoad();
        void postExecuteLoad(List<Like> listLike);
    }
}

class AddLikeAsync{
    private final WeakReference<Context> weakContext;
    private final WeakReference<AddLikeAsync.AddLikeCallback> weakCallback;
    private Like likes;
    private AddLikeAsync addLikeAsync;
    AddLikeAsync(Context weakContext, AddLikeAsync.AddLikeCallback addLikeCallback, String id, String menu) {
        this.weakContext = new WeakReference<>(weakContext);
        this.weakCallback = new WeakReference<>(addLikeCallback);
        this.likes = new Like(id,menu);
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakCallback.get().preExecuteAdd();
        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase.database.likeDAO().insert(likes);
            handler.post(() -> weakCallback.get().postExecuteAdd());
        });
    }

    interface AddLikeCallback {
        void preExecuteAdd();
        void postExecuteAdd();
    }
}

class DeleteLikeAsync{
    private final WeakReference<Context> weakContext;
    private final WeakReference<DeleteLikeAsync.DeleteLikeCallback> weakCallback;
    private Like likes;
    private AddLikeAsync addLikeAsync;
    DeleteLikeAsync(Context weakContext, DeleteLikeAsync.DeleteLikeCallback deleteLikeCallback, Like like) {
        this.weakContext = new WeakReference<>(weakContext);
        this.weakCallback = new WeakReference<>(deleteLikeCallback);
        this.likes = like;
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakCallback.get().preExecuteDelete();
        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase.database.likeDAO().delete(likes);
            handler.post(() -> weakCallback.get().postExecuteDelete());
        });
    }

    interface DeleteLikeCallback {
        void preExecuteDelete();
        void postExecuteDelete();
    }
}

class AddOrderAsync{
    private final WeakReference<Context> weakContext;
    private final WeakReference<AddOrderCallback> weakCallback;
    private OrderMenu orderMenu;
    AddOrderAsync(Context weakContext, AddOrderCallback addGameCallback,OrderMenu orderMenu) {
        this.weakContext = new WeakReference<>(weakContext);
        this.weakCallback = new WeakReference<>(addGameCallback);
        this.orderMenu = orderMenu;
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakCallback.get().preExecuteAddO();
        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase.database.orderDAO().insert(orderMenu);
            handler.post(() -> weakCallback.get().postExecuteAddO());
        });
    }

    interface AddOrderCallback {
        void preExecuteAddO();
        void postExecuteAddO();
    }
}

class LoadCartAsync{
    private final WeakReference<Context> weakContext;
    private final WeakReference<LoadCartAsync.LoadCartCallback> weakCallback;
    private String id;

    LoadCartAsync(Context context, LoadCartAsync.LoadCartCallback updateCartCallback, String id) {
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(updateCartCallback);
        this.id = id;
    }

    void execute() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        weakCallback.get().preExecuteLoadO();
        executorService.execute(() -> {
            Context context = weakContext.get();
            List<OrderMenu> orderList = AppDatabase.database.orderDAO().getAllOrder(id);
            handler.post(() -> weakCallback.get().postExecuteLoadO(orderList));
        });
    }

    interface LoadCartCallback{
        void preExecuteLoadO();
        void postExecuteLoadO(List<OrderMenu> listMenu);
    }
}