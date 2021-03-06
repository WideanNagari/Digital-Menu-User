package com.example.userapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText edEmail, edPhone, edPass, edCPass;
    long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_register);

        changeStatusBarColor();

        edEmail = findViewById(R.id.registerEmail);
        edPhone = findViewById(R.id.registerMobile);
        edPass = findViewById(R.id.registerPassword);
        edCPass = findViewById(R.id.registerConfPassword);

        edPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(edPhone.getText().length()>13) {
                    edPhone.setError("No More");
                } else if(edPhone.getText().length()<13) {
                    edPhone.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        finish();
    }

    public void registrasi(View view) {
        if (edEmail.getText().length() > 0 && edPhone.getText().length() > 0 && edPass.getText().length() > 0 && edCPass.getText().length() > 0) {
//            Toast.makeText(getApplicationContext(), "StartRegis", Toast.LENGTH_SHORT).show();
            if(edPass.getText().toString().equals(edCPass.getText().toString())){

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.url)+"register",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                                System.out.println(response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int kode = jsonObject.getInt("code");
                                    String pesan  = jsonObject.getString("message");
//                                    JSONObject user = jsonObject.getJSONObject("user");
                                    Toast.makeText(RegisterActivity.this, pesan, Toast.LENGTH_SHORT).show();
                                    if (kode == 1){
                                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
                                        finish();
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
                        params.put("phone","0"+edPhone.getText().toString());
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