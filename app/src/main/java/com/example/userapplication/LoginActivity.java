package com.example.userapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.userapplication.Home.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText edPhone, edPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

        changeStatusBarColor();

        edPhone = findViewById(R.id.loginTelp);
        edPass = findViewById(R.id.loginPassword);

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.red2));
        }
    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        finish();
    }

    public void btnClick(View view){
//        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
//        startActivity(i);
//        finish();
        if(edPhone.getText().length()>0 && edPass.getText().length()>0){
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    getResources().getString(R.string.url)+"loginUser",
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
                                    UserApp loggedIn = new UserApp(user.getInt("user_id"),
                                            user.getString("name"),user.getString("email"),
                                            user.getString("no_telp"),user.getString("password"),
                                            user.getInt("saldo"), user.getInt("stamp"),
                                            user.getString("role"),user.getString("status"),
                                            jsonObject.getString("meja"), jsonObject.getString("idMeja")
                                            );
                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                    i.putExtra("loggedIn",loggedIn);
                                    startActivity(i);
                                    finish();
                                }

                                Toast.makeText(LoginActivity.this, pesan, Toast.LENGTH_SHORT).show();
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
                    params.put("phone",edPhone.getText().toString());
                    params.put("password",edPass.getText().toString());
                    params.put("role","1");
                    System.out.println();
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else{
            Toast.makeText(getApplicationContext(), "Empty field!", Toast.LENGTH_SHORT).show();
        }
    }
}