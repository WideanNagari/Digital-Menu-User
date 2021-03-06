package com.example.userapplication.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.userapplication.Classes.OrderMenu;

import java.util.List;

@Dao
public interface OrderDAO {
    @Query("SELECT * FROM `order` Where status = '-' and customer = :id")
    List<OrderMenu> getAllOrder(String id);

    @Insert
    void insert(OrderMenu orderMenu);

    @Delete
    void delete(OrderMenu orderMenu);

    @Update
    void update(OrderMenu orderMenu);
}
