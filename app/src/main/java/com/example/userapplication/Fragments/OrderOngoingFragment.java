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

import com.example.userapplication.OngoingAdapter;
import com.example.userapplication.OrderMenu;
import com.example.userapplication.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderOngoingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderOngoingFragment extends Fragment {

    ArrayList<OrderMenu> arrOrder;
    RecyclerView rv;
    OngoingAdapter ongoingAdapter;

    public OrderOngoingFragment() {
        // Required empty public constructor
    }

    public static OrderOngoingFragment newInstance(ArrayList<OrderMenu> order) {
        OrderOngoingFragment fragment = new OrderOngoingFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("orderOngoing", order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            arrOrder = getArguments().getParcelableArrayList("orderOngoing");
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

        rv = view.findViewById(R.id.rvOngoing);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        ongoingAdapter = new OngoingAdapter(arrOrder);
        rv.setAdapter(ongoingAdapter);
    }
}