package com.example.userapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.userapplication.Classes.HJual;
import com.example.userapplication.Classes.Review;
import com.example.userapplication.Classes.UserApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailHistoryActivity extends AppCompatActivity {

    TextView promo, total, potongan, subtotal, stamp;
    Button review, back;
    RecyclerView rv;
    Intent i;
    HJual hjual;
    UserApp user;
    PaymentAdapter paymentAdapter;

    ArrayList<OrderMenu> arrOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        i = getIntent();
        if (i.getExtras()!=null){
            if (i.hasExtra("hjual")){
                hjual = i.getParcelableExtra("hjual");
            }
            if (i.hasExtra("user")){
                user = i.getParcelableExtra("user");
            }
        }

        promo = findViewById(R.id.usedPromo);
        total = findViewById(R.id.totalHistory);
        potongan = findViewById(R.id.diskonHistory);
        subtotal = findViewById(R.id.subtotalHistory);
        stamp = findViewById(R.id.stampHistory);

        review = findViewById(R.id.btnReview);
        back = findViewById(R.id.btnBack);

        //mending ini
//        if(hjual.getReview_status()==1) review.setEnabled(false);

        //atau ini?
        if(hjual.getReview_status()==1) review.setVisibility(View.GONE);

        arrOrder = new ArrayList<>();
        rv = findViewById(R.id.rvMenuHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        paymentAdapter = new PaymentAdapter(arrOrder);
        rv.setAdapter(paymentAdapter);

        promo.setText(hjual.getPromo()+"");
        total.setText("Rp. 0");
        potongan.setText(currency(hjual.getPotongan()+""));
        subtotal.setText(currency(hjual.getSubtotal()+""));
        stamp.setText("+0");

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Review> arrReview = new ArrayList<>();
                for (int i = 0; i < arrOrder.size(); i++) {
                    OrderMenu order = arrOrder.get(i);
                    arrReview.add(new Review(Integer.parseInt(order.getId()), order.getNama_menu()));
                }

                Intent i = new Intent(DetailHistoryActivity.this, ReviewActivity.class);
                i.putExtra("review", arrReview);
                i.putExtra("hjual", hjual.getNomor_nota());
                i.putExtra("user", user);
                activityResultLauncher.launch(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getItemHistory(hjual.getNomor_nota());
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data.hasExtra("done")){
                            setResult(Activity.RESULT_OK, i);
                            i.putExtra("done","done");
                            finish();
                        }
                    }
                }
            }
    );

    private void hitungTotal(){
        int totals = 0;
        for (int i = 0; i < arrOrder.size(); i++) {
            OrderMenu order = arrOrder.get(i);
            totals += Integer.parseInt(order.getHarga_menu())*order.getJumlah();
        }

        total.setText(currency(totals+""));
        stamp.setText("+"+(totals/20000));
    }

    private String currency(String angkaAwal){
        String hasil = "";
        if (angkaAwal.length()>=3){
            int ctr = 1;
            for (int i = angkaAwal.length()-1; i >= 0; i--) {
                hasil = angkaAwal.charAt(i) + hasil;
                if (ctr%3==0 && ctr<angkaAwal.length()) hasil = "."+hasil;
                ctr++;
            }
        }else{
            hasil = angkaAwal;
        }
        return "Rp. "+hasil;
    }

    private void getItemHistory(String nota){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"transaction/getDetail",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int kode = jsonObject.getInt("code");
                            String pesan  = jsonObject.getString("message");

                            if (kode == 1){
                                arrOrder.clear();
                                JSONArray arr = jsonObject.getJSONArray("dataDjual");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject djual = arr.getJSONObject(i);
                                    arrOrder.add(new OrderMenu(djual.getString("id")
                                            , djual.getString("nama_menu")
                                            , djual.getString("harga_menu")
                                            , djual.getString("deskripsi")
                                            , djual.getString("jenis_menu")
                                            , djual.getString("status_menu")
                                            , djual.getInt("jumlah")
                                            , djual.getString("status")
                                            , djual.getInt("reward_status")
                                    ));
                                }
                                hitungTotal();
                                paymentAdapter.notifyDataSetChanged();
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
                params.put("nota",nota);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(DetailHistoryActivity.this);
        requestQueue.add(stringRequest);
    }
}