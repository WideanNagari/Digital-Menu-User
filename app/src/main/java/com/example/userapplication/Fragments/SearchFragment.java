package com.example.userapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.userapplication.DetailMenuActivity;
import com.example.userapplication.HomeActivity;
import com.example.userapplication.MainActivity;
import com.example.userapplication.MenuAdapter;
import com.example.userapplication.OnItemClickListener;
import com.example.userapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {


    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    EditText edtSearch;
    Button btn_src, btn_detail;
    RecyclerView rv;
    MenuAdapter itemadapter;
    JSONArray dataMenu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_search, container, false);
        btn_src = v.findViewById(R.id.btn_search);
        btn_detail = v.findViewById(R.id.btnDetail);
        edtSearch=v.findViewById(R.id.edt_SearchBar);
        rv=v.findViewById(R.id.rec_menu);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url)+"menu",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                                System.out.println("MASUK LALALA "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int kode = jsonObject.getInt("code");
                            dataMenu  = jsonObject.getJSONArray("dataMenu");

                            if (kode == 1){
//                                berhasil get all menu
                                Toast.makeText(getActivity(), dataMenu.length()+" Menu Fetched", Toast.LENGTH_SHORT).show();
                                System.out.println(dataMenu.length()+" INI HASILNYA");
                                showRecycler();
                            }else if(kode == -3){
//                                tidak ada data
                                Toast.makeText(getActivity(), "No Menu", Toast.LENGTH_SHORT).show();
                            }
//                            Toast.makeText(LoginActivity.this, pesan, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
//                            Toast.makeText(getActivity(), "Masuk catch", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },

                //untuk handle error
                new Response.ErrorListener() {
                    private VolleyError error;
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        this.error = error;
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
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(stringRequest);

        itemadapter.onClick(new OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent it=new Intent(getActivity(), DetailMenuActivity.class);
                try {
                    JSONObject data_now=dataMenu.getJSONObject(position);

                    it.putExtra("nama_detail", data_now.getString("nama_menu"));
                    it.putExtra("harga_detail", data_now.getString("harga_menu"));
                    it.putExtra("desc_detail", data_now.getString("desc_menu"));
                    startActivity(it);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        btn_src.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Toast.makeText(getActivity(), edtSearch.getText().toString(), Toast.LENGTH_SHORT).show();
                // do something
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
                                    dataMenu  = jsonObject.getJSONArray("dataMenu");
//                                    System.out.println(kode + "");
//                                    Toast.makeText(getActivity(), dataMenu.length()+" datamenu", Toast.LENGTH_SHORT).show();
                                    if (kode == 1){
//                                berhasil get all menu
//                                        System.out.println(dataMenu.length()+" INI HASILNYA QUERY");
                                        Toast.makeText(getActivity(), dataMenu.length()+" Menu Fetched", Toast.LENGTH_SHORT).show();
                                        showRecycler();
                                    }else if(kode == -3){
//                                tidak ada data
                                        Toast.makeText(getActivity(), "No Menu", Toast.LENGTH_SHORT).show();
                                    }

//                            Toast.makeText(LoginActivity.this, pesan, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
//                                    Toast.makeText(getActivity(), "Masuk catch", Toast.LENGTH_SHORT).show();

                                    e.printStackTrace();
                                }
                            }
                        },

                        //untuk handle error
                        new Response.ErrorListener() {
                            private VolleyError error;
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                this.error = error;
                                System.out.println(error.getMessage());
                            }
                        }
                ){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("query_string",edtSearch.getText().toString());
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                requestQueue.add(stringRequest);
                showRecycler();
            }
        });
        /*btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        return v;
    }
    private void showRecycler() {
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        itemadapter = new MenuAdapter(dataMenu);
        rv.setAdapter(itemadapter);
        itemadapter.notifyDataSetChanged();
    }
}