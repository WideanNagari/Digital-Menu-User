package com.example.userapplication.Liked;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.userapplication.Classes.Like;
import com.example.userapplication.Home.PopularAdapter;
import com.example.userapplication.R;

import java.util.ArrayList;

public class LikedActivity extends AppCompatActivity {

    AppCompatImageView back;
    RecyclerView recyclerView;
    LikedAdapter likedAdapter;
    ArrayList<Like> arrayListLike = new ArrayList<Like>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);

        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        recyclerView = findViewById(R.id.rvLiked);
        likedAdapter = new LikedAdapter(arrayListLike);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        recyclerView.setAdapter(likedAdapter);
    }
}