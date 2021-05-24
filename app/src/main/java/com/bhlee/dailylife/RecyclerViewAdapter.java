package com.bhlee.dailylife;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyItemView> {

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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyItemView extends RecyclerView.ViewHolder {

        public TextView listTitle;

        public MyItemView(@NonNull View itemView) {
            super(itemView);
            listTitle = itemView.findViewById(R.id.list_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ListActivity.class);
                    intent.putExtra("listTitle", list.get(getAdapterPosition()).getListTitle());
                    intent.putExtra("listId", list.get(getAdapterPosition()).getListId());
                    intent.putExtra("masterId", list.get(getAdapterPosition()).getMasterId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
