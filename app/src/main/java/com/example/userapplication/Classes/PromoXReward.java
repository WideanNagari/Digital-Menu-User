package com.example.userapplication.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class PromoXReward implements Parcelable {
    private String jenis;
    private int id;
    private String nama_promo;
    private int besar_promo;
    private int max_promo;
    private int min_spent;
    private String status;
    private String reward;
    private String id_menu;
    private int stamp;

    public PromoXReward(String jenis, int id, String nama_promo, int besar_promo, int max_promo, int min_spent, String status, String reward, String id_menu, int stamp) {
        this.jenis = jenis;
        this.id = id;
        this.nama_promo = nama_promo;
        this.besar_promo = besar_promo;
        this.max_promo = max_promo;
        this.min_spent = min_spent;
        this.status = status;
        this.reward = reward;
        this.id_menu = id_menu;
        this.stamp = stamp;
    }

    protected PromoXReward(Parcel in) {
        jenis = in.readString();
        id = in.readInt();
        nama_promo = in.readString();
        besar_promo = in.readInt();
        max_promo = in.readInt();
        min_spent = in.readInt();
        status = in.readString();
        reward = in.readString();
        id_menu = in.readString();
        stamp = in.readInt();
    }

    public static final Creator<PromoXReward> CREATOR = new Creator<PromoXReward>() {
        @Override
        public PromoXReward createFromParcel(Parcel in) {
            return new PromoXReward(in);
        }

        @Override
        public PromoXReward[] newArray(int size) {
            return new PromoXReward[size];
        }
    };

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

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

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public int getStamp() {
        return stamp;
    }

    public void setStamp(int stamp) {
        this.stamp = stamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(jenis);
        parcel.writeInt(id);
        parcel.writeString(nama_promo);
        parcel.writeInt(besar_promo);
        parcel.writeInt(max_promo);
        parcel.writeInt(min_spent);
        parcel.writeString(status);
        parcel.writeString(reward);
        parcel.writeString(id_menu);
        parcel.writeInt(stamp);
    }
}
