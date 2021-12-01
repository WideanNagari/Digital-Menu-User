package com.example.userapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrderDAO {
    @Query("SELECT * FROM `order` Where status = '-'")
    List<OrderMenu> getAllOrder();

    @Insert
    void insert(OrderMenu orderMenu);

    @Delete
    void delete(OrderMenu orderMenu);

    @Update
    void update(OrderMenu orderMenu);
}
