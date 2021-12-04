package com.example.userapplication.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class HomeActivity extends AppCompatActivity{

    private UserApp loggedIn;
    BottomNavigationView navbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppDatabase.initDatabase(getApplicationContext(), "OrderDB");

        navbar = findViewById(R.id.nav_home);
        Intent par = getIntent();
        if(par.hasExtra("loggedIn")){
            loggedIn = par.getParcelableExtra("loggedIn");
        }

        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag;
                switch (item.getItemId()){
                    default:
                        frag = HomeFragment.newInstance(loggedIn);
                        break;
                    case R.id.btn_navhome:
                        frag = HomeFragment.newInstance(loggedIn);
                        break;
                    case R.id.btn_navsearch:
                        frag = SearchFragment.newInstance(loggedIn);
                        break;
                    case R.id.btn_navorder:
                        frag = OrderFragment.newInstance(loggedIn);
                        break;
                    case R.id.btn_navhistory:
                        frag = HistoryFragment.newInstance(loggedIn);
                        break;
                    case R.id.btn_navprofile:
                        frag = ProfileFragment.newInstance(loggedIn);
                        break;
                }
                try {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fr_home,frag).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        if (savedInstanceState == null) {
            navbar.setSelectedItemId(R.id.btn_navhome);
        }

        getSupportFragmentManager().addFragmentOnAttachListener(new FragmentOnAttachListener() {
            @Override
            public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
                if (fragment instanceof OrderFragment) {
                    OrderFragment orderFragment = (OrderFragment) fragment;
                    orderFragment.setOnActionListener(new OrderFragment.OnActionListener() {
                        @Override
                        public void onBack(UserApp u) {
                            loggedIn = u;
                            navbar.setSelectedItemId(R.id.btn_navhome);
                        }
                    });
                }else if (fragment instanceof HomeFragment){
                    HomeFragment homeFragment = (HomeFragment) fragment;
                    homeFragment.setOnActionListener(new HomeFragment.OnActionListener() {
                        @Override
                        public void onBack(UserApp user) {
                            loggedIn = user;
                            navbar.setSelectedItemId(R.id.btn_navhome);
                        }
                    });
                }
            }
        });

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