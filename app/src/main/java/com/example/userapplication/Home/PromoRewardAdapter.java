package com.example.userapplication.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapplication.Classes.PromoXReward;
import com.example.userapplication.Classes.Reward;
import com.example.userapplication.R;
import com.example.userapplication.Reward.RewardAdapter;

import java.util.ArrayList;

public class PromoRewardAdapter extends RecyclerView.Adapter<PromoRewardAdapter.ViewHolder>{
    ArrayList<PromoXReward> arrReward;
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

    public PromoRewardAdapter(ArrayList<PromoXReward> arrReward, int stamp) {
        this.arrReward = arrReward;
        this.userStamp = stamp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reward,
                        parent,
                        false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PromoXReward r = arrReward.get(position);

        if (r.getJenis().equals("promo")){
            holder.rewardName.setText(r.getNama_promo());
            holder.jenis.setText("Dapatkan Potongan Harga Sampai "+r.getBesar_promo()+"%!");
            holder.rewardImage.setImageResource(R.drawable.coupon);
            holder.btn_claim.setVisibility(View.GONE);
            holder.stamp.setText("");

        }else if (r.getJenis().equals("reward")){
            holder.rewardName.setText(r.getReward());
            holder.rewardImage.setImageResource(R.drawable.badge);
            holder.jenis.setText("Tukarkan stamp anda dengan menu "+r.getReward()+"!");

            String stamps = "1 Stamp";
            if (r.getStamp()>1) stamps = r.getStamp()+" Stamps";
            holder.stamp.setText(stamps);
            holder.btn_claim.setVisibility(View.VISIBLE);
//            if (userStamp>=r.getStamp()){
                holder.btn_claim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onClaimClick!=null) onClaimClick.onClaim(r);
                    }
                });
//            }
        }
    }

    @Override
    public int getItemCount() {
        return arrReward.size();
    }

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

    public interface OnClaimClick{
        void onClaim(PromoXReward reward);
    }
}