package com.example.userapplication.Order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.Classes.OrderMenu;
import com.example.userapplication.Payment.PaymentActivity;
import com.example.userapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderOngoingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderOngoingFragment extends Fragment{

    ArrayList<OrderMenu> arrOrder;
    RecyclerView rv;
    OngoingAdapter ongoingAdapter;
    CircularProgressButton btnPay;
    TextView subtotal, jumOrder;
    UserApp user;
    OnActionListener onActionListener;

    public OnActionListener getOnActionListener() {
        return onActionListener;
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public OrderOngoingFragment() {
        // Required empty public constructor
    }

    public static OrderOngoingFragment newInstance(UserApp user) {
        OrderOngoingFragment fragment = new OrderOngoingFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_ongoing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        jumOrder = view.findViewById(R.id.jumOrder);

        rv = view.findViewById(R.id.rvOngoing);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        arrOrder = new ArrayList<>();
        ongoingAdapter = new OngoingAdapter(getActivity(), arrOrder);
        rv.setAdapter(ongoingAdapter);

        subtotal = view.findViewById(R.id.subtotalOngoing);
        subtotal.setText("Rp. 0");

        btnPay = view.findViewById(R.id.btnPayOrder);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!user.getCheckIn().equals("-")){
                    boolean pending = false;
                    boolean confirm = false;
                    for (int i = 0; i < arrOrder.size(); i++) {
                        if (arrOrder.get(i).getStatus().equals("Pending")) pending = true;
                        if (arrOrder.get(i).getStatus().equals("Confirmed")) confirm = true;
                    }
                    if (arrOrder.size() == 0){
                        Toast.makeText(getContext(), "Your order(s) are still empty :(", Toast.LENGTH_SHORT).show();
                    }
                    else if (pending)
                        Toast.makeText(getContext(), "there are still pending orders", Toast.LENGTH_SHORT).show();
                    else{
                        if (confirm){
                            Intent i = new Intent(getContext(), PaymentActivity.class);
                            i.putExtra("customer", user);
                            activityResultLauncher.launch(i);
                        }else{
                            Toast.makeText(getContext(), "You have no confirmed order(s).", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(getContext(), "Please Check In!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getAllOrder(user.getId()+"");
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data.hasExtra("customer")){
                            user = data.getParcelableExtra("customer");
                        }
                        if (data.hasExtra("done")){
                            if (onActionListener!=null) onActionListener.onBack(user);
                        }
                    }
                }
            }
    );

    private void hitungTotal(){
        int subtotals = 0;
        int jumTotal = 0;
        for (int i = 0; i < arrOrder.size(); i++) {
            OrderMenu order = arrOrder.get(i);
            if (order.getStatus().equals("Confirmed")){
                subtotals += Integer.parseInt(order.getHarga_menu())*order.getJumlah();
                jumTotal++;
            }
        }

        subtotal.setText(currency(subtotals+""));
        jumOrder.setText(jumTotal+"");
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
                                ongoingAdapter.notifyDataSetChanged();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public interface OnActionListener{
        void onBack(UserApp user);
    }
}