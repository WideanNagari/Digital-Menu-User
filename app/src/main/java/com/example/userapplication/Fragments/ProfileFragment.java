package com.example.userapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.EditProfileActivity;
import com.example.userapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private UserApp loggedIn;
//    private String mParam2;

    //SEMUA VARIABEL SEMENTARA DIREMARK, SAAT INI BELUM DIPAKAI


    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(UserApp loggedIn) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, loggedIn);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loggedIn = getArguments().getParcelable(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    TextView name, email, cash, phone;
    Button btnEdit;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.tv_profilename);
        email = view.findViewById(R.id.tv_profileemail);
        cash = view.findViewById(R.id.tv_profilesaldo);
        phone = view.findViewById(R.id.tv_profilephonenum);
        btnEdit = view.findViewById(R.id.btn_profileedit);
        name.setText(loggedIn.getName());
        email.setText(loggedIn.getEmail());
        cash.setText("Rp "+String.format("%,.2f", new Double(loggedIn.getSaldo())));
        phone.setText(loggedIn.getTelp());
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(getActivity(), EditProfileActivity.class);
                edit.putExtra("loggedIn",loggedIn);
                startActivity(edit);
            }
        });
    }
}