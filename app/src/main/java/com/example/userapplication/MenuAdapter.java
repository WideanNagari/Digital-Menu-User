package com.example.userapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{
    ArrayList<Menu> arrMenu;
    OnItemClick onItemClick;

    public OnItemClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public MenuAdapter(ArrayList<Menu> arrMenu) {
        this.arrMenu = arrMenu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_menu, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, int position) {
        Menu m = arrMenu.get(position);

        holder.tvNama.setText(m.getNama_menu());
        holder.tvHarga.setText(currency(m.getHarga_menu()));
        holder.tvDesc.setText(m.getDeskripsi_menu());

        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClick!=null) onItemClick.onDetailClick(m);
            }
        });

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
        return arrMenu.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvNama, tvHarga, tvDesc;
        Button detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imgDispMenu);
            tvNama=itemView.findViewById(R.id.txtNamaMenu);
            tvHarga=itemView.findViewById(R.id.txtHargaMenu);
            tvDesc=itemView.findViewById(R.id.txtDescMenu);
            detail=itemView.findViewById(R.id.btnDetail);
        }
    }

    public interface OnItemClick{
        void onDetailClick(Menu m);
    }

}