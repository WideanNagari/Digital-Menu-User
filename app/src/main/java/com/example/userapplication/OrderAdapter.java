package com.example.userapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    ArrayList<OrderMenu> arrOrder;

    public OrderAdapter(ArrayList<OrderMenu> arrOrder) {
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
                .inflate(R.layout.item_order, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderMenu o = arrOrder.get(position);
        holder.nama.setText(o.getNama_menu());
        holder.harga.setText(o.getHarga_menu());
        holder.jumlah.setText(o.getJumlah()+"");
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrOrder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox check;
        ImageView img;
        TextView nama, harga, jumlah;
        Button add, min;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.checkboxPilihOrder);
            img = itemView.findViewById(R.id.imgMenu);
            nama = itemView.findViewById(R.id.namaMenuOrder);
            harga = itemView.findViewById(R.id.hargaMenuOrder);
            jumlah = itemView.findViewById(R.id.jumlahOrder);
            add = itemView.findViewById(R.id.btnAddOrder);
            min = itemView.findViewById(R.id.btnMinOrder);
        }
    }
}
