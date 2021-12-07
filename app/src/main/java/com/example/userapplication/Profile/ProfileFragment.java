package com.example.userapplication.Profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.userapplication.Classes.UserApp;
import com.example.userapplication.Home.HomeActivity;
import com.example.userapplication.Liked.LikedActivity;
import com.example.userapplication.LoginActivity;
import com.example.userapplication.R;
import com.example.userapplication.Reward.ClaimRewardActivity;
import com.example.userapplication.TopUp.TopUpCashActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private UserApp loggedIn;
    OnActionListener onActionListener;

    public OnActionListener getOnActionListener() {
        return onActionListener;
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public static ProfileFragment newInstance(UserApp loggedIn) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable("loggedIn", loggedIn);
        System.out.println(" stamp "+loggedIn.getStamp());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loggedIn = getArguments().getParcelable("loggedIn");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    TextView name, email, cash, phone, stamps;
    //Button btnEdit, btnPass;
    CardView btn_topup, btn_reward, btn_edit, btn_like;
    AppCompatImageView btn_pass;
    ConstraintLayout logout;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.tv_profilename);
        email = view.findViewById(R.id.tv_profileemail);
        cash = view.findViewById(R.id.tv_profilesaldo);
        phone = view.findViewById(R.id.tv_profilephonenum);

        //stamps = view.findViewById(R.id.tv_profilestamps);
        //btnEdit = view.findViewById(R.id.btn_profileedit);
        //btnPass = view.findViewById(R.id.btn_updpass);

        btn_edit = view.findViewById(R.id.card_edit);
        btn_reward = view.findViewById(R.id.card_reward);
        btn_topup = view.findViewById(R.id.card_topup);
        btn_pass = view.findViewById(R.id.img_edit_pass);
        logout = view.findViewById(R.id.layout_logout);
        btn_like = view.findViewById(R.id.card_like);

        name.setText(loggedIn.getName());
        email.setText(loggedIn.getEmail());
        cash.setText("Rp "+String.format("%,.2f", new Double(loggedIn.getSaldo())));
        phone.setText(loggedIn.getTelp());
        //stamps.setText(loggedIn.getStamp()+" stamps");

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent i = result.getData();
                    if(i.hasExtra("loggedIn")){
                        loggedIn = i.getParcelableExtra("loggedIn");
                        name.setText(loggedIn.getName());
                        email.setText(loggedIn.getEmail());
                        cash.setText("Rp "+String.format("%,.2f", new Double(loggedIn.getSaldo())));
                        phone.setText(loggedIn.getTelp());
                        //set obyek user di home supaya baru
                        HomeActivity par = (HomeActivity) getActivity();
                        par.setLoggedIn(loggedIn);
                        System.out.println("c"+loggedIn.getStamp());
                    }

                    if (i.hasExtra("done")){
                        if (onActionListener!=null) onActionListener.onBack(loggedIn);
                    }
                }
            }
        });

        btn_topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //halaman top up
                Intent i = new Intent(getContext(), TopUpCashActivity.class);
                i.putExtra("loggedIn",loggedIn);
                launcher.launch(i);

            }
        });
        btn_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ClaimRewardActivity.class);
                i.putExtra("user",loggedIn);
                launcher.launch(i);
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(getActivity(), EditProfileActivity.class);
                edit.putExtra("loggedIn",loggedIn);
                launcher.launch(edit);
            }
        });
        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(getActivity(), EditPasswordActivity.class);
                edit.putExtra("loggedIn",loggedIn);
                launcher.launch(edit);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logout
                Intent i = new Intent(getContext(), LoginActivity.class);
                launcher.launch(i);
            }
        });
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //halaman like
                Intent liked = new Intent(getActivity(), LikedActivity.class);
                liked.putExtra("user",loggedIn);
                launcher.launch(liked);
            }
        });
    }

    public interface OnActionListener{
        void onBack(UserApp user);
    }
}