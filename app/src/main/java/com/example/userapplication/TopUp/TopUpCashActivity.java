package com.example.userapplication.TopUp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.Profile.EditPasswordActivity;
import com.example.userapplication.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TopUpCashActivity extends AppCompatActivity {

    Button btnTopUp;
    EditText edNominal, edPass;
    UserApp loggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_cash);
        btnTopUp = findViewById(R.id.btn_topupadd);
        edNominal = findViewById(R.id.ed_topupcash);
        edPass = findViewById(R.id.ed_topuppass);
        Intent par = getIntent();
        if(par.hasExtra("loggedIn")){
            loggedIn = par.getParcelableExtra("loggedIn");
            System.out.println("=="+loggedIn.toString());
        }

        btnTopUp.setOnClickListener(this::doTopUp);
    }
    public void doTopUp(View view){
        if(edNominal.getText().toString().isEmpty()==false && edPass.getText().toString().isEmpty()==false){
            if(Integer.parseInt(edNominal.getText().toString()) > 0){
                //doTopUp
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.url)+"user/topup",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int kode = jsonObject.getInt("code");
                                    String pesan  = jsonObject.getString("message");

                                    if (kode==1){
                                        loggedIn.setSaldo(loggedIn.getSaldo() + Integer.parseInt(edNominal.getText().toString()));
                                        Intent i = new Intent();
                                        i.putExtra("loggedIn", loggedIn);
                                        setResult(Activity.RESULT_OK, i);
                                        finish();
                                    }

                                    Toast.makeText(TopUpCashActivity.this, pesan, Toast.LENGTH_SHORT).show();
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
                        params.put("pass",edPass.getText().toString());
                        params.put("nominal",edNominal.getText().toString());
                        System.out.println();
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }else{
                Toast.makeText(getApplicationContext(), "Value must be bigger than 0", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Empty field!", Toast.LENGTH_SHORT).show();
        }
    }
}