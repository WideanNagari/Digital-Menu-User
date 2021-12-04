package com.example.userapplication.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapplication.Classes.Reward;
import com.example.userapplication.R;

import java.util.ArrayList;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder> {
    private ArrayList<Reward> rewardsList;

    public RewardAdapter(ArrayList<Reward> rewardsList) {
        this.rewardsList = rewardsList;
    }

    @NonNull
    @Override
    public RewardAdapter.RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_reward,
                        parent,
                        false);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardAdapter.RewardViewHolder holder, int position) {
        holder.rewardName.setText(rewardsList.get(position).getReward());

    }

    @Override
    public int getItemCount() {
        return rewardsList.size();
    }

    public class RewardViewHolder extends RecyclerView.ViewHolder {
        ImageView rewardImage;
        TextView rewardName;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);

            rewardImage = itemView.findViewById(R.id.image_reward);
            rewardName = itemView.findViewById(R.id.name_reward);
        }
    }
}
