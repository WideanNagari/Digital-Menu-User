package com.example.userapplication.Order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.userapplication.DAO.AppDatabase;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.Classes.OrderMenu;
import com.example.userapplication.R;
import com.example.userapplication.Classes.Table;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderCartFragment extends Fragment implements LoadCartAsync.LoadCartCallback, UpdateCartAsync.UpdateCartCallback, DeleteCartAsync.DeleteCartCallback {

    ArrayList<OrderMenu> arrOrder;
    RecyclerView rv;
    OrderAdapter orderAdapter;
    TextView subtotal;
    Button btnOrder, btnCheckin;
    UserApp user;
    AlertDialog.Builder alertDialogBuilder;
    ArrayList<Table> arrMeja;
    String[] spinItem;
    ArrayAdapter<String> spinAdapter;
    BottomSheetDialog bottomSheetDialog;
    View bottomSheetView;
    Spinner spin;

    public OrderCartFragment() {
        // Required empty public constructor
    }

    public static OrderCartFragment newInstance(UserApp user) {
        OrderCartFragment fragment = new OrderCartFragment();
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
        return inflater.inflate(R.layout.fragment_order_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arrOrder = new ArrayList<>();
        arrMeja = new ArrayList<>();
        rv = view.findViewById(R.id.rvOrder);
        subtotal = view.findViewById(R.id.subtotalCart);
        subtotal.setText("Rp. 0");

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(arrOrder);
        rv.setAdapter(orderAdapter);
        orderAdapter.setOnOrderItemClick(new OrderAdapter.OnOrderItemClick() {
            @Override
            public void onAdd(OrderMenu orderMenu) {
                orderMenu.setJumlah(orderMenu.getJumlah()+1);
                new UpdateCartAsync(getActivity(),OrderCartFragment.this, orderMenu).execute();
            }

            @Override
            public void onMin(OrderMenu orderMenu) {
                orderMenu.setJumlah(orderMenu.getJumlah()-1);
                new UpdateCartAsync(getActivity(),OrderCartFragment.this, orderMenu).execute();
            }

            @Override
            public void onCheck(OrderMenu orderMenu, boolean b) {
                orderMenu.setConfirm(b);
                new UpdateCartAsync(getActivity(),OrderCartFragment.this, orderMenu).execute();
            }

            @Override
            public void onDelete(OrderMenu orderMenu) {
                arrOrder.remove(orderMenu);
                orderAdapter.notifyDataSetChanged();
                new DeleteCartAsync(getActivity(),OrderCartFragment.this, orderMenu).execute();
            }
        });

        btnOrder = view.findViewById(R.id.btnNewOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int jumlah = 0;
                for (int i = 0; i < arrOrder.size(); i++) {
                    if (arrOrder.get(i).isConfirm()) jumlah++;
                }
                if (jumlah>0){
                    if (user.getCheckIn().equals("-"))
                        showDialog("Check In Required!",getResources().getDrawable(R.drawable.ic_check),"check-in alert");
                    else
                        showDialog("Please double check your order.",getResources().getDrawable(R.drawable.ic_check),"double check");
                }else{
                    Toast.makeText(getContext(), "Please choose at least 1 menu.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sheetDialogNew();
        btnCheckin = view.findViewById(R.id.btnCheckin);
        if (!user.getCheckIn().equals("-")){
            btnCheckin.setEnabled(false);
            btnCheckin.setText(user.getCheckIn());
        }
        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getCheckIn().equals("-")){
                    getAvailableTable();
                }
            }
        });

        new LoadCartAsync(getActivity(),OrderCartFragment.this).execute();
        alertDialogBuilder = new AlertDialog.Builder(getContext());
    }

    private void sheetDialogNew(){
        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetView = LayoutInflater.from(getContext().getApplicationContext())
                .inflate(
                        R.layout.layout_checkin,
                        (LinearLayout)getView().findViewById(R.id.bottomContainer)
                );

        EditText isiKode = bottomSheetView.findViewById(R.id.kodeCheckIn);
        spin = bottomSheetView.findViewById(R.id.spinnerCheckin);
        bottomSheetView.findViewById(R.id.doCheckin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIn(arrMeja.get(spin.getSelectedItemPosition()).getId()+"",user.getId()+"",isiKode.getText().toString());
            }
        });
    }

    private void callSheetDialog(){
        spin.setAdapter(spinAdapter);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void isiSpinner(){
        spinItem = new String[arrMeja.size()];
        for (int i = 0; i < arrMeja.size(); i++) {
            spinItem[i] = arrMeja.get(i).getNomor_meja();
        }
        spinAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_spinner_item,
                spinItem
        );
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        callSheetDialog();
    }

    private void showDialog(String message, Drawable drawable, String action){
        alertDialogBuilder.setTitle("Alert!");
        alertDialogBuilder
                .setMessage(message)
                .setIcon(drawable)
                .setCancelable(false);
        if (action.equals("check-in alert")){
            alertDialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick (DialogInterface dialogInterface,int j){

                }
            });
        }else if (action.equals("double check")){
            alertDialogBuilder.setPositiveButton("I'm sure", new DialogInterface.OnClickListener() {
                @Override
                public void onClick (DialogInterface dialogInterface,int j){
                    ArrayList<OrderMenu> orderMenus = new ArrayList<>();
                    for (int i = 0; i < arrOrder.size(); i++) {
                        OrderMenu order = arrOrder.get(i);
                        if (order.isConfirm()){
                            order.setStatus("Pending");
                            new UpdateCartAsync(getActivity(),OrderCartFragment.this, order).execute();
                            addOrder(user.getId()+"",order.getId()+"",order.getJumlah()+"", user.getIdMeja());
                            orderMenus.add(order);
                        }
                    }
                    for (int i = 0; i < orderMenus.size(); i++) {
                        arrOrder.remove(orderMenus.get(i));
                    }
                    Toast.makeText(getContext(), "Your order is being processed.", Toast.LENGTH_SHORT).show();
                    orderAdapter.notifyDataSetChanged();
                }
            }).setNegativeButton("Double check", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void addOrder(String customer, String menu, String jumlah, String meja){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"order/addOrder",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String pesan  = jsonObject.getString("message");
                            int kode = jsonObject.getInt("code");
                            if (kode!=1)
                                Toast.makeText(getContext(), pesan, Toast.LENGTH_SHORT).show();
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
                params.put("menu",menu);
                params.put("jumlah",jumlah);
                params.put("meja",meja);
                params.put("reward","0");
                params.put("stamp","0");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void checkIn(String meja, String userId, String kode){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"table/updateTable",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String pesan  = jsonObject.getString("message");
                            int kode = jsonObject.getInt("code");
                            Toast.makeText(getContext(), pesan, Toast.LENGTH_SHORT).show();
                            if (kode==1) {
                                user.setCheckIn(jsonObject.getString("meja"));
                                user.setIdMeja(jsonObject.getString("idMeja"));
                                System.out.println(user.getCheckIn());
                                System.out.println(user.getIdMeja());
                                btnCheckin.setEnabled(false);
                                btnCheckin.setText(user.getCheckIn());
                                bottomSheetDialog.dismiss();
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
                params.put("id",meja);
                params.put("user",userId);
                params.put("kode",kode);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void preExecuteLoad() {

    }

    @Override
    public void postExecuteLoad(List<OrderMenu> listMenu) {
        arrOrder.clear();
        arrOrder.addAll(listMenu);
        orderAdapter.notifyDataSetChanged();
        hitungTotal();
    }

    private void hitungTotal(){
        int subtotals = 0;
        for (int i = 0; i < arrOrder.size(); i++) {
            OrderMenu order = arrOrder.get(i);
            if (order.isConfirm()) subtotals += Integer.parseInt(order.getHarga_menu())*order.getJumlah();
        }

        subtotal.setText(currency(subtotals+""));
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

    @Override
    public void preExecuteUpdate() {

    }

    @Override
    public void postExecuteUpdate() {
        orderAdapter.notifyDataSetChanged();
        hitungTotal();
    }

    @Override
    public void preExecuteDelete() {

    }

    @Override
    public void postExecuteDelete() {
        hitungTotal();
    }

    private void getAvailableTable(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"table/",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int kode = jsonObject.getInt("code");
                            String pesan  = jsonObject.getString("message");

                            if (kode == 1){
                                arrMeja.clear();
                                JSONArray arr = jsonObject.getJSONArray("dataMeja");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject meja = arr.getJSONObject(i);
                                    arrMeja.add(new Table(meja.getInt("id")
                                            , meja.getString("nomor")
                                            , meja.getString("kode")
                                    ));
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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}

class UpdateCartAsync{
    private final WeakReference<Context> weakContext;
    private final WeakReference<UpdateCartCallback> weakCallback;
    private OrderMenu orderMenu;

    UpdateCartAsync(Context context, UpdateCartCallback updateCartCallback, OrderMenu orderMenu) {
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(updateCartCallback);
        this.orderMenu = orderMenu;
    }

    void execute() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        weakCallback.get().preExecuteUpdate();
        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase.database.orderDAO().update(orderMenu);
            handler.post(() -> weakCallback.get().postExecuteUpdate());
        });
    }

    interface UpdateCartCallback{
        void preExecuteUpdate();
        void postExecuteUpdate();
    }
}

class DeleteCartAsync{
    private final WeakReference<Context> weakContext;
    private final WeakReference<DeleteCartCallback> weakCallback;
    private OrderMenu orderMenu;

    DeleteCartAsync(Context context, DeleteCartCallback deleteCartCallback, OrderMenu orderMenu) {
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(deleteCartCallback);
        this.orderMenu = orderMenu;
    }

    void execute() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        weakCallback.get().preExecuteDelete();
        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase.database.orderDAO().delete(orderMenu);
            handler.post(() -> weakCallback.get().postExecuteDelete());
        });
    }

    interface DeleteCartCallback{
        void preExecuteDelete();
        void postExecuteDelete();
    }
}

class LoadCartAsync{
    private final WeakReference<Context> weakContext;
    private final WeakReference<LoadCartCallback> weakCallback;

    LoadCartAsync(Context context, LoadCartCallback updateCartCallback) {
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(updateCartCallback);
    }

    void execute() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        weakCallback.get().preExecuteLoad();
        executorService.execute(() -> {
            Context context = weakContext.get();
            List<OrderMenu> orderList = AppDatabase.database.orderDAO().getAllOrder();
            handler.post(() -> weakCallback.get().postExecuteLoad(orderList));
        });
    }

    interface LoadCartCallback{
        void preExecuteLoad();
        void postExecuteLoad(List<OrderMenu> listMenu);
    }
}