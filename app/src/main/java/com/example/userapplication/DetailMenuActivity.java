package com.example.userapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailMenuActivity extends AppCompatActivity {
    ImageView imgV;
    TextView txtNama, txtHrg, txtDesc, txtJum, txtSub;
    ImageButton btnAdd, btnSub;
    Button btnAddtoCart, btnBack;
    Intent it;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgV=findViewById(R.id.imgDetailMenu);
        txtNama=findViewById(R.id.txtNamaDetail);
        txtHrg=findViewById(R.id.txtHargaDetail);
        txtDesc=findViewById(R.id.txtDescDetail);

        txtJum=findViewById(R.id.txtJum);
        txtSub=findViewById(R.id.txtSubtotal);
        btnAdd=findViewById(R.id.btnAdd);
        btnSub=findViewById(R.id.btnSub);
        btnAddtoCart=findViewById(R.id.btnAddToCart);

        btnBack=findViewById(R.id.btnBack);

        txtJum.setText(jum+"");

        it=getIntent();
        nama=it.getStringExtra("nama_detail");
        hrg=Integer.parseInt(it.getStringExtra("harga_detail"));
        desc=it.getStringExtra("desc_detail");

        setContentView(R.layout.activity_detail_menu);
    }
    int jum=0, hrg=0;
    String nama, desc;

    public void add(View v){
        jum++;
        txtJum.setText(jum+"");
        updateSub();
    }
    public void sub(View v){
        if(jum>0){
            jum--;
            txtJum.setText(jum+"");
        }
        updateSub();
    }
    public void updateSub(){
        int sub=hrg*jum;
        txtSub.setText("Subtotal: Rp. "+sub+",-");
    }
    public void back(View v){
        finish();
    }
}