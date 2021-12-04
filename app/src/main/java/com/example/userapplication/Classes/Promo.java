package com.example.userapplication.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Promo implements Parcelable {
    private int id;
    private String nama_promo;
    private int besar_promo;
    private int max_promo;
    private int min_spent;
    private String status;

    public Promo(int id, String nama_promo, int besar_promo, int max_promo, int min_spent, String status) {
        this.id = id;
        this.nama_promo = nama_promo;
        this.besar_promo = besar_promo;
        this.max_promo = max_promo;
        this.min_spent = min_spent;
        this.status = status;
    }

    protected Promo(Parcel in) {
        id = in.readInt();
        nama_promo = in.readString();
        besar_promo = in.readInt();
        max_promo = in.readInt();
        min_spent = in.readInt();
        status = in.readString();
    }

    public static final Creator<Promo> CREATOR = new Creator<Promo>() {
        @Override
        public Promo createFromParcel(Parcel in) {
            return new Promo(in);
        }

        @Override
        public Promo[] newArray(int size) {
            return new Promo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_promo() {
        return nama_promo;
    }

    public void setNama_promo(String nama_promo) {
        this.nama_promo = nama_promo;
    }

    public int getBesar_promo() {
        return besar_promo;
    }

    public void setBesar_promo(int besar_promo) {
        this.besar_promo = besar_promo;
    }

    public int getMax_promo() {
        return max_promo;
    }

    public void setMax_promo(int max_promo) {
        this.max_promo = max_promo;
    }

    public int getMin_spent() {
        return min_spent;
    }

    public void setMin_spent(int min_spent) {
        this.min_spent = min_spent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nama_promo);
        parcel.writeInt(besar_promo);
        parcel.writeInt(max_promo);
        parcel.writeInt(min_spent);
        parcel.writeString(status);
    }
}
