package com.ruilebre.pandemicqueue.ui.storelist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ruilebre.pandemicqueue.R;

public class StoreListRecyclerViewHolder extends RecyclerView.ViewHolder {
    public CardView cardView;

    public ImageView image;
    public TextView name;
    public TextView description;
    public TextView numberWaitingTickets;

    public StoreListRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public StoreListRecyclerViewHolder(LayoutInflater inflater, ViewGroup container) {
        super(inflater.inflate(R.layout.store_item, container, false));

        cardView = itemView.findViewById(R.id.store_card_view);

        image = itemView.findViewById(R.id.store_image);
        name = itemView.findViewById(R.id.store_name);
        description = itemView.findViewById(R.id.store_location);
        numberWaitingTickets = itemView.findViewById(R.id.store_number_waiting_tickets);

    }
}
