package com.example.userapplication.History;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.userapplication.Classes.DJual;
import com.example.userapplication.Classes.HJual;
import com.example.userapplication.Classes.Menu;
import com.example.userapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NestedHistoryAdapter extends RecyclerView.Adapter<NestedHistoryAdapter.ViewHolder> {

    private ArrayList<DJual> list;
    Activity activity;

    public NestedHistoryAdapter(Activity activity, ArrayList<DJual> list) {
        this.list = list;
        this.activity = activity;
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
        DJual dJual = list.get(position);

        holder.txtNama.setText(dJual.getNama_menu());
        holder.txtJenis.setText(dJual.getJenis_menu());
        holder.txtPrice.setText(currency(dJual.getHarga()+""));
        holder.txtRate.setText(dJual.getRating()+"");
        holder.txtJum.setText(dJual.getQuantity()+"");
        Glide.with(activity).load(dJual.getGambar()).into(holder.imgFood);

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
