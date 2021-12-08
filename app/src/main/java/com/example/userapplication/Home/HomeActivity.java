package com.example.userapplication.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.userapplication.Classes.Menu;
import com.example.userapplication.Classes.OrderMenu;
import com.example.userapplication.Classes.Promo;
import com.example.userapplication.Classes.PromoXReward;
import com.example.userapplication.DAO.AppDatabase;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.History.HistoryFragment;
import com.example.userapplication.Order.OrderCartFragment;
import com.example.userapplication.Order.OrderFragment;
import com.example.userapplication.Profile.ProfileFragment;
import com.example.userapplication.Menu.SearchFragment;
import com.example.userapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class HomeActivity extends AppCompatActivity implements LoadCartAsync.LoadCartCallback{

    private UserApp loggedIn;

    private final int ID_HOME = 1;
    private final int ID_SEARCH = 2;
    private final int ID_ORDER = 3;
    private final int ID_HISTORY = 4;
    private final int ID_PROFILE = 5;

    MeowBottomNavigation bottomNavigation;

    ArrayList<Menu> pop;
    ArrayList<Menu> recommend;
    ArrayList<Menu> again;
    ArrayList<PromoXReward> promoReward;

    long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        changeStatusBarColor();
        AppDatabase.initDatabase(getApplicationContext(), "OrderDB");

        //navbar = findViewById(R.id.nav_home);
        Intent par = getIntent();
        if(par.hasExtra("loggedIn")){
            loggedIn = par.getParcelableExtra("loggedIn");
        }

        bottomNavigation = findViewById(R.id.nav_home);
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_baseline_circle_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_SEARCH, R.drawable.ic_baseline_search_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ORDER, R.drawable.ic_baseline_shopping_cart_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HISTORY, R.drawable.ic_baseline_attach_money_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_PROFILE, R.drawable.ic_baseline_person_24));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment;
                switch (item.getId()){
                    case ID_HOME:
                        fragment = new HomeFragment().newInstance(loggedIn);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_home, fragment)
                                .commit();
                        break;
                    case ID_SEARCH:
                        fragment = new SearchFragment().newInstance(loggedIn);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_home, fragment)
                                .commit();
                        break;
                    case ID_ORDER:
                        fragment = new OrderFragment().newInstance(loggedIn);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_home, fragment)
                                .commit();
                        break;
                    case ID_HISTORY:
                        fragment = new HistoryFragment().newInstance(loggedIn);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_home, fragment)
                                .commit();
                        break;
                    case ID_PROFILE:
                        fragment = new ProfileFragment().newInstance(loggedIn);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_home, fragment)
                                .commit();
                        break;
                }
            }
        });

        bottomNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
