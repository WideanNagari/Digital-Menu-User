package com.example.userapplication.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.userapplication.AppDatabase;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.HomeActivity;
import com.example.userapplication.OrderAdapter;
import com.example.userapplication.OrderMenu;
import com.example.userapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment implements AddOrderAsync.AddOrderCallback  {

    BottomNavigationView navbar;
    UserApp user;
    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance(UserApp user) {
        OrderFragment fragment = new OrderFragment();
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
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("1", "menu satu", "25000", "deskripsi menu satu harganya 25 rebu", "x", "yes",1,"-")).execute();
//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("2", "menu dua", "15500", "deskripsi menu dua harganya 15 rebu 5 ratus", "x", "yes",1,"-")).execute();
//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("3", "menu tiga", "5000", "deskripsi menu tiga harganya 5 rebu", "x", "yes",1,"-")).execute();
//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("4", "menu empat", "10000", "deskripsi menu empat harganya 10 rebu", "x", "yes",1,"-")).execute();
//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("5", "menu lima", "71200", "deskripsi menu lima harganya 71 rebu 2 ratus", "x", "yes",1,"-")).execute();
//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("2", "menu dua", "71200", "deskripsi menu lima harganya 71 rebu 2 ratus", "x", "yes",1,"-")).execute();
//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("1", "menu satu", "71200", "deskripsi menu lima harganya 71 rebu 2 ratus", "x", "yes",1,"-")).execute();
//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("3", "menu tiga", "71200", "deskripsi menu lima harganya 71 rebu 2 ratus", "x", "yes",1,"-")).execute();

        navbar = view.findViewById(R.id.navigationOrder);
        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag;
                switch (item.getItemId()){
                    default:
                        frag = OrderCartFragment.newInstance(user);
                        break;
                    case R.id.item_cart:
                        frag = OrderCartFragment.newInstance(user);
                        break;
                    case R.id.item_ongoing:
                        frag = OrderOngoingFragment.newInstance(user);
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

        getActivity().getSupportFragmentManager().addFragmentOnAttachListener(new FragmentOnAttachListener() {
            @Override
            public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
                if (fragment instanceof OrderOngoingFragment) {
                    // total di trans, pemasukan, pengeluaran
                    OrderOngoingFragment orderOngoingFragment = (OrderOngoingFragment) fragment;
                    orderOngoingFragment.setOnActionListener(new OrderOngoingFragment.OnActionListener() {
                        @Override
                        public void onBack(UserApp u) {
                            user = u;
                            navbar.setSelectedItemId(R.id.item_cart);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void preExecuteAdd() {

    }

    @Override
    public void postExecuteAdd() {

    }
}

class AddOrderAsync{
    private final WeakReference<Context> weakContext;
    private final WeakReference<AddOrderCallback> weakCallback;
    private OrderMenu orderMenu;
    private AddOrderAsync addGameAsync;
    AddOrderAsync(Context weakContext, AddOrderCallback addGameCallback,OrderMenu orderMenu) {
        this.weakContext = new WeakReference<>(weakContext);
        this.weakCallback = new WeakReference<>(addGameCallback);
        this.orderMenu = orderMenu;
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakCallback.get().preExecuteAdd();
        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase.database.orderDAO().insert(orderMenu);
            handler.post(() -> weakCallback.get().postExecuteAdd());
        });
    }

    interface AddOrderCallback {
        void preExecuteAdd();
        void postExecuteAdd();
    }
}