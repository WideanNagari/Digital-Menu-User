package com.example.userapplication.Classes;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order")
public class OrderMenu extends Menu {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo()
    private int idOrder;

    @ColumnInfo(name = "customer")
    private int customer;

    @ColumnInfo(name = "jumlah")
    private int jumlah;

    @ColumnInfo(name = "confirm")
    private boolean confirm;

    @ColumnInfo(name = "status")
    private String status;

    private int reward_status;

    public OrderMenu(String id, String nama_menu, String harga_menu, String deskripsi_menu, String jenis_menu, String status_menu, double rating, int customer, int jumlah, String status, int reward_status, String gambar) {
        super(id, nama_menu, harga_menu, deskripsi_menu, jenis_menu, status_menu, gambar, rating);
        this.customer = customer;
        this.jumlah = jumlah;
        this.confirm = false;
        this.status = status;
        this.reward_status = reward_status;

        if (this.reward_status == 1){
            this.harga_menu = "0";
        }
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getReward_status() {
        return reward_status;
    }

    public void setReward_status(int reward_status) {
        this.reward_status = reward_status;
    }
}
