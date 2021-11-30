package com.example.userapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userapplication.Fragments.HomeFragment;
import com.example.userapplication.Fragments.OrderFragment;
import com.example.userapplication.Fragments.ProfileFragment;
import com.example.userapplication.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity{

    BottomNavigationView navbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppDatabase.initDatabase(getApplicationContext(), "OrderDB");

        navbar = findViewById(R.id.nav_home);

        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag;
                switch (item.getItemId()){
                    default:
                        frag = HomeFragment.newInstance("param1", "param2");
                        break;
                    case R.id.btn_navhome:
                        frag = HomeFragment.newInstance("param1", "param2");
                        break;
                    case R.id.btn_navsearch:
                        frag = SearchFragment.newInstance();
                        break;
                    case R.id.btn_navorder:
                        frag = OrderFragment.newInstance();
                        break;
                    case R.id.btn_navprofile:
                        frag = ProfileFragment.newInstance("param1", "param2");
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