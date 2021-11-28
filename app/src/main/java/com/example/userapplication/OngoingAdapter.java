package com.example.userapplication;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OngoingAdapter  extends RecyclerView.Adapter<OngoingAdapter.ViewHolder>{
    ArrayList<OrderMenu> arrOrder;

    public OngoingAdapter(ArrayList<OrderMenu> arrOrder) {
        this.arrOrder = arrOrder;
    }

    public ArrayList<OrderMenu> getArrOrder() {
        return arrOrder;
    }

    public void setArrOrder(ArrayList<OrderMenu> arrOrder) {
        this.arrOrder = arrOrder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ongoing, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderMenu o = arrOrder.get(position);
        holder.nama.setText(o.getNama_menu());
        holder.harga.setText(o.getJumlah()+" x "+o.getHarga_menu());
        holder.status.setText(o.getStatus());

        int warna = Color.parseColor("#FF8FA3");
        if (o.getStatus().equals("Confirmed")) warna = Color.parseColor("#FF4D6D");
        else if (o.getStatus().equals("Cancelled")) warna = Color.parseColor("#C9184A");
        holder.status.getBackground().setColorFilter(warna, PorterDuff.Mode.SRC);
    }

    @Override
    public int getItemCount() {
        return arrOrder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView nama, harga, status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgMenuOngoing);
            nama = itemView.findViewById(R.id.namaMenuOngoing);
            harga = itemView.findViewById(R.id.hargaMenuOngoing);
            status = itemView.findViewById(R.id.statusMenu);
        }
    }
}
