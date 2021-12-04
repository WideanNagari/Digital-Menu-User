package com.example.userapplication.Home;

import android.content.Intent;
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

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {
    private ArrayList<Menu> listpopular = new ArrayList<>();

    public PopularAdapter(ArrayList<Menu> listpopular) {
        this.listpopular = listpopular;
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
        holder.foodImage.setImageResource(R.drawable.popularfood2);
        holder.name.setText(listpopular.get(position).getNama_menu());
        holder.price.setText(listpopular.get(position).getHarga_menu()+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(context, DetailsActivity.class);
//                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listpopular.size();
    }

    public class PopularViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView price, name;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            price = itemView.findViewById(R.id.price_food);
            name = itemView.findViewById(R.id.name_food);
        }
    }
}
