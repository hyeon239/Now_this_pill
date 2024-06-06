package com.example.now_this_pill;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView timeNameTextView;
    //public Button actionButton;

    public ItemViewHolder(View itemView) {
        super(itemView);
        timeNameTextView = itemView.findViewById(R.id.time_name);
        //actionButton = itemView.findViewById(R.id.button);
    }
}
