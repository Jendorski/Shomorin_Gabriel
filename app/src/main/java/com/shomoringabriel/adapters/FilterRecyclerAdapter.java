package com.shomoringabriel.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shomoringabriel.R;
import com.shomoringabriel.models.filterModel.userModel.FilterUserModel;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.shomoringabriel.utils.Utils.getAppContext;

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
        FilterUserModel filterUserModel = filterUserModels.get(position);

        holder.name.setText(filterUserModel.getFullName());

        holder.gender.setText(filterUserModel.getGender());

        itemGlider(holder.circleImageView, filterUserModel.getAvatar());

    }

    private void itemGlider(ImageView holder, String loadUrl){

        Single.just(Glide.with(getAppContext())
                .load(loadUrl)
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void stringColorBuilder(){
        
    }

    @Override
    public int getItemCount() {
        return filterUserModels.size();
    }
}
