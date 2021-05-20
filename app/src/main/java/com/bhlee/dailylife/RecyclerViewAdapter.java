package com.bhlee.dailylife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<MyItemView> {

    private ArrayList<DailyList> list = new ArrayList<>();

    public RecyclerViewAdapter(ArrayList<DailyList> list){
        this.list = list;
    }

    @NonNull
    @Override
    public MyItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dailylist_item, parent, false);
        MyItemView myItemView = new MyItemView(view);
        return myItemView;
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemView holder, int position) {
        DailyList dailyList = list.get(position);
        holder.listTitle.setText(dailyList.getListTitle());
        holder.masterId.setText(dailyList.getMasterId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
