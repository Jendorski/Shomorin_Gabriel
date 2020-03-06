package com.shomoringabriel.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shomoringabriel.R;
import com.shomoringabriel.filterRepository.FilterRepository;

public class FilterFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = FilterFragment.class.getSimpleName();

    private com.shomoringabriel.databinding.PropertiesFilterLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.properties_filter_layout, container, false);

        binding.propertiesFilterSwipeRefreshLayout.setOnRefreshListener(this);
        binding.propertiesFilterSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        a();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRefresh() {
        a();
        binding.propertiesFilterSwipeRefreshLayout.setRefreshing(false);
    }

    private void a(){
        new FilterRepository()
                .getRecyclerView(binding.propertiesFilterRecyclerView)
                .getAVL(binding.propertiesFilterAVLoadingIndicatorView)
                .g();
    }

}
