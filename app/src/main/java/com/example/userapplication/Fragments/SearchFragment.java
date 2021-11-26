package com.example.userapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.userapplication.R;

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
    Button btn_src;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_search, container, false);
        btn_src = v.findViewById(R.id.btn_search);
        edtSearch=v.findViewById(R.id.edt_SearchBar);
        btn_src.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
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
                                    JSONObject dataMenu  = jsonObject.getJSONObject("dataMenu");
                                    System.out.println(dataMenu.length()+" INI HASILNYA");
                                    Toast.makeText(getActivity(), dataMenu.length()+" datamenu", Toast.LENGTH_SHORT).show();
                                    if (kode == 1){
//                                berhasil get all menu
                                        Toast.makeText(getActivity(), dataMenu.length()+"", Toast.LENGTH_SHORT).show();
                                    }else if(kode == -3){
//                                tidak ada data
                                        Toast.makeText(getActivity(), "No Menu Yet", Toast.LENGTH_SHORT).show();
                                    }

//                            Toast.makeText(LoginActivity.this, pesan, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    Toast.makeText(getActivity(), "Masuk catch", Toast.LENGTH_SHORT).show();

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
//                params.put("phone",noTelp.getText().toString());
//                params.put("password",password.getText().toString());
//                params.put("role","0");
                        System.out.println();
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                requestQueue.add(stringRequest);
            }
        });

        return v;
    }

    public void btn_Search(View v){

    }
}