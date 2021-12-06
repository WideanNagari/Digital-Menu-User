package com.example.userapplication.Payment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userapplication.Classes.OrderMenu;
import com.example.userapplication.Classes.Promo;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    Spinner promo;
    String[] spinItem;
    ArrayList<Promo> arrPromo;
    ArrayList<OrderMenu> arrOrder;
    Intent i;
    UserApp user;
    AlertDialog.Builder alertDialogBuilder;
    String usePromo;

    TextView total, potongan, subtotal, stamp, infoPromo, promoAlert, balance, balanceAlert;
    Button pay;
    RecyclerView rv;
    PaymentAdapter paymentAdapter;
    int totals, diskon, max, subtotals, disc;

    int counter, maxx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        i = getIntent();
        if (i.getExtras()!=null){
            if (i.hasExtra("customer")){
                user = i.getParcelableExtra("customer");
            }
        }

        counter = 0;
        rv = findViewById(R.id.rvMenuBayar);
        pay = findViewById(R.id.btnPay);
        promo = findViewById(R.id.spinnerPromo);
        total = findViewById(R.id.totalBayar);
        potongan = findViewById(R.id.diskonBayar);
        subtotal = findViewById(R.id.subtotalBayar);
        stamp = findViewById(R.id.dapatStamp);
        infoPromo = findViewById(R.id.infoPromo);
        promoAlert = findViewById(R.id.promoMinAlert);
        balance = findViewById(R.id.userBalance);
        balanceAlert = findViewById(R.id.userBalanceAlert);
        balance.setText(currency(user.getSaldo()+""));

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subtotals>user.getSaldo()){
                    showDialog("insufficient balance, please top up first.",getResources().getDrawable(R.drawable.ic_check),"fail");
                }else{
                    showDialog("Pay "+currency(subtotals+"")+"?",getResources().getDrawable(R.drawable.ic_check),"pay");
                }
            }
        });

        diskon = 0;
        max = 0;

        arrOrder = new ArrayList<>();
        getAllOrder(user.getId()+"");
        arrPromo = new ArrayList<>();
        getAllPromo();

        rv.setLayoutManager(new LinearLayoutManager(this));
        paymentAdapter = new PaymentAdapter(this, arrOrder);
        rv.setAdapter(paymentAdapter);

        alertDialogBuilder = new AlertDialog.Builder(this);
        usePromo = "0";
    }

    private void hitungTotal(){
        totals = 0;
        for (int i = 0; i < arrOrder.size(); i++) {
            OrderMenu order = arrOrder.get(i);
            totals += Integer.parseInt(order.getHarga_menu())*order.getJumlah();
        }

        disc = diskon<=max ? diskon:max;
        subtotals = totals-disc;

        total.setText(currency(totals+""));
        potongan.setText(currency(disc+""));
        subtotal.setText(currency(subtotals+""));
        stamp.setText("+"+(totals/20000));

        balanceAlert.setVisibility(subtotals>user.getSaldo() ? View.VISIBLE:View.GONE);
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

    private void isiSpinner(){
        spinItem = new String[arrPromo.size()];
        for (int i = 0; i < arrPromo.size(); i++) {
            Promo promo = arrPromo.get(i);
            spinItem[i] = promo.getNama_promo();
        }
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(
                PaymentActivity.this,
                android.R.layout.simple_spinner_item,
                spinItem
        );

        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        promo.setAdapter(spinAdapter);

        promo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Promo promo = arrPromo.get(i);
                infoPromo.setText("Potongan "+promo.getBesar_promo()+"% maks. "+currency(promo.getMax_promo()+"")
                        +" dengan minimal pembelian "+currency(promo.getMin_spent()+"")+")");
                if (promo.getMin_spent()>totals){
                    promoAlert.setVisibility(View.VISIBLE);
                }else{
                    usePromo = promo.getId()+"";
                    promoAlert.setVisibility(View.GONE);
                    diskon = (totals*promo.getBesar_promo())/100;
                    max = promo.getMax_promo();
                    hitungTotal();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showDialog(String message, Drawable drawable, String action){
        alertDialogBuilder.setTitle("Alert!");
        alertDialogBuilder
                .setMessage(message)
                .setIcon(drawable)
                .setCancelable(false);
        if (action.equals("fail")){
            alertDialogBuilder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick (DialogInterface dialogInterface,int j){

                }
            });
        }else if (action.equals("pay")){
            alertDialogBuilder.setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick (DialogInterface dialogInterface,int j){
                    maxx = arrOrder.size();
                    addHJual(user.getId()+"",usePromo,arrOrder.size()+"", disc+"", subtotals+"",totals/20000);
                    for (int i = 0; i < arrOrder.size(); i++) {
                        addDJual(arrOrder.get(i).getId(),arrOrder.get(i).getJumlah()+"", arrOrder.get(i).getReward_status()+"");
                    }
                }
            }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void addHJual(String customer, String promo, String jumlah, String potongan, String subtotal, int stamp){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"transaction/addH",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            String pesan  = jsonObject.getString("message");
//                            int kode = jsonObject.getInt("code");
                            user.setSaldo(jsonObject.getInt("saldo"));
                            if(jsonObject.getString("checkin")!=null)
                                user.setCheckIn(jsonObject.getString("checkin"));
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
                params.put("promo",promo);
                params.put("jumlah",jumlah);
                params.put("potongan",potongan);
                params.put("subtotal",subtotal);
                params.put("stamp",stamp+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PaymentActivity.this);
        requestQueue.add(stringRequest);
    }

    public void addDJual(String menu, String quantity, String reward){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"transaction/addD",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            String pesan  = jsonObject.getString("message");
//                            int kode = jsonObject.getInt("code");
                            counter++;
                            if (counter==maxx){
                                Toast.makeText(PaymentActivity.this, "Thank You!", Toast.LENGTH_SHORT).show();
                                i.putExtra("done","done");
                                i.putExtra("customer", user);
                                System.out.println(user.getCheckIn());
                                setResult(Activity.RESULT_OK, i);
                                finish();
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
                params.put("menu",menu);
                params.put("quantity",quantity);
                params.put("reward",reward);
                params.put("stamp","0");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PaymentActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getAllOrder(String customer){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"order/",

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
                                JSONArray arr = jsonObject.getJSONArray("dataOrder");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject order = arr.getJSONObject(i);
                                    if (order.getString("status").equals("Confirmed")){
                                        arrOrder.add(new OrderMenu(order.getString("id")
                                                , order.getString("nama_menu")
                                                , order.getString("harga_menu")
                                                , order.getString("deskripsi_menu")
                                                , order.getString("jenis_menu")
                                                , order.getString("status_menu")
                                                , order.getDouble("rating")
                                                , 0
                                                , order.getInt("jumlah")
                                                , order.getString("status")
                                                , order.getInt("reward_status")
                                                , order.getString("asset")
                                        ));
                                    }
                                }
                                paymentAdapter.notifyDataSetChanged();
                                hitungTotal();
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
                params.put("customer",customer);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PaymentActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getAllPromo(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"promo/",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int kode = jsonObject.getInt("code");
                            String pesan  = jsonObject.getString("message");

                            if (kode == 1){
                                arrPromo.clear();
                                JSONArray arr = jsonObject.getJSONArray("dataPromo");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject promo = arr.getJSONObject(i);
                                    arrPromo.add(new Promo(promo.getInt("id"),
                                            promo.getString("nama_promo"),
                                            promo.getInt("besar_promo"),
                                            promo.getInt("max_promo"),
                                            promo.getInt("min_spent"),
                                            promo.getString("status")));
                                }
                                isiSpinner();
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
}