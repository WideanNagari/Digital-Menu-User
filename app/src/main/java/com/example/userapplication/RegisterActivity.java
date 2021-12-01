package com.example.userapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userapplication.Classes.UserApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegis;
    EditText edEmail, edPhone, edPass, edCPass;
    TextView btnToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegis = findViewById(R.id.cirRegisButton);
        btnToLogin = findViewById(R.id.tv_tologin);
        edEmail = findViewById(R.id.regisEmail);
        edPhone = findViewById(R.id.regisTelp);
        edPass = findViewById(R.id.regisPassword);
        edCPass = findViewById(R.id.regisPasswordConf);

        btnRegis.setOnClickListener(this::doRegis);
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void doRegis(View view) {
        if (edEmail.getText().length() > 0 && edPhone.getText().length() > 0 && edPass.getText().length() > 0 && edCPass.getText().length() > 0) {
//            Toast.makeText(getApplicationContext(), "StartRegis", Toast.LENGTH_SHORT).show();
            if(edPass.getText().toString().equals(edCPass.getText().toString())){

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.url)+"register",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int kode = jsonObject.getInt("code");
                                    String pesan  = jsonObject.getString("message");
                                    JSONObject user = jsonObject.getJSONObject("user");
                                    System.out.println(kode+" ========");
                                    if (kode == 2){
//                                        UserApp loggedIn = new UserApp(user.getInt("user_id"),
//                                                user.getString("name"),user.getString("email"),
//                                                user.getString("no_telp"),user.getString("password"),
//                                                user.getInt("saldo"),user.getString("role"),
//                                                user.getString("status"));
//                                        Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
//                                        i.putExtra("loggedIn",loggedIn);
//                                        startActivity(i);
//                                        finish();
                                    }

                                    Toast.makeText(RegisterActivity.this, pesan, Toast.LENGTH_SHORT).show();
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
                        params.put("email",edEmail.getText().toString());
                        params.put("phone",edPhone.getText().toString());
                        params.put("password",edPass.getText().toString());
                        params.put("role","1");
                        System.out.println();
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }else{
                Toast.makeText(getApplicationContext(), "Password mismatch!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Empty field!", Toast.LENGTH_SHORT).show();
        }
    }
}