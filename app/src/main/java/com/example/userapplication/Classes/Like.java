package com.example.userapplication.Classes;

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
}
