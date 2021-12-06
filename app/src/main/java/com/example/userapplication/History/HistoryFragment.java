package com.example.userapplication.History;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userapplication.Classes.DJual;
import com.example.userapplication.Classes.HJual;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    UserApp user;
    RecyclerView rv;
    ArrayList<HJual> arrHistory;
    ArrayList<DJual> arrDHistory;
    GroupHistoryAdp historyAdp;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(UserApp user) {
        HistoryFragment fragment = new HistoryFragment();
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
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arrHistory = new ArrayList<>();
        arrDHistory = new ArrayList<>();
        rv = view.findViewById(R.id.rvHistory);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        historyAdp = new GroupHistoryAdp(getActivity(), arrHistory, arrDHistory);
        rv.setAdapter(historyAdp);
//        historyAdapter = new HistoryAdapter(arrHistory);
//        rv.setAdapter(historyAdapter);
//
//
        historyAdp.setOnItemClick(new GroupHistoryAdp.OnItemClick() {
            @Override
            public void onItemClicked(HJual h) {
                Intent i = new Intent(getContext(), DetailHistoryActivity.class);
                i.putExtra("hjual", h);
                i.putExtra("user", user);
                activityResultLauncher.launch(i);
            }
        });

        getHistory(user.getId()+"");
        getDHistory();
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data.hasExtra("done")){
                            getHistory(user.getId()+"");
                        }
                    }
                }
            }
    );

    private void getHistory(String id){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"transaction/",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int kode = jsonObject.getInt("code");
                            String pesan  = jsonObject.getString("message");

                            if (kode == 1){
                                arrHistory.clear();
                                JSONArray arr = jsonObject.getJSONArray("dataHjual");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject hjual = arr.getJSONObject(i);
                                    arrHistory.add(new HJual(hjual.getString("nomor_nota")
                                            , hjual.getString("promo")
                                            , hjual.getInt("potongan")
                                            , hjual.getInt("subtotal")
                                            , hjual.getInt("jumlah_item")
                                            , hjual.getInt("review_status")
                                            , hjual.getString("tanggal")
                                    ));
                                }
                                historyAdp.notifyDataSetChanged();
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
                params.put("id",id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getDHistory(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"transaction/getDetailNested",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int kode = jsonObject.getInt("code");
                            String pesan  = jsonObject.getString("message");

                            if (kode == 1){
                                arrDHistory.clear();
                                JSONArray arr = jsonObject.getJSONArray("dataDjual");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject djual = arr.getJSONObject(i);
                                    arrDHistory.add(new DJual(djual.getString("nomor_nota")
                                            , djual.getString("nama_menu")
                                            , djual.getString("jenis_menu")
                                            , djual.getDouble("rating")
                                            , djual.getInt("jumlah")
                                            , djual.getInt("harga_menu")
                                            , djual.getString("asset")
                                    ));
                                }
                                historyAdp.notifyDataSetChanged();
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