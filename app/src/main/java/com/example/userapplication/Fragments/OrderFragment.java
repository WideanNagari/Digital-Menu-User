package com.example.userapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.userapplication.OrderAdapter;
import com.example.userapplication.OrderMenu;
import com.example.userapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    BottomNavigationView navbar;
    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<OrderMenu> arrCart = new ArrayList<>();
        arrCart.add(new OrderMenu("x", "menu satu", "25000", "deskripsi menu satu harganya 25 rebu", "x", "yes"));
        arrCart.add(new OrderMenu("x", "menu dua", "15500", "deskripsi menu dua harganya 15 rebu 5 ratus", "x", "yes"));
        arrCart.add(new OrderMenu("x", "menu tiga", "5000", "deskripsi menu tiga harganya 5 rebu", "x", "yes"));
        arrCart.add(new OrderMenu("x", "menu empat", "10000", "deskripsi menu empat harganya 10 rebu", "x", "yes"));
        arrCart.add(new OrderMenu("x", "menu lima", "71200", "deskripsi menu lima harganya 71 rebu 2 ratus", "x", "yes"));

        ArrayList<OrderMenu> arrOngoing = new ArrayList<>();
        arrOngoing.add(new OrderMenu("x", "menu satu", "25000", "deskripsi menu satu harganya 25 rebu", "x", "yes",1, "Pending"));
        arrOngoing.add(new OrderMenu("x", "menu dua", "15500", "deskripsi menu dua harganya 15 rebu 5 ratus", "x", "yes",2, "Confirmed"));
        arrOngoing.add(new OrderMenu("x", "menu tiga", "5000", "deskripsi menu tiga harganya 5 rebu", "x", "yes",3, "Pending"));
        arrOngoing.add(new OrderMenu("x", "menu empat", "10000", "deskripsi menu empat harganya 10 rebu", "x", "yes",10, "Confirmed"));
        arrOngoing.add(new OrderMenu("x", "menu lima", "71200", "deskripsi menu lima harganya 71 rebu 2 ratus", "x", "yes",22, "Cancelled"));
        navbar = view.findViewById(R.id.navigationOrder);
        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag;
                switch (item.getItemId()){
                    default:
                        frag = OrderCartFragment.newInstance(arrCart);
                        break;
                    case R.id.item_cart:
                        frag = OrderCartFragment.newInstance(arrCart);
                        break;
                    case R.id.item_ongoing:
                        frag = OrderOngoingFragment.newInstance(arrOngoing);
                        break;
                }
                try {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_order,frag).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        if (savedInstanceState == null) {
            navbar.setSelectedItemId(R.id.item_cart);
        }
    }
}