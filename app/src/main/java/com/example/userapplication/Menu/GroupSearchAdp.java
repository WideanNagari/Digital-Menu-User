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
import com.example.userapplication.R;

import java.util.ArrayList;

public class GroupSearchAdp extends RecyclerView.Adapter<GroupSearchAdp.ViewHolder> {
    private Activity activity;
    ArrayList<Menu> list;

    public GroupSearchAdp(Activity activity, ArrayList<Menu> list) {
        this.activity = activity;
        this.list = list;
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
        holder.tvName.setText(list.get(position).getJenis_menu());

        SearchAdapter searchAdapter = new SearchAdapter(list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        holder.rvMember.setLayoutManager(layoutManager);
        holder.rvMember.setAdapter(searchAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
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
