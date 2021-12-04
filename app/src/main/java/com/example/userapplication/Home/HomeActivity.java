package com.example.userapplication.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
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
import com.example.userapplication.DAO.AppDatabase;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.History.HistoryFragment;
import com.example.userapplication.Order.OrderFragment;
import com.example.userapplication.Profile.ProfileFragment;
import com.example.userapplication.Menu.SearchFragment;
import com.example.userapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class HomeActivity extends AppCompatActivity{

    private UserApp loggedIn;

    private final int ID_HOME = 1;
    private final int ID_SEARCH = 2;
    private final int ID_ORDER = 3;
    private final int ID_HISTORY = 4;
    private final int ID_PROFILE = 5;

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

        MeowBottomNavigation bottomNavigation = findViewById(R.id.nav_home);
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
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });
        bottomNavigation.setCount(ID_ORDER, "0"); //yg kasih notif
        bottomNavigation.show(ID_HOME, true);


        getSupportFragmentManager().addFragmentOnAttachListener(new FragmentOnAttachListener() {
            @Override
            public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {

            }
        });
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

    private void GetAllMahasiswaProcess(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url),
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject list = new JSONObject(response);
                            int responsecode = list.getInt("code");
                            String responsemessage = list.getString("message");
                            if(responsecode == 1){
                                //JSONArray HARUS DIGANTI
                                JSONArray listJSON = list.getJSONArray("datamhs");
                                for (int i = 0; i < listJSON.length(); i++) {
                                    JSONObject mhs = listJSON.getJSONObject(i);
                                    //DI SINI TERIMA OBJECT KALO UDAH SIAP CLASSNYA
                                }
                            }
//                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("function","getallmhs");
                //DI SINI PANGGIL FUNCTION LARAVEL GET ALL
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(stringRequest);
    }
}