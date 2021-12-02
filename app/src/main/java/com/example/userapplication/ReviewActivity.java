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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userapplication.Classes.HJual;
import com.example.userapplication.Classes.Review;
import com.example.userapplication.Classes.UserApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {

    ArrayList<Review> arrReview;
    RecyclerView rv;
    ReviewAdapter reviewAdapter;
    Button back, submit;
    String hjual;
    UserApp user;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        arrReview = new ArrayList<>();
        rv = findViewById(R.id.rvReview);
        back = findViewById(R.id.btnBack);
        submit = findViewById(R.id. btnReview);

        i = getIntent();
        if (i.getExtras()!=null){
            if (i.hasExtra("review")){
                arrReview = i.getParcelableArrayListExtra("review");
            }
            if (i.hasExtra("hjual")){
                hjual = i.getStringExtra("hjual");
            }
            if (i.hasExtra("user")){
                user = i.getParcelableExtra("user");
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < arrReview.size(); i++) {
                    Review r = arrReview.get(i);
                    addReview(hjual, user.getId()+"", r.getId_menu()+"", r.getIsiReview(), r.getRating()+"");
                }
                Toast.makeText(ReviewActivity.this, "Thank you for your review :D", Toast.LENGTH_SHORT).show();

                setResult(Activity.RESULT_OK, i);
                i.putExtra("done","done");
                finish();
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(ReviewActivity.this));
        reviewAdapter = new ReviewAdapter(arrReview);
        rv.setAdapter(reviewAdapter);
    }

    public void addReview(String idHjual, String userId, String menu, String isi, String rating){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"review/addReview",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            String pesan  = jsonObject.getString("message");
//                            int kode = jsonObject.getInt("code");
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
                params.put("idHjual",idHjual);
                params.put("customer",userId);
                params.put("menu",menu);
                params.put("isi",isi);
                params.put("rating",rating);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ReviewActivity.this);
        requestQueue.add(stringRequest);
    }

}