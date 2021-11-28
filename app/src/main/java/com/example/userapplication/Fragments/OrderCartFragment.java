package com.example.userapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.userapplication.OrderAdapter;
import com.example.userapplication.OrderMenu;
import com.example.userapplication.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderCartFragment extends Fragment {

    ArrayList<OrderMenu> arrOrder;
    RecyclerView rv;
    OrderAdapter orderAdapter;

    public OrderCartFragment() {
        // Required empty public constructor
    }

    public static OrderCartFragment newInstance(ArrayList<OrderMenu> order) {
        OrderCartFragment fragment = new OrderCartFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("orderCart", order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            arrOrder = getArguments().getParcelableArrayList("orderCart");
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

        rv = view.findViewById(R.id.rvOrder);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(arrOrder);
        rv.setAdapter(orderAdapter);
    }
}