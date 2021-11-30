package com.example.userapplication.Fragments;

import android.content.Context;
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

import com.example.userapplication.AppDatabase;
import com.example.userapplication.OrderAdapter;
import com.example.userapplication.OrderMenu;
import com.example.userapplication.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderCartFragment extends Fragment implements LoadCartAsync.LoadCartCallback {

    ArrayList<OrderMenu> arrOrder;
    RecyclerView rv;
    OrderAdapter orderAdapter;

    public OrderCartFragment() {
        // Required empty public constructor
    }

    public static OrderCartFragment newInstance() {
        OrderCartFragment fragment = new OrderCartFragment();
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
        return inflater.inflate(R.layout.fragment_order_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arrOrder = new ArrayList<>();
        rv = view.findViewById(R.id.rvOrder);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(arrOrder);
        rv.setAdapter(orderAdapter);

        new LoadCartAsync(getActivity(),OrderCartFragment.this).execute();
    }

    @Override
    public void preExecuteLoad() {

    }

    @Override
    public void postExecuteLoad(List<OrderMenu> listMenu) {
        arrOrder.clear();
        arrOrder.addAll(listMenu);
        orderAdapter.notifyDataSetChanged();
    }
}

class LoadCartAsync{
    private final WeakReference<Context> weakContext;
    private final WeakReference<LoadCartCallback> weakCallback;

    LoadCartAsync(Context context, LoadCartCallback loadCartCallback) {
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(loadCartCallback);
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