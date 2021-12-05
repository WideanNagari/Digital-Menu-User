package com.example.userapplication.Menu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapplication.Classes.Menu;
import com.example.userapplication.Classes.Type;
import com.example.userapplication.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroupSearchAdp extends RecyclerView.Adapter<GroupSearchAdp.ViewHolder> {
    private Activity activity;
    ArrayList<Menu> list;
    ArrayList<Type> listJenis;

    OnItemClick onItemClick;
    public OnItemClick getOnItemClick() {
        return onItemClick;
    }
    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick{
        void onDetailClick(Menu m);
    }

    public GroupSearchAdp(Activity activity, ArrayList<Menu> list, ArrayList<Type> jenis) {
        this.activity = activity;
        this.list = list;
        this.listJenis = jenis;
    }

    @NonNull
    @Override
    public GroupSearchAdp.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nested_search,
                        parent,
                        false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupSearchAdp.ViewHolder holder, int position) {
        Type type = listJenis.get(position);
        holder.tvName.setText(type.getNama());

        ArrayList<Menu> arrMenu = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getJenis_menu().equals(type.getNama()))
                arrMenu.add(list.get(i));
        }

        SearchAdapter searchAdapter = new SearchAdapter(arrMenu);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        holder.rvMember.setLayoutManager(layoutManager);
        holder.rvMember.setAdapter(searchAdapter);
        searchAdapter.setOnItemClick(new SearchAdapter.OnItemClick() {
            @Override
            public void onDetailClick(Menu m) {
                if (onItemClick!=null) onItemClick.onDetailClick(m);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listJenis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RecyclerView rvMember;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            rvMember = itemView.findViewById(R.id.rv_member);
        }
    }
}
