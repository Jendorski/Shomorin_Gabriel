package com.shomoringabriel.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shomoringabriel.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FilterRecyclerVH extends RecyclerView.ViewHolder {

    public CircleImageView circleImageView;

    public TextView name, gender, country, date, color;

    public CardView cardView;

    public FilterRecyclerVH(@NonNull View itemView) {
        super(itemView);

        circleImageView = itemView.findViewById(R.id.filter_circle_image);

        name = itemView.findViewById(R.id.filter_name);

        gender = itemView.findViewById(R.id.filter_gender);

        color = itemView.findViewById(R.id.filter_color);

        country = itemView.findViewById(R.id.filter_country);

        cardView = itemView.findViewById(R.id.filter_card_view);

    }

}
