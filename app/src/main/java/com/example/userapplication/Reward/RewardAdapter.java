package com.example.userapplication.Reward;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapplication.Classes.Reward;
import com.example.userapplication.R;

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
        //if(viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_reward,
                            parent,
                            false);
            return new ViewHolder(view);
//        } else {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.item_loading,
//                            parent,
//                            false);
//            //return new LoadingHolder(view);
//            return new ViewHolder(view);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reward r = arrReward.get(position);

        holder.rewardName.setText(r.getReward());

        String stamps = "1 Stamp";
        if (r.getStamp()>1) stamps = r.getStamp()+" Stamps";
        holder.stamp.setText(stamps);

        if (userStamp>=r.getStamp()){
            holder.btn_claim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClaimClick!=null) onClaimClick.onClaim(r);
                }
            });
        }else{
            holder.btn_claim.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return arrReward.size();
    }

//    final int VIEW_TYPE_LOADING = 0;
//    final int VIEW_TYPE_ITEM = 1;
//    @Override
//    public int getItemViewType(int position) {
//        return arrReward.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView rewardImage;
        TextView rewardName, jenis, stamp;
        Button btn_claim;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rewardImage = itemView.findViewById(R.id.image_Reward);
            rewardName = itemView.findViewById(R.id.nama_reward);
            jenis = itemView.findViewById(R.id.jenis_promo);
            stamp = itemView.findViewById(R.id.jumlah_stamp);
            btn_claim = itemView.findViewById(R.id.btn_claim_reward);
        }
    }

//    public class LoadingHolder extends RecyclerView.ViewHolder{
//
//        public LoadingHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }

    public interface OnClaimClick{
        void onClaim(Reward reward);
    }
}
