package com.example.userapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapplication.Classes.Reward;

import java.util.ArrayList;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.ViewHolder>{

    ArrayList<Reward> arrReward;
    int userStamp;

    public void setUserStamp(int userStamp) {
        this.userStamp = userStamp;
    }

    OnClaimClick onClaimClick;

    public OnClaimClick getOnClaimClick() {
        return onClaimClick;
    }

    public void setOnClaimClick(OnClaimClick onClaimClick) {
        this.onClaimClick = onClaimClick;
    }

    public RewardAdapter(ArrayList<Reward> arrReward, int stamp) {
        this.arrReward = arrReward;
        this.userStamp = stamp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reward, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reward r = arrReward.get(position);

        holder.nama.setText(r.getReward());

        String stamps = "1 Stamp";
        if (r.getStamp()>1) stamps = r.getStamp()+" Stamps";
        holder.stamp.setText(stamps);

        if (userStamp>=r.getStamp()){
            holder.claim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClaimClick!=null) onClaimClick.onClaim(r);
                }
            });
        }else{
            holder.claim.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return arrReward.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView nama, stamp;
        Button claim;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgMenuReward);
            nama = itemView.findViewById(R.id.namaMenuReward);
            stamp = itemView.findViewById(R.id.stampMenu);
            claim = itemView.findViewById(R.id.btnClaim);
        }
    }

    public interface OnClaimClick{
        void onClaim(Reward reward);
    }
}
