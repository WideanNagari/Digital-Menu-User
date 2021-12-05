package com.example.userapplication.History;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapplication.Classes.HJual;
import com.example.userapplication.Classes.Menu;
import com.example.userapplication.R;

import java.util.ArrayList;

public class NestedHistoryAdapter extends RecyclerView.Adapter<NestedHistoryAdapter.ViewHolder> {

    private ArrayList<HJual> list;

    public NestedHistoryAdapter(ArrayList<HJual> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NestedHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedHistoryAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood, imgRate;
        TextView txtNama, txtJenis, txtPrice, txtRate, txtJum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.history_image);
            imgRate = itemView.findViewById(R.id.img_history_rate);
            txtNama = itemView.findViewById(R.id.history_nama);
            txtJenis = itemView.findViewById(R.id.history_jenis);
            txtPrice = itemView.findViewById(R.id.history_price);
            txtRate = itemView.findViewById(R.id.history_rate);
            txtJum = itemView.findViewById(R.id.history_jum);
        }
    }
}
