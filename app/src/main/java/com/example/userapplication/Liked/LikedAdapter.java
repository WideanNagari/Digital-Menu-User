package com.example.userapplication.Liked;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.userapplication.Classes.Like;
import com.example.userapplication.Classes.Menu;
import com.example.userapplication.Home.PopularAdapter;
import com.example.userapplication.R;

import java.util.ArrayList;

public class LikedAdapter extends RecyclerView.Adapter<LikedAdapter.ViewHolder> {
    private ArrayList<Menu> arrMenu;
    Activity activity;
    OnClick onClick;

    public OnClick getOnClick() {
        return onClick;
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public LikedAdapter(Activity activity, ArrayList<Menu> arrMenu) {
        this.arrMenu = arrMenu;
        this.activity = activity;
    }

    @NonNull
    @Override
    public LikedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_like,
                        parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedAdapter.ViewHolder holder, int position) {
        Menu m = arrMenu.get(position);
        holder.jenis.setText(m.getJenis_menu());
        holder.nama.setText(m.getNama_menu());
        holder.rate.setText(m.getRating()+"");
        holder.price.setText(currency(m.getHarga_menu()));
        Glide.with(activity).load(m.getGambar()).into(holder.image);

        holder.liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClick!=null) onClick.onUnlike(m);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrMenu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, liked;
        TextView jenis,nama,rate, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.history_image);
            jenis = itemView.findViewById(R.id.history_jenis);
            nama = itemView.findViewById(R.id.history_nama);
            rate = itemView.findViewById(R.id.history_rate);
            liked = itemView.findViewById(R.id.img_liked);
            price = itemView.findViewById(R.id.history_price);
        }
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

    public interface OnClick{
        void onUnlike(Menu m);
    }
}
