package com.openclassrooms.realestatemanager.ui.fragments.ListView;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentListviewBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Parameter;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.ui.activities.DetailsActivity;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;
import com.r2te.codehelper.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class ListViewFragment extends Fragment {

    private FragmentListviewBinding binding;
    SharedPreferences mSharedPreferences;
    private Context mContext;
    private PropertyViewModel mViewModel;

    private String actualDevise;
    private List<Property> mListProperty;
    private PropertyAdapter mAdapter;

    public ListViewFragment() { }

    public static ListViewFragment newInstance() {
        return (new ListViewFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mContext = view.getContext();
        configureViewModel();

        // 2 - Calling the method that configuring click on RecyclerView
        this.configureOnClickRecyclerView();

        mSharedPreferences = mContext.getSharedPreferences(Constants.PREF_SHARED_KEY, MODE_PRIVATE);
        actualDevise = mSharedPreferences.getString(Constants.PREF_CURRENCY_KEY, "ERROR_CURRENCY");

        mViewModel.mListPropertyMutableLiveData.observe(getViewLifecycleOwner(), this::updateUI);

        this.configureRecyclerView();

        return view;
    }

    // 1 - Configure item click on RecyclerView
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(binding.fragmentListviewRecyclerView, R.layout.fragment_listview_item).setOnItemClickListener((recyclerView, position, v) -> {
                    Property property = mAdapter.getProperty(position);

                    getActivity().startActivityForResult(new Intent(mContext, DetailsActivity.class)
                            .putExtra("property_id", property.getId()),
                            Constants.LAUNCH_DETAILS_ACTIVITY);
                });
    }

    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(mContext);
        this.mViewModel = ViewModelProviders.of(requireActivity(), mViewModelFactory).get(PropertyViewModel.class);
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    // 3 - Configure RecyclerView, Adapter, LayoutManager & glue it together
    private void configureRecyclerView(){
        // 3.1 - Reset list
        this.mListProperty = new ArrayList<>();

        // 3.2 - Create mAdapter passing the list of users
        this.mAdapter = new PropertyAdapter(this.mListProperty);
        // 3.3 - Attach the mAdapter to the recyclerview to populate items
        binding.fragmentListviewRecyclerView.setAdapter(this.mAdapter);
        // 3.4 - Set layout manager to position the items
        binding.fragmentListviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void updateUI(List<Property> properties){

        mListProperty.clear();
        mListProperty.addAll(properties);
        mAdapter.notifyDataSetChanged();
    }

}

