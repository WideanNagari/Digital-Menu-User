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
        Review review = arrayListReview.get(position);

        holder.namaPeopleReview.setText(review.getNama_menu());
        holder.ratingBar.setRating((float)review.getRating());
        holder.isiReview.setText(review.getIsiReview());
//        holder.isiReview.setEnabled(false);

        System.out.println(review.getIsiReview());
        System.out.println(arrayListReview.size());
    }

    @Override
    public int getItemCount() {
        return arrayListReview.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaPeopleReview;
        RatingBar ratingBar;
        EditText isiReview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaPeopleReview = itemView.findViewById(R.id.namaPeopleReview);
            ratingBar = itemView.findViewById(R.id.ratingReview);
            isiReview = itemView.findViewById(R.id.isiReview);
        }
    }
}