//                Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
//                System.out.println("a");
            }
        });

        pop = new ArrayList<>();
        recommend = new ArrayList<>();
        again = new ArrayList<>();
        promoReward = new ArrayList<>();

        getPromo();

        getSupportFragmentManager().addFragmentOnAttachListener(new FragmentOnAttachListener() {
            @Override
            public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
                if (fragment instanceof HomeFragment){
                    HomeFragment homeFragment = (HomeFragment) fragment;
                    homeFragment.setOnActionListener(new HomeFragment.OnActionListener() {
                        @Override
                        public void onBack(UserApp user) {
                            loggedIn = user;
                            bottomNavigation.show(ID_HOME, true);
                        }

                        @Override
                        public void onReady(HomeFragment homeFragment) {
                            homeFragment.setListReward(promoReward);
                            homeFragment.setListPopular(pop);
                            homeFragment.setListRecommended(recommend);
                            homeFragment.setListAgain(again);
                        }
                    });
                }else if (fragment instanceof OrderFragment){
                    OrderFragment orderFragment = (OrderFragment) fragment;
                    orderFragment.setOnActionListener(new OrderFragment.OnActionListener() {
                        @Override
                        public void onBack(UserApp user) {
                            loggedIn = user;
                            bottomNavigation.show(ID_HOME, true);
                        }
                    });
                }else if (fragment instanceof ProfileFragment){
                    ProfileFragment profileFragment = (ProfileFragment) fragment;
                    profileFragment.setOnActionListener(new ProfileFragment.OnActionListener() {
                        @Override
                        public void onBack(UserApp user) {
                            loggedIn = user;
                            System.out.println(loggedIn.getStamp()+"a");
                            bottomNavigation.show(ID_PROFILE, true);
                        }

                        @Override
                        public void onFinish() {
                            SharedPreferences preferences = getSharedPreferences("loginUser", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("remember", "");
                            editor.apply();
                            finish();
                        }
                    });
                }
            }
        });

        new LoadCartAsync(HomeActivity.this, HomeActivity.this, loggedIn.getId()+"").execute();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.red2));
        }
    }

    public UserApp getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(UserApp loggedIn) {
        this.loggedIn = loggedIn;
    }

    private void getPromo(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"promo/getPromo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arr = jsonObject.getJSONArray("dataPromo");
                            int kode = jsonObject.getInt("code");
                            if (kode == 1){
                                promoReward.clear();
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject order = arr.getJSONObject(i);
                                    promoReward.add(new PromoXReward("promo"
                                            , order.getInt("id")
                                            , order.getString("nama_promo")
                                            , order.getInt("besar_promo")
                                            , order.getInt("max_promo")
                                            , order.getInt("min_spent")
                                            , order.getString("status")
                                            , "", "", 0
                                    ));
                                }
                                getReward();
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
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getReward(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"reward/getReward",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arr = jsonObject.getJSONArray("dataReward");
                            int kode = jsonObject.getInt("code");
                            if (kode == 1){
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject order = arr.getJSONObject(i);
                                    promoReward.add(new PromoXReward("reward"
                                            , 0, "", 0, 0, 0, ""
                                            , order.getString("reward")
                                            , order.getString("id_menu")
                                            , order.getInt("stamp")
                                    ));
                                }
                                getPopular();
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
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getPopular(){
        System.out.println("popular");
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"menu/popular",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arr = jsonObject.getJSONArray("dataMenu");
                            int kode = jsonObject.getInt("code");
                            if (kode == 1){
                                pop.clear();
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject order = arr.getJSONObject(i);
                                    pop.add(new Menu(order.getString("id")
                                            , order.getString("nama_menu")
                                            , order.getString("harga_menu")
                                            , order.getString("deskripsi_menu")
                                            , order.getString("jenis_menu")
                                            , order.getString("status_menu")
                                            , order.getString("asset")
                                            , order.getDouble("rating")
                                    ));
                                }
                                getRecommended();
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
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getRecommended(){
        System.out.println("recom");
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"menu/recommended",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arr = jsonObject.getJSONArray("dataMenu");
                            int kode = jsonObject.getInt("code");
                            if (kode == 1){
                                recommend.clear();
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject order = arr.getJSONObject(i);
                                    recommend.add(new com.example.userapplication.Classes.Menu(order.getString("id")
                                            , order.getString("nama_menu")
                                            , order.getString("harga_menu")
                                            , order.getString("deskripsi_menu")
                                            , order.getString("jenis_menu")
                                            , order.getString("status_menu")
                                            , order.getString("asset")
                                            , order.getDouble("rating")
                                    ));
                                }
                                getAgain();
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
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getAgain(){
        System.out.println("again");
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"menu/orderAgain",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arr = jsonObject.getJSONArray("dataMenu");
                            int kode = jsonObject.getInt("code");
                            if (kode == 1){
                                again.clear();
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject order = arr.getJSONObject(i);
                                    again.add(new Menu(order.getString("id")
                                            , order.getString("nama_menu")
                                            , order.getString("harga_menu")
                                            , order.getString("deskripsi_menu")
                                            , order.getString("jenis_menu")
                                            , order.getString("status_menu")
                                            , order.getString("asset")
                                            , order.getDouble("rating")
                                    ));
                                }
                                bottomNavigation.show(ID_HOME, true);
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
                params.put("id",loggedIn.getId()+"");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void preExecuteLoadO() {

    }

    @Override
    public void postExecuteLoadO(List<OrderMenu> listMenu) {
        String jumlah = listMenu.size()+"";
        bottomNavigation.setCount(ID_ORDER, jumlah); //yg kasih notif
    }

    @Override
    public void onBackPressed() {
        if ((exitTime+2000) > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }
        exitTime = System.currentTimeMillis();
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