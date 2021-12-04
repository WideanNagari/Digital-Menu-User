package com.example.userapplication.Menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.Classes.Menu;
import com.example.userapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    EditText edtSearch;
    Button btn_src, btn_detail;
    RecyclerView rv;
    MenuAdapter itemadapter;
    ArrayList<Menu> arrMenu;
    UserApp user;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(UserApp user) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_src = view.findViewById(R.id.btn_search);
        btn_detail = view.findViewById(R.id.btnDetail);
        edtSearch = view.findViewById(R.id.edt_SearchBar);
        rv = view.findViewById(R.id.rec_menu);

        arrMenu = new ArrayList<>();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        itemadapter = new MenuAdapter(arrMenu);
        rv.setAdapter(itemadapter);
        itemadapter.setOnItemClick(new MenuAdapter.OnItemClick() {
            @Override
            public void onDetailClick(Menu m) {
                Intent i = new Intent(getContext(), DetailMenuActivity.class);
                i.putExtra("menu",m);
                i.putExtra("user",user);
                startActivity(i);
            }
        });

        getAllMenu();

        btn_src.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                getQueryMenu();
            }
        });
    }

    private void getQueryMenu(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"menu/byQuery",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("WOY");
                            JSONObject jsonObject = new JSONObject(response);
                            int kode = jsonObject.getInt("code");
                            String pesan  = jsonObject.getString("message");
                            JSONArray arr = jsonObject.getJSONArray("dataMenu");
                            if (kode == 1){
//                                Toast.makeText(getActivity(), dataMenu.length()+" Menu Fetched", Toast.LENGTH_SHORT).show();
                                arrMenu.clear();
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject order = arr.getJSONObject(i);
                                    arrMenu.add(new Menu(order.getString("id")
                                            , order.getString("nama_menu")
                                            , order.getString("harga_menu")
                                            , order.getString("deskripsi_menu")
                                            , order.getString("jenis_menu")
                                            , order.getString("status_menu")
                                    ));
                                }
                                itemadapter.notifyDataSetChanged();
                            }else if(kode == -3){
//                                Toast.makeText(getActivity(), "No Menu", Toast.LENGTH_SHORT).show();
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
                params.put("query_string", edtSearch.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getAllMenu(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"menu",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("MASUK LALALA "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arr = jsonObject.getJSONArray("dataMenu");
                            int kode = jsonObject.getInt("code");
                            if (kode == 1){
                                arrMenu.clear();
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject order = arr.getJSONObject(i);
                                    arrMenu.add(new Menu(order.getString("id")
                                            , order.getString("nama_menu")
                                            , order.getString("harga_menu")
                                            , order.getString("deskripsi_menu")
                                            , order.getString("jenis_menu")
                                            , order.getString("status_menu")
                                    ));
                                }
//                                Toast.makeText(getActivity(), dataMenu.length()+" Menu Fetched", Toast.LENGTH_SHORT).show();
//                                System.out.println(dataMenu.length()+" INI HASILNYA");
                                itemadapter.notifyDataSetChanged();
                            }else if(kode == -3){
//                                Toast.makeText(getActivity(), "No Menu", Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}