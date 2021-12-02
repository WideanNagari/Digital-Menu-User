package com.example.userapplication.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.userapplication.OrderMenu;

import java.util.ArrayList;

public class HJual implements Parcelable {
    private String nomor_nota;
    private String promo;
    private int potongan;
    private int subtotal;
    private int jumlahItem;
    private int review_status;
    private String tanggal;

    public HJual(String nomor_nota, String promo, int potongan, int subtotal, int jumlahItem, int review_status, String tanggal) {
        this.nomor_nota = nomor_nota;
        this.promo = promo;
        this.potongan = potongan;
        this.subtotal = subtotal;
        this.jumlahItem = jumlahItem;
        this.review_status = review_status;
        this.tanggal = tanggal;
    }

    protected HJual(Parcel in) {
        nomor_nota = in.readString();
        promo = in.readString();
        potongan = in.readInt();
        subtotal = in.readInt();
        jumlahItem = in.readInt();
        review_status = in.readInt();
        tanggal = in.readString();
    }

    public static final Creator<HJual> CREATOR = new Creator<HJual>() {
        @Override
        public HJual createFromParcel(Parcel in) {
            return new HJual(in);
        }

        @Override
        public HJual[] newArray(int size) {
            return new HJual[size];
        }
    };

    public String getNomor_nota() {
        return nomor_nota;
    }

    public void setNomor_nota(String nomor_nota) {
        this.nomor_nota = nomor_nota;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public int getPotongan() {
        return potongan;
    }

    public void setPotongan(int potongan) {
        this.potongan = potongan;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getJumlahItem() {
        return jumlahItem;
    }

    public void setJumlahItem(int jumlahItem) {
        this.jumlahItem = jumlahItem;
    }

    public int getReview_status() {
        return review_status;
    }

    public void setReview_status(int review_status) {
        this.review_status = review_status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nomor_nota);
        parcel.writeString(promo);
        parcel.writeInt(potongan);
        parcel.writeInt(subtotal);
        parcel.writeInt(jumlahItem);
        parcel.writeInt(review_status);
        parcel.writeString(tanggal);
    }
}
