package com.example.userapplication.Order;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.userapplication.Classes.OrderMenu;
import com.example.userapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    ArrayList<OrderMenu> arrOrder;
    OnOrderItemClick onOrderItemClick;
    Activity activity;

    public OnOrderItemClick getOnOrderItemClick() {
        return onOrderItemClick;
    }

    public void setOnOrderItemClick(OnOrderItemClick onOrderItemClick) {
        this.onOrderItemClick = onOrderItemClick;
    }

    public OrderAdapter(Activity activity, ArrayList<OrderMenu> arrOrder) {
        this.arrOrder = arrOrder;
        this.activity = activity;
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
        holder.harga.setText(currency(o.getHarga_menu()));
        holder.jumlah.setText(o.getJumlah()+"");
        Glide.with(activity).load(o.getGambarID()).into(holder.img);
        if (o.isConfirm()) holder.check.setChecked(true);
        else holder.check.setChecked(false);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onOrderItemClick!=null) onOrderItemClick.onAdd(o);
            }
        });

        holder.min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (o.getJumlah()>1){
                    if (onOrderItemClick!=null) onOrderItemClick.onMin(o);
                }
            }
        });

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.check.isChecked()){
                    if (onOrderItemClick!=null) onOrderItemClick.onCheck(o,true);
                }else{
                    if (onOrderItemClick!=null) onOrderItemClick.onCheck(o,false);
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onOrderItemClick!=null) onOrderItemClick.onDelete(o);
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
        Button add, min, delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.checkboxPilihOrder);
            img = itemView.findViewById(R.id.imgMenu);
            nama = itemView.findViewById(R.id.namaMenuOrder);
            harga = itemView.findViewById(R.id.hargaMenuOrder);
            jumlah = itemView.findViewById(R.id.jumlahOrder);
            add = itemView.findViewById(R.id.btnAddOrder);
            min = itemView.findViewById(R.id.btnMinOrder);
            delete = itemView.findViewById(R.id.btnDeleteOrder);
        }
    }

    private String currency(String angkaAwal){
        String hasil = "";

        if (angkaAwal.length()>=3){
            int ctr = 1;
            for (int i = angkaAwal.length()-1; i >= 0; i--) {
                hasil = angkaAwal.charAt(i) + hasil;
                if (ctr%3==0 && ctr<angkaAwal.length()) hasil = "."+hasil;
                ctr++;
            }
        }else{
            hasil = angkaAwal;
        }
        return "Rp. "+hasil;
    }

    public interface OnOrderItemClick{
        void onAdd(OrderMenu orderMenu);
        void onMin(OrderMenu orderMenu);
        void onCheck(OrderMenu orderMenu, boolean b);
        void onDelete(OrderMenu orderMenu);
    }
}
