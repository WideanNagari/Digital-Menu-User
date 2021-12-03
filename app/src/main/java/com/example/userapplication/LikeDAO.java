package com.example.userapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.userapplication.Classes.Like;

import java.util.List;

@Dao
public interface LikeDAO {
    @Query("SELECT * FROM `like` WHERE customer_id = :id")
    List<Like> getAllLike(String id);

    //like
    @Insert
    void insert(Like like);

    //unlike
    @Delete
    void delete(Like like);
}
