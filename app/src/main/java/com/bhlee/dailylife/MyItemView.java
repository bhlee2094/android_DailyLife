package com.bhlee.dailylife;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyItemView extends RecyclerView.ViewHolder {

    public TextView listTitle;
    public TextView masterId;

    public MyItemView(@NonNull View itemView) {
        super(itemView);
        listTitle = itemView.findViewById(R.id.list_title);
        masterId = itemView.findViewById(R.id.master_id);
    }
}
