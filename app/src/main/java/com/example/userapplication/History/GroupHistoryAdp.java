package com.example.userapplication.History;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapplication.Classes.DJual;
import com.example.userapplication.Classes.HJual;
import com.example.userapplication.R;

import java.util.ArrayList;

public class GroupHistoryAdp extends RecyclerView.Adapter<GroupHistoryAdp.ViewHolder> {
    private Activity activity;
    ArrayList<HJual> arrHistory;
    ArrayList<DJual> arrItem;
    OnItemClick onItemClick;

    public OnItemClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public GroupHistoryAdp.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_each_history,
                        parent,
                        false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHistoryAdp.ViewHolder holder, int position) {
        HJual hJual = arrHistory.get(position);

        holder.tvName.setText(hJual.getNomor_nota());
        holder.date.setText(hJual.getTanggal());
        holder.jumtotal.setText(hJual.getJumlahItem()+(hJual.getJumlahItem()>1?" Items":" Item"));

        boolean isExpandable = hJual.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if (isExpandable){
            holder.mArrowImage.setImageResource(R.drawable.ic_up);
        }else{
            holder.mArrowImage.setImageResource(R.drawable.ic_down);
        }

        ArrayList<DJual> arrPass = new ArrayList<>();
        for (int i = 0; i < arrItem.size(); i++) {
//            System.out.println(arrItem.get(i).getNota()+" "+hJual.getNomor_nota());
            if (arrItem.get(i).getNota().equals(hJual.getNomor_nota())){
                arrPass.add(arrItem.get(i));
            }
        }
        NestedHistoryAdapter adapter = new NestedHistoryAdapter(arrPass);
        holder.rvMember.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvMember.setHasFixedSize(true);
        holder.rvMember.setAdapter(adapter);
        holder.mArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hJual.setExpandable(!hJual.isExpandable());
                notifyItemChanged(holder.getLayoutPosition());
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick!=null) onItemClick.onItemClicked(hJual);
            }
        });
    }

    public GroupHistoryAdp(Activity activity, ArrayList<HJual> arrHistory, ArrayList<DJual> arrD) {
        this.activity = activity;
        this.arrHistory = arrHistory;
        this.arrItem = arrD;
    }

    @Override
    public int getItemCount() {
        return arrHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, date, jumtotal;

        private LinearLayout linearLayout;
        private RelativeLayout expandableLayout;
        private ImageView mArrowImage;
        private RecyclerView rvMember;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            mArrowImage = itemView.findViewById(R.id.arro_imageview);
            rvMember = itemView.findViewById(R.id.child_rv);

            date = itemView.findViewById(R.id.itemDate);
            tvName = itemView.findViewById(R.id.itemTv);
            jumtotal = itemView.findViewById(R.id.itemTotal);
        }
    }

    public interface OnItemClick{
        void onItemClicked(HJual h);
    }
}
