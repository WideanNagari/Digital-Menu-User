package com.example.userapplication.Order;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class OngoingAdapter  extends RecyclerView.Adapter<OngoingAdapter.ViewHolder>{
    ArrayList<OrderMenu> arrOrder;
    Activity activity;

    public OngoingAdapter(Activity activity, ArrayList<OrderMenu> arrOrder) {
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
                .inflate(R.layout.item_ongoing, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderMenu o = arrOrder.get(position);
        holder.nama.setText(o.getNama_menu());
        if (o.getReward_status()==1){
            holder.statusReward.setVisibility(View.VISIBLE);
        }else{
            holder.statusReward.setVisibility(View.GONE);
        }
        holder.jumOrder.setText(o.getJumlah()+" X");
        holder.harga.setText(currency(o.getHarga_menu()));
        holder.status.setText(o.getStatus());
        Glide.with(activity).load(o.getGambar()).into(holder.img);

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
        TextView nama, harga, jumOrder, statusReward;
        CircularProgressButton status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgMenuOngoing);
            nama = itemView.findViewById(R.id.namaMenuOngoing);
            harga = itemView.findViewById(R.id.hargaMenuOngoing);
            jumOrder = itemView.findViewById(R.id.jumOrder);
            status = itemView.findViewById(R.id.statusMenu);
            statusReward = itemView.findViewById(R.id.rewardGa);
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
}
