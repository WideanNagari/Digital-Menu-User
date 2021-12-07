package com.example.userapplication.Profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    UserApp loggedIn;
    Button btnUpdate;
    EditText profileName, profileEmail, profilePhone, confPass;
    AppCompatImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        btnUpdate = findViewById(R.id.btn_updateprofile);
        profileName = findViewById(R.id.ed_profilename);
        profileEmail = findViewById(R.id.ed_profileemail);
        profilePhone = findViewById(R.id.ed_profilephone);
        confPass = findViewById(R.id.ed_confirmPassword);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent par = getIntent();
        if(par.hasExtra("loggedIn")){
            loggedIn = par.getParcelableExtra("loggedIn");
            profileName.setText(loggedIn.getName());
            profileEmail.setText(loggedIn.getEmail());
            profilePhone.setText(loggedIn.getTelp());
        }
        btnUpdate.setOnClickListener(this::doUpdate);
    }


    private void doUpdate(View view){
        if(profileName.getText().length()>0 && profileEmail.getText().length()>0 && profilePhone.getText().length()>0 && confPass.getText().length()>0){
            if(loggedIn.getPassword().equals(confPass.getText().toString())){
                //do Update

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.url)+"user/updateProfile",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int kode = jsonObject.getInt("code");
                                    String pesan  = jsonObject.getString("message");
//                                    JSONObject user = jsonObject.getJSONObject("user");
                                    System.out.println(kode+" ========");
                                    if (kode == 2){
//                                        UserApp loggedIn = new UserApp(user.getInt("user_id"),
//                                                user.getString("name"),user.getString("email"),
//                                                user.getString("no_telp"),user.getString("password"),
//                                                user.getInt("saldo"),user.getString("role"),
//                                                user.getString("status"));
//                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
//                                        i.putExtra("loggedIn",loggedIn);
//                                        startActivity(i);
//                                        finish();
                                    }

                                    Toast.makeText(EditProfileActivity.this, pesan, Toast.LENGTH_SHORT).show();
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
                        params.put("name",profileName.getText().toString());
                        params.put("email",profileEmail.getText().toString());
                        params.put("no_telp",profilePhone.getText().toString());
                        System.out.println();
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);


                loggedIn.setName(profileName.getText().toString());
                loggedIn.setEmail(profileEmail.getText().toString());
                loggedIn.setTelp(profilePhone.getText().toString());

                Intent i = new Intent();
                i.putExtra("loggedIn",loggedIn);
                setResult(Activity.RESULT_OK, i);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Password mismatch!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Empty field!", Toast.LENGTH_SHORT).show();
        }
    }
}