package com.example.userapplication.History;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapplication.Classes.HJual;
import com.example.userapplication.R;

import java.util.ArrayList;

public class HistoryAdapter  extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    ArrayList<HJual> arrHistory;
    OnItemClickCallback onItemClickCallback;

    public OnItemClickCallback getOnItemClickCallback() {
        return onItemClickCallback;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public HistoryAdapter(ArrayList<HJual> arrHistory) {
        this.arrHistory = arrHistory;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        HJual hJual = arrHistory.get(position);

        int jum = hJual.getJumlahItem();
        String jumlah;
        if (jum > 1) jumlah = jum+" Items";
        else jumlah = jum+" Item";

        holder.nota.setText(hJual.getNomor_nota());
        holder.jumlahItem.setText(jumlah);
        holder.tanggal.setText(hJual.getTanggal());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickCallback!=null) onItemClickCallback.onItemClicked(hJual);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView nota, jumlahItem, tanggal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgMenuHistory);
            nota = itemView.findViewById(R.id.notaHistory);
            jumlahItem = itemView.findViewById(R.id.jumlahItemHistory);
            tanggal = itemView.findViewById(R.id.tanggalHistory);
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(HJual h);
    }

}
