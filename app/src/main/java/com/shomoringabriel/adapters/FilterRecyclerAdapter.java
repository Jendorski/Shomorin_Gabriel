package com.shomoringabriel.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shomoringabriel.R;
import com.shomoringabriel.models.filterModel.colorModel.FilterColorModel;
import com.shomoringabriel.models.filterModel.countryModel.FilterCountryModel;
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

        return new FilterRecyclerVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterRecyclerVH holder, int position) {
        FilterUserModel filterUserModel = filterUserModels.get(position);

        holder.name.setText(filterUserModel.getFullName());

        holder.gender.setText(filterUserModel.getGender());

        itemGlider(holder.circleImageView, filterUserModel.getAvatar());

        stringColorBuilder(holder, filterUserModel);

        stringCountryBuilder(holder, filterUserModel);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To properties Fragment
            }
        });

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

    private void stringCountryBuilder(FilterRecyclerVH holder, FilterUserModel f){

        List<FilterCountryModel> list = f.getCountriesList(f.getId()).blockingGet();

        StringBuilder builder = new StringBuilder();
        for (FilterCountryModel fcM: list) {
            builder.append(fcM.getCountryName()).append(", ");
        }
        holder.country.setText(builder.toString());
        holder.country.setMarqueeRepeatLimit(-1);
        holder.country.setSelected(true);
        holder.country.setFocusableInTouchMode(true);

    }

    private void stringColorBuilder(FilterRecyclerVH holder, FilterUserModel f){

        List<FilterColorModel> list = f.getColorsList(f.getId()).blockingGet();

        StringBuilder builder = new StringBuilder();
        for (FilterColorModel fcM: list) {
            builder.append(fcM.getColorName()).append(", ");
        }
        holder.color.setText(builder.toString());
        holder.color.setMarqueeRepeatLimit(-1);
        holder.color.setSelected(true);
        holder.color.setFocusableInTouchMode(true);

    }

    @Override
    public int getItemCount() {
        return filterUserModels.size();
    }
}
