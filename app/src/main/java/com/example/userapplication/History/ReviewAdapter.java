package com.example.userapplication.History;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapplication.Classes.Review;
import com.example.userapplication.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    ArrayList<Review> arrReview;
    String isi;

    public ReviewAdapter(ArrayList<Review> arrReview) {
        this.arrReview = arrReview;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = arrReview.get(position);

        holder.nama.setText(review.getNama_menu());
        holder.rating.setRating((float)review.getRating());

        holder.rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                review.setRating((int)v);
            }
        });

        isi = review.getIsiReview();

        holder.review.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isi = charSequence+"";
                if (isi.equals("")) isi = "-";
                review.setIsiReview(isi);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrReview.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama;
        RatingBar rating;
        EditText review;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.namaMenuReview);
            rating = itemView.findViewById(R.id.ratingReview);
            review = itemView.findViewById(R.id.isiReview);
        }
    }
}
