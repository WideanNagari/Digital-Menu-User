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

import com.example.userapplication.Classes.Menu;
import com.example.userapplication.Classes.Reward;
import com.example.userapplication.Reward.ClaimRewardActivity;
import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    Button btn;
    UserApp user;
    OnActionListener onActionListener;

    private ArrayList<Reward> listReward = new ArrayList<>();
    private ArrayList<Menu> listPopular = new ArrayList<>();
    private ArrayList<Menu> listRecommended = new ArrayList<>();
    private ArrayList<Menu> listAgain = new ArrayList<>();

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

    RecyclerView rv_reward, rv_popular, rv_recommended,  rv_again;
    RewardAdapter rewardAdapter;
    PopularAdapter popularAdapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv_reward = view.findViewById(R.id.reward_recycler);
        rewardAdapter = new RewardAdapter(listReward);
        rv_reward.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_reward.setAdapter(rewardAdapter);

        rv_popular = view.findViewById(R.id.popular_recycler);
        popularAdapter = new PopularAdapter(listPopular);
        rv_popular.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_popular.setAdapter(popularAdapter);

        rv_recommended = view.findViewById(R.id.recommended_recycler);
        popularAdapter = new PopularAdapter(listPopular);
        rv_recommended.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_recommended.setAdapter(popularAdapter);

        rv_again = view.findViewById(R.id.again_recycler);
        popularAdapter = new PopularAdapter(listPopular);
        rv_again.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_again.setAdapter(popularAdapter);

//        btn = view.findViewById(R.id.goClaim);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getContext(), ClaimRewardActivity.class);
//                i.putExtra("user", user);
//                activityResultLauncher.launch(i);
//            }
//        });
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
    }
}