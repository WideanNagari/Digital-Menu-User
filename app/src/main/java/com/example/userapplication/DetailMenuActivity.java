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
    ImageView imgV, imgLike;
    TextView txtNama, txtHrg, txtDesc, txtJum, txtSub;
    ImageButton btnAdd, btnSub;
    Button btnAddtoCart, btnBack;
    Intent i;
    boolean like;
    Menu menu;

    int jum=0;
    String nama, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);

        imgV=findViewById(R.id.imgDetailMenu);
        imgLike=findViewById(R.id.imgLike);
        txtNama=findViewById(R.id.txtNamaDetail);
        txtHrg=findViewById(R.id.txtHargaDetail);
        txtDesc=findViewById(R.id.txtDescDetail);

        txtJum=findViewById(R.id.txtJum);
        txtSub=findViewById(R.id.txtSubtotal);
        btnAdd=findViewById(R.id.btnAdd);
        btnSub=findViewById(R.id.btnSub);
        btnAddtoCart=findViewById(R.id.btnAddToCart);

        btnBack=findViewById(R.id.btnBack);

        txtJum.setText("1");

        i=getIntent();
        if (i.getExtras()!=null){
            if (i.hasExtra("menu")){
                menu = i.getParcelableExtra("menu");
            }
        }
        txtNama.setText(menu.getNama_menu());
        txtHrg.setText(currency(menu.getHarga_menu()+""));
        txtDesc.setText(menu.getDeskripsi_menu());

        like = true;
        if (like) imgLike.setImageResource(R.drawable.liked);
        else imgLike.setImageResource(R.drawable.like);
        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (like){
                    //ngelike
                    imgLike.setImageResource(R.drawable.liked);
                }else{
                    //ngunlike
                    imgLike.setImageResource(R.drawable.like);
                }
            }
        });
    }

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
        int sub=Integer.parseInt(menu.getHarga_menu())*jum;
        txtSub.setText("Subtotal: "+currency(sub+""));
    }

    private String currency(String angkaAwal){
        String hasil = "";
        if (angkaAwal.length()>=3){
            int ctr = 1;
            for (int i = angkaAwal.length()-1; i >= 0; i--) {
                hasil = angkaAwal.charAt(i) + hasil;
                if (ctr%3==0 && ctr<angkaAwal.length()) hasil = "."+hasil;
                ctr++;
            }
        }else{
            hasil = angkaAwal;
        }
        return "Rp. "+hasil;
    }
    public void back(View v){
        finish();
    }
}