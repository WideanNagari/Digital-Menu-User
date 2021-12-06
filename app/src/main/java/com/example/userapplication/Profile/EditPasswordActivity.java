package com.example.userapplication.Profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class EditPasswordActivity extends AppCompatActivity {

    Button btnConf;
    EditText edOld, edNew, edConfirmNew;
    UserApp loggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        btnConf = findViewById(R.id.btn_changepassword);
        edOld = findViewById(R.id.ed_previouspass);
        edNew = findViewById(R.id.ed_newpass);
        edConfirmNew = findViewById(R.id.ed_confirmnewpass);
        Intent par = getIntent();
        if(par.hasExtra("loggedIn")){
            loggedIn = par.getParcelableExtra("loggedIn");
        }
        btnConf.setOnClickListener(this::doUpdatePassword);
    }

    public void doUpdatePassword(View view) {
        if (edOld.getText().length() > 0 && edNew.getText().length() > 0 && edConfirmNew.getText().length() > 0) {
//            if(edOld.getText().toString().equals(loggedIn.getPassword())){
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.url)+"user/updatePassword",
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

                                    }

                                    Toast.makeText(EditPasswordActivity.this, pesan, Toast.LENGTH_SHORT).show();
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
                        params.put("oldPass",edOld.getText().toString());
                        params.put("newPass",edNew.getText().toString());
                        System.out.println();
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
//            }else{
//                Toast.makeText(getApplicationContext(), "Wrong old password, no change made", Toast.LENGTH_SHORT).show();
//            }
            loggedIn.setPassword(edNew.getText().toString());

            Intent i = new Intent();
            i.putExtra("loggedIn", loggedIn);
            setResult(110, i);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Empty field!", Toast.LENGTH_SHORT).show();
        }
    }
}