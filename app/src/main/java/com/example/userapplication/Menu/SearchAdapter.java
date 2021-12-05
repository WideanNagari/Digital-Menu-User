package com.example.userapplication.Menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapplication.Classes.Menu;
import com.example.userapplication.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private ArrayList<Menu> listMenu = new ArrayList<>();
    OnItemClick onItemClick;

    public OnItemClick getOnItemClick() {
        return onItemClick;
    }
    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick{
        void onDetailClick(Menu m);
    }

    public SearchAdapter(ArrayList<Menu> listMenu) {
        this.listMenu = listMenu;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search,
                        parent,
                        false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        holder.jenis.setText(listMenu.get(position).getJenis_menu());
        holder.nama.setText(listMenu.get(position).getNama_menu());
        holder.Rate.setText(listMenu.get(position).getRating()+"");
        holder.price.setText(listMenu.get(position).getHarga_menu()+"");
    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView jenis, nama, Rate, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.food_image);
            jenis = itemView.findViewById(R.id.jenis_name);
            nama = itemView.findViewById(R.id.name_makanan);
            Rate = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
        }
    }
}
