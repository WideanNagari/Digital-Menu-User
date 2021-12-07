package com.example.userapplication.Liked;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userapplication.Classes.Like;
import com.example.userapplication.Classes.Menu;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.DAO.AppDatabase;
import com.example.userapplication.R;

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

public class LikedActivity extends AppCompatActivity implements LoadLikeAsync.LoadLikeCallback, DeleteLikeAsync.DeleteLikeCallback{

    AppCompatImageView back;
    RecyclerView recyclerView;
    LikedAdapter likedAdapter;
    ArrayList<Like> arrLike;
    ArrayList<Menu> arrMenu;
    Intent i;
    UserApp user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);

        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        i = getIntent();
        if (i.getExtras()!=null){
            if (i.hasExtra("user")){
                user = i.getParcelableExtra("user");
            }
        }

        arrLike = new ArrayList<>();
        arrMenu = new ArrayList<>();
        recyclerView = findViewById(R.id.rvLiked);
        likedAdapter = new LikedAdapter(this, arrMenu);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        recyclerView.setAdapter(likedAdapter);
        likedAdapter.setOnClick(new LikedAdapter.OnClick() {
            @Override
            public void onUnlike(Menu m) {
                Like like = null;
                for (int i = 0; i < arrLike.size(); i++) {
                    if (arrLike.get(i).getMenu().equals(m.getId())) like = arrLike.get(i);
                }

                if (like!=null){
                    new DeleteLikeAsync(LikedActivity.this, LikedActivity.this, like).execute();
                }
            }
        });

        new LoadLikeAsync(this, LikedActivity.this, user.getId()+"").execute();
    }


    @Override
    public void preExecuteLoad() {

    }

    @Override
    public void postExecuteLoad(List<Like> listLike) {
        arrLike.clear();
        arrLike.addAll(listLike);
        getAllMenu();
    }

    private void getAllMenu(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"menu/forSearchAll",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arr = jsonObject.getJSONArray("dataMenu");
                            int kode = jsonObject.getInt("code");
                            if (kode == 1){
                                arrMenu.clear();
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject order = arr.getJSONObject(i);
                                    boolean ada = false;
                                    for (int j = 0; j < arrLike.size(); j++) {
                                        if (arrLike.get(j).getMenu().equals(order.getString("id"))){
                                            ada = true;
                                        }
                                    }
                                    if (ada){
                                        arrMenu.add(new Menu(order.getString("id")
                                                , order.getString("nama_menu")
                                                , order.getString("harga_menu")
                                                , order.getString("deskripsi_menu")
                                                , order.getString("jenis_menu")
                                                , order.getString("status_menu")
                                                , order.getString("asset")
                                                , order.getDouble("rating")
                                        ));
                                    }
                                }
                                likedAdapter.notifyDataSetChanged();
                            }else if(kode == -3){
//                                Toast.makeText(getActivity(), "No Menu", Toast.LENGTH_SHORT).show();
                            }
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
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LikedActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void preExecuteDelete() {

    }

    @Override
    public void postExecuteDelete() {
        new LoadLikeAsync(this, LikedActivity.this, user.getId()+"").execute();
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

class DeleteLikeAsync{
    private final WeakReference<Context> weakContext;
    private final WeakReference<DeleteLikeAsync.DeleteLikeCallback> weakCallback;
    private Like likes;

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