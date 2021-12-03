package com.example.userapplication;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "like")
public class Like {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo()
    private int idLike;

    @ColumnInfo(name = "customer_id")
    private String customer;

    @ColumnInfo(name = "menu_id")
    private String menu;

    public Like(String customer, String menu) {
        this.customer = customer;
        this.menu = menu;
    }

    public int getIdLike() {
        return idLike;
    }

    public void setIdLike(int idLike) {
        this.idLike = idLike;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}
