package com.example.userapplication.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private int id_menu;
    private String nama_menu;
    private String isiReview;
    private int rating;

    public Review(int id_menu, String nama_menu) {
        this.id_menu = id_menu;
        this.nama_menu = nama_menu;
        this.isiReview = "-";
        this.rating = 1;
    }

    protected Review(Parcel in) {
        id_menu = in.readInt();
        nama_menu = in.readString();
        isiReview = in.readString();
        rating = in.readInt();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getIsiReview() {
        return isiReview;
    }

    public void setIsiReview(String isiReview) {
        this.isiReview = isiReview;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_menu);
        parcel.writeString(nama_menu);
        parcel.writeString(isiReview);
        parcel.writeInt(rating);
    }
}

