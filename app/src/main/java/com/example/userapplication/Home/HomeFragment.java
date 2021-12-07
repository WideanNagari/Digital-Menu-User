package com.example.userapplication.Home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userapplication.Classes.Menu;
import com.example.userapplication.Classes.PromoXReward;
import com.example.userapplication.Classes.Reward;
import com.example.userapplication.Menu.DetailMenuActivity;
import com.example.userapplication.Reward.ClaimRewardActivity;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.R;
import com.example.userapplication.Reward.RewardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    TextView nama, more_reward;
    UserApp user;
    OnActionListener onActionListener;

    RecyclerView rv_reward, rv_popular, rv_recommended,  rv_again;
    PromoRewardAdapter rewardAdapter;
    PopularAdapter popularAdapter, recommendAdapter, againAdapter;

    private ArrayList<PromoXReward> listReward = new ArrayList<>();
    private ArrayList<Menu> listPopular = new ArrayList<>();
    private ArrayList<Menu> listRecommended = new ArrayList<>();
    private ArrayList<Menu> listAgain = new ArrayList<>();

    public void setListReward(ArrayList<PromoXReward> listReward) {
        this.listReward.clear();
        this.listReward.addAll(listReward);
        rewardAdapter.notifyDataSetChanged();
    }

    public void setListPopular(ArrayList<Menu> listPopular) {
        this.listPopular.clear();
        this.listPopular.addAll(listPopular);
        popularAdapter.notifyDataSetChanged();
    }

    public void setListRecommended(ArrayList<Menu> listRecommended) {
        this.listRecommended.clear();
        this.listRecommended.addAll(listRecommended);
        recommendAdapter.notifyDataSetChanged();
    }

    public void setListAgain(ArrayList<Menu> listAgain) {
        this.listAgain.clear();
        this.listAgain.addAll(listAgain);
        againAdapter.notifyDataSetChanged();
    }

    public OnActionListener getOnActionListener() {
        return onActionListener;
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(UserApp u) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", u);
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listReward = new ArrayList<>();
        rewardAdapter = new PromoRewardAdapter(listReward, user.getStamp());
        rv_reward = view.findViewById(R.id.reward_recycler);
        rv_reward.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_reward.setAdapter(rewardAdapter);
        rewardAdapter.setOnClaimClick(new PromoRewardAdapter.OnClaimClick() {
            @Override
            public void onClaim(PromoXReward reward) {
                Toast.makeText(getContext(), "A", Toast.LENGTH_SHORT).show();
            }
        });

        listPopular = new ArrayList<>();
        popularAdapter = new PopularAdapter(getActivity(), listPopular);
        rv_popular = view.findViewById(R.id.popular_recycler);
        rv_popular.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_popular.setAdapter(popularAdapter);
        popularAdapter.setOnPilihListener(new PopularAdapter.OnPilihListener() {
            @Override
            public void clickMenu(Menu m) {
                Intent i = new Intent(getContext(), DetailMenuActivity.class);
                i.putExtra("menu",m);
                i.putExtra("user",user);
                startActivity(i);
            }
        });

        listRecommended = new ArrayList<>();
        recommendAdapter = new PopularAdapter(getActivity(), listRecommended);
        rv_recommended = view.findViewById(R.id.recommended_recycler);
        rv_recommended.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_recommended.setAdapter(recommendAdapter);
        recommendAdapter.setOnPilihListener(new PopularAdapter.OnPilihListener() {
            @Override
            public void clickMenu(Menu m) {
                Intent i = new Intent(getContext(), DetailMenuActivity.class);
                i.putExtra("menu",m);
                i.putExtra("user",user);
                startActivity(i);
            }
        });

        listAgain = new ArrayList<>();
        againAdapter = new PopularAdapter(getActivity(), listAgain);
        rv_again = view.findViewById(R.id.again_recycler);
        rv_again.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_again.setAdapter(againAdapter);
        againAdapter.setOnPilihListener(new PopularAdapter.OnPilihListener() {
            @Override
            public void clickMenu(Menu m) {
                Intent i = new Intent(getContext(), DetailMenuActivity.class);
                i.putExtra("menu",m);
                i.putExtra("user",user);
                startActivity(i);
            }
        });

        nama = view.findViewById(R.id.txtNamaUser);
        nama.setText("Hi, "+user.getName());

        more_reward = view.findViewById(R.id.txtMoreReward);
        more_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pindah ke activity reward
                Intent i = new Intent(getContext(), ClaimRewardActivity.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        if (onActionListener!=null) onActionListener.onReady(this);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data.hasExtra("customer")){
                            user = data.getParcelableExtra("customer");
                        }
                        if (data.hasExtra("done")){
                            if (onActionListener!=null) onActionListener.onBack(user);
                        }
                    }
                }
            }
    );

    public interface OnActionListener{
        void onBack(UserApp user);
        void onReady(HomeFragment homeFragment);
    }
}