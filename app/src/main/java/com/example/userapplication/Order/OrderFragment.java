package com.example.userapplication.Order;

import android.content.Context;
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

import com.example.userapplication.DAO.AppDatabase;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.Classes.OrderMenu;
import com.example.userapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment  {

    BottomNavigationView navbar;
    UserApp user;
    OnActionListener onActionListener;

    public OnActionListener getOnActionListener() {
        return onActionListener;
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

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

//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("1", "menu satu", "25000", "deskripsi menu satu harganya 25 rebu", "x", "yes",1,"-",0)).execute();
//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("2", "menu dua", "15500", "deskripsi menu dua harganya 15 rebu 5 ratus", "x", "yes",1,"-",0)).execute();
//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("3", "menu tiga", "5000", "deskripsi menu tiga harganya 5 rebu", "x", "yes",1,"-",0)).execute();
//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("4", "menu empat", "10000", "deskripsi menu empat harganya 10 rebu", "x", "yes",1,"-",0)).execute();
//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("5", "menu lima", "71200", "deskripsi menu lima harganya 71 rebu 2 ratus", "x", "yes",1,"-",0)).execute();
//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("2", "menu dua", "71200", "deskripsi menu lima harganya 71 rebu 2 ratus", "x", "yes",1,"-",0)).execute();
//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("1", "menu satu", "71200", "deskripsi menu lima harganya 71 rebu 2 ratus", "x", "yes",1,"-",0)).execute();
//        new AddOrderAsync(getContext(), OrderFragment.this, new OrderMenu("3", "menu tiga", "71200", "deskripsi menu lima harganya 71 rebu 2 ratus", "x", "yes",1,"-",0)).execute();

        navbar = view.findViewById(R.id.navigationOrder);
        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag;
                switch (item.getItemId()){
                    default:
                        frag = OrderCartFragment.newInstance(user, getContext(), getResources().getString(R.string.url));
                        break;
                    case R.id.item_cart:
                        frag = OrderCartFragment.newInstance(user, getContext(), getResources().getString(R.string.url));
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
                    OrderOngoingFragment orderOngoingFragment = (OrderOngoingFragment) fragment;
                    orderOngoingFragment.setOnActionListener(new OrderOngoingFragment.OnActionListener() {
                        @Override
                        public void onBack(UserApp u) {
                            user = u;
                            System.out.println("order"+user.getCheckIn());
                            if (onActionListener!=null) onActionListener.onBack(user);
                        }
                    });
                }
            }
        });
    }

    public interface OnActionListener{
        void onBack(UserApp user);
    }
}