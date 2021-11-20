package com.example.userapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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