package com.example.userapplication.Home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapplication.Classes.Menu;
import com.example.userapplication.R;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {
    private ArrayList<Menu> listpopular = new ArrayList<>();
    OnPilihListener onPilihListener;

    public PopularAdapter(ArrayList<Menu> listpopular) {
        this.listpopular = listpopular;
    }

    public OnPilihListener getOnPilihListener() {
        return onPilihListener;
    }

    public void setOnPilihListener(OnPilihListener onPilihListener) {
        this.onPilihListener = onPilihListener;
    }

    @NonNull
    @Override
    public PopularAdapter.PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular,
                        parent, false);
        return new PopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.PopularViewHolder holder, int position) {
        Menu menu = listpopular.get(position);
        holder.foodImage.setImageResource(R.drawable.popularfood2);
        holder.name.setText(menu.getNama_menu());
        holder.price.setText(currency(menu.getHarga_menu()));
        holder.jenis.setText(menu.getJenis_menu());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPilihListener!=null) onPilihListener.clickMenu(menu);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listpopular.size();
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

    public class PopularViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView name, rate, jenis;
        Button price;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.image_food);
            price = itemView.findViewById(R.id.button_price);
            name = itemView.findViewById(R.id.recommended_name);
            jenis = itemView.findViewById(R.id.recommended_jenis);
            rate = itemView.findViewById(R.id.recommended_rating);
        }
    }

    public interface OnPilihListener{
        void clickMenu(Menu m);
    }
}
