package com.example.userapplication.Payment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.userapplication.Classes.OrderMenu;
import com.example.userapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder>{
    ArrayList<OrderMenu> arrOrder;
    Activity activity;
    public PaymentAdapter(Activity activity, ArrayList<OrderMenu> arrOrder) {
        this.arrOrder = arrOrder;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_purchase, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderMenu o = arrOrder.get(position);
        holder.nama.setText(o.getNama_menu());
        holder.harga.setText(o.getJumlah()+" x "+currency(o.getHarga_menu()));
        holder.total.setText(currency(o.getJumlah()*Integer.parseInt(o.getHarga_menu())+""));
        holder.jenis.setText(o.getJenis_menu());
        Glide.with(activity).load(o.getGambar()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return arrOrder.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView nama, harga, total, jenis;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgMenuPurchase);
            nama = itemView.findViewById(R.id.namaMenuPurchase);
            harga = itemView.findViewById(R.id.hargaMenuPurchase);
            total = itemView.findViewById(R.id.totalHargaPurchase);
            jenis = itemView.findViewById(R.id.history_jenis);
        }
    }
}
