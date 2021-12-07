package com.example.userapplication.Liked;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapplication.Classes.Like;
import com.example.userapplication.Home.PopularAdapter;
import com.example.userapplication.R;

import java.util.ArrayList;

public class LikedAdapter extends RecyclerView.Adapter<LikedAdapter.ViewHolder> {
    private ArrayList<Like> arrayListLike = new ArrayList<>();

    public LikedAdapter(ArrayList<Like> arrayListLike) {
        this.arrayListLike = arrayListLike;
    }

    @NonNull
    @Override
    public LikedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_like,
                        parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayListLike.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, liked;
        TextView jenis,nama,rate, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.history_image);
            jenis = itemView.findViewById(R.id.history_jenis);
            nama = itemView.findViewById(R.id.history_nama);
            rate = itemView.findViewById(R.id.history_rate);
            liked = itemView.findViewById(R.id.img_liked);
            price = itemView.findViewById(R.id.history_price);
        }
    }
}
