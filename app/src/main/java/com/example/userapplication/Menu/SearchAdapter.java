package com.example.userapplication.Menu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.userapplication.Classes.Menu;
import com.example.userapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private ArrayList<Menu> listMenu = new ArrayList<>();
    OnItemClick onItemClick;
    Activity activity;

    public OnItemClick getOnItemClick() {
        return onItemClick;
    }
    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick{
        void onDetailClick(Menu m);
    }

    public SearchAdapter(Activity activity, ArrayList<Menu> listMenu) {
        this.listMenu = listMenu;
        this.activity = activity;
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
        Menu m = listMenu.get(position);
        holder.jenis.setText(m.getJenis_menu());
        holder.nama.setText(m.getNama_menu());
        holder.Rate.setText(m.getRating()+"");
        holder.price.setText(currency(m.getHarga_menu()));
        Glide.with(activity).load(m.getGambar()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClick!=null) onItemClick.onDetailClick(m);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView jenis, nama, Rate, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.food_image);
            jenis = itemView.findViewById(R.id.jenis_name);
            nama = itemView.findViewById(R.id.name_makanan);
            Rate = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
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
