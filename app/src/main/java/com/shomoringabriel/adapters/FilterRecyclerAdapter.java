package com.shomoringabriel.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shomoringabriel.R;
import com.shomoringabriel.models.filterModel.userModel.FilterUserModel;

import java.util.List;

public class FilterRecyclerAdapter extends RecyclerView.Adapter<FilterRecyclerVH> {

    private List<FilterUserModel> filterUserModels;

    public FilterRecyclerAdapter(List<FilterUserModel> filterUserModels) {
        this.filterUserModels = filterUserModels;
    }

    @NonNull
    @Override
    public FilterRecyclerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.filter_list_item, parent, false);

        FilterRecyclerVH viewHolder = new FilterRecyclerVH(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FilterRecyclerVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return filterUserModels.size();
    }
}
