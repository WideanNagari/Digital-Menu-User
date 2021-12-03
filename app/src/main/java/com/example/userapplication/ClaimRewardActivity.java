package com.example.userapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userapplication.Classes.Reward;
import com.example.userapplication.Classes.UserApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClaimRewardActivity extends AppCompatActivity {

    Intent i;
    UserApp user;
    TextView jumlahStamp;
    Button back;
    RewardAdapter rewardAdapter;
    RecyclerView rv;
    ArrayList<Reward> arrReward;
    boolean sudah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_reward);

        jumlahStamp = findViewById(R.id.userStamps);
        back = findViewById(R.id.rewardBack);
        rv = findViewById(R.id.rvReward);

        arrReward = new ArrayList<>();
        getAllReward();

        sudah = true;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sudah){
                    i.putExtra("done","done");
                    i.putExtra("customer", user);
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
            }
        });

        i = getIntent();
        if (i.getExtras()!=null){
            if (i.hasExtra("user")){
                user = i.getParcelableExtra("user");
            }
        }

        jumlahStamp.setText(user.getStamp()>1 ? user.getStamp()+" Stamps":user.getStamp()+" Stamp");

        rv.setLayoutManager(new LinearLayoutManager(this));
        rewardAdapter = new RewardAdapter(arrReward, user.getStamp());
        rv.setAdapter(rewardAdapter);
        rewardAdapter.setOnClaimClick(new RewardAdapter.OnClaimClick() {
            @Override
            public void onClaim(Reward reward) {
                sudah = false;
                addOrder(user.getId()+"",reward.getId_menu(),"1", reward.getStamp()+"");
            }
        });
    }

    private void getAllReward(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"reward/",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int kode = jsonObject.getInt("code");
                            String pesan  = jsonObject.getString("message");

                            if (kode == 1){
                                arrReward.clear();
                                JSONArray arr = jsonObject.getJSONArray("dataReward");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject reward = arr.getJSONObject(i);
                                    arrReward.add(new Reward(reward.getString("reward"),
                                            reward.getString("id_menu"),
                                            reward.getInt("stamp")));
                                }
                                rewardAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

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
                params.put("mode","customer");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void addOrder(String customer, String menu, String jumlah, String stamp){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"order/addOrder",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String pesan  = jsonObject.getString("message");
                            int kode = jsonObject.getInt("code");
                            if (kode==1) {
                                user.setStamp(jsonObject.getInt("stamp"));
                                jumlahStamp.setText(user.getStamp() > 1 ? user.getStamp() + " Stamps" : user.getStamp() + " Stamp");
                                sudah = true;
                            }
                            rewardAdapter.setUserStamp(user.getStamp());
                            rewardAdapter.notifyDataSetChanged();
                            Toast.makeText(ClaimRewardActivity.this, pesan, Toast.LENGTH_SHORT).show();

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
                params.put("customer",customer);
                params.put("menu",menu);
                params.put("jumlah",jumlah);
                params.put("reward","1");
                params.put("stamp",stamp);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}