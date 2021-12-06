package com.example.userapplication.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Menu implements Parcelable {
    protected String id, nama_menu, harga_menu, deskripsi_menu, jenis_menu, status_menu;
    protected double rating;

    public Menu(String id, String nama_menu, String harga_menu, String deskripsi_menu, String jenis_menu, String status_menu, double rating) {
        this.id = id;
        this.nama_menu = nama_menu;
        this.harga_menu = harga_menu;
        this.deskripsi_menu = deskripsi_menu;
        this.jenis_menu = jenis_menu;
        this.status_menu = status_menu;
        this.rating = rating;
    }

    protected Menu(Parcel in) {
        id = in.readString();
        nama_menu = in.readString();
        harga_menu = in.readString();
        deskripsi_menu = in.readString();
        jenis_menu = in.readString();
        status_menu = in.readString();
        rating = in.readDouble();
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getHarga_menu() {
        return harga_menu;
    }

    public void setHarga_menu(String harga_menu) {
        this.harga_menu = harga_menu;
    }

    public String getDeskripsi_menu() {
        return deskripsi_menu;
    }

    public void setDeskripsi_menu(String deskripsi_menu) {
        this.deskripsi_menu = deskripsi_menu;
    }

    public String getJenis_menu() {
        return jenis_menu;
    }

    public void setJenis_menu(String jenis_menu) {
        this.jenis_menu = jenis_menu;
    }

    public String getStatus_menu() {
        return status_menu;
    }

    public void setStatus_menu(String status_menu) {
        this.status_menu = status_menu;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(nama_menu);
        parcel.writeString(harga_menu);
        parcel.writeString(deskripsi_menu);
        parcel.writeString(jenis_menu);
        parcel.writeString(status_menu);
        parcel.writeDouble(rating);
    }
}
