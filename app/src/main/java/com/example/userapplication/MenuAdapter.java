package com.example.userapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{
    JSONArray dataMenu;

    public MenuAdapter(JSONArray dataMenu) {
        this.dataMenu = dataMenu;
    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from((parent.getContext())).inflate(R.layout.recycler_menu,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, int position) {
        JSONObject itemnow;
        try {
            itemnow = dataMenu.getJSONObject(position);
            holder.tvNama.setText(itemnow.getString("nama_menu"));
            holder.tvHarga.setText(itemnow.getString("harga_menu"));
            holder.tvDesc.setText(itemnow.getString("deskripsi_menu"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return dataMenu.length();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvNama, tvHarga, tvDesc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imgDispMenu);
            tvNama=itemView.findViewById(R.id.txtNamaMenu);
            tvHarga=itemView.findViewById(R.id.txtHargaMenu);
            tvDesc=itemView.findViewById(R.id.txtDescMenu);
        }
    }

}