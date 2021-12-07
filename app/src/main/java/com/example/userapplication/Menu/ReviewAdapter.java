package com.example.userapplication.Menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapplication.Classes.Review;
import com.example.userapplication.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private ArrayList<Review> arrayListReview = new ArrayList<>();

    public ReviewAdapter(ArrayList<Review> arrayListReview) {
        this.arrayListReview = arrayListReview;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review_menu,
                        parent,
                        false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayListReview.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaPeopleReview, namaMenuReview;
        ImageView img;
        RatingBar ratingBar;
        EditText isiReview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaMenuReview = itemView.findViewById(R.id.namaMenuReview);
            namaPeopleReview = itemView.findViewById(R.id.namaPeopleReview);
            img = itemView.findViewById(R.id.imageView9);
            ratingBar = itemView.findViewById(R.id.ratingReview);
            isiReview = itemView.findViewById(R.id.isiReview);
        }
    }
}
