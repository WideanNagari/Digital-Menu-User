package com.example.userapplication.DAO;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.userapplication.Classes.Like;
import com.example.userapplication.Classes.OrderMenu;

@Database(entities = {OrderMenu.class, Like.class}, version = 1)
public abstract class AppDatabase  extends RoomDatabase {
    public abstract OrderDAO orderDAO();
    public abstract LikeDAO likeDAO();

    public static AppDatabase database = null;
    public static void initDatabase(Context context, String databaseName){
        if(database == null){
            database = Room.databaseBuilder(context, AppDatabase.class, databaseName).build();
        }
    }
}
