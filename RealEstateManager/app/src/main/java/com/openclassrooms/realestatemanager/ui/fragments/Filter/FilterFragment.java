package com.openclassrooms.realestatemanager.ui.fragments.Filter;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterFragment extends Fragment {

    // FOR DESIGN
    @BindView(R.id.fragment_filter_recycler_view) RecyclerView mRecyclerView;

    //FOR DATA
    private List<String> mListFilterProperty;
    private FilterPropertyAdapter mAdapter;
    private PropertyViewModel mViewModel;

    public FilterFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(requireActivity()).get(PropertyViewModel.class);
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this, view);

        this.configureRecyclerView();

        mViewModel.mListFilterPropertyMutableLiveData.observe(getViewLifecycleOwner(),mListFilterProperty -> {
           updateUI(mListFilterProperty);
        } );

        return view;
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    // 3 - Configure RecyclerView, Adapter, LayoutManager & glue it together
    private void configureRecyclerView(){
        // 3.1 - Reset list
        this.mListFilterProperty = new ArrayList<>();

        // 3.2 - Create mAdapter passing the list of users
        this.mAdapter = new FilterPropertyAdapter(this.mListFilterProperty);
        // 3.3 - Attach the mAdapter to the recyclerview to populate items
        this.mRecyclerView.setAdapter(this.mAdapter);
        // 3.4 - Set layout manager to position the items
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void updateUI(List<String> users){
        mListFilterProperty.addAll(users);
        mAdapter.notifyDataSetChanged();
    }
}

