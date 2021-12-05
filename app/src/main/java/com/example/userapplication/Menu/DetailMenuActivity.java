package com.example.userapplication.Menu;

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

import com.example.userapplication.DAO.AppDatabase;
import com.example.userapplication.Classes.Like;
import com.example.userapplication.Classes.Menu;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailMenuActivity extends AppCompatActivity implements AddLikeAsync.AddLikeCallback, DeleteLikeAsync.DeleteLikeCallback, LoadLikeAsync.LoadLikeCallback {
    ImageView imgV, imgLike;
    TextView txtNama, txtHrg, txtDesc, txtJum, txtSub;
    ImageView btnAdd, btnSub, btnAddtoCart;
    AppCompatImageView btnBack;
    Intent i;
    boolean like;
    Menu menu;
    UserApp user;
    ArrayList<Like> arrLike;
    Like likes;

    int jum=0;
    String nama, desc;
    boolean canAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);

        imgV=findViewById(R.id.detailImage);
        imgLike=findViewById(R.id.detail_btn_like);
        txtNama=findViewById(R.id.detail_name);
        txtHrg=findViewById(R.id.detail_price);
        txtDesc=findViewById(R.id.detail_desc);

        txtJum=findViewById(R.id.detail_jum_order);
        txtSub=findViewById(R.id.detail_sub_price);
        btnAdd=findViewById(R.id.btn_plus);
        btnSub=findViewById(R.id.btn_minus);
        btnAddtoCart=findViewById(R.id.detail_add_cart);

        btnBack=findViewById(R.id.detail_back);

        txtJum.setText("1");

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
        txtHrg.setText(currency(menu.getHarga_menu()+""));
        txtDesc.setText(menu.getDeskripsi_menu());

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
    }

    public void add(View v){
        jum++;
        txtJum.setText(jum+"");
        updateSub();
    }

    public void sub(View v){
        if(jum>0){
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
