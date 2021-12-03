package com.example.userapplication;

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

    @ColumnInfo(name = "jumlah")
    private int jumlah;

    @ColumnInfo(name = "confirm")
    private boolean confirm;

    @ColumnInfo(name = "status")
    private String status;

    private int reward_status;

    public OrderMenu(String id, String nama_menu, String harga_menu, String deskripsi_menu, String jenis_menu, String status_menu, int jumlah, String status, int reward_status) {
        super(id, nama_menu, harga_menu, deskripsi_menu, jenis_menu, status_menu);
        this.jumlah = jumlah;
        this.confirm = false;
        this.status = status;
        this.reward_status = reward_status;

        if (this.reward_status == 1){
            this.harga_menu = "0";
        }
    }

    public int getReward_status() {
        return reward_status;
    }

    public void setReward_status(int reward_status) {
        this.reward_status = reward_status;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
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

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(jumlah);
        parcel.writeByte((byte) (confirm ? 1 : 0));
        parcel.writeString(status);
    }

    protected OrderMenu(Parcel in) {
        super(in);
        jumlah = in.readInt();
        confirm = in.readByte() != 0;
        status = in.readString();
    }
}
