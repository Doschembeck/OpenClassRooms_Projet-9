package com.openclassrooms.realestatemanager.ui.fragments.ListView;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.openclassrooms.realestatemanager.databinding.FragmentListviewBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment {

    private FragmentListviewBinding binding;

    private List<Property> mListProperty;
    private PropertyAdapter mAdapter;
    private PropertyViewModel mViewModel;
    private Context mContext;

    public ListViewFragment() { }

    public static ListViewFragment newInstance() {
        return (new ListViewFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        configureViewModel();

        binding = FragmentListviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        mContext = view.getContext();

        this.configureRecyclerView();
        getAllProperty();

        return view;
    }

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(mContext);
        this.mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
    }

    private void getAllProperty(){
        this.mViewModel.getAllProperty().observe(getViewLifecycleOwner(), this::updateUI);
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    // 3 - Configure RecyclerView, Adapter, LayoutManager & glue it together
    private void configureRecyclerView(){
        // 3.1 - Reset list
        this.mListProperty = new ArrayList<Property>();

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

    private void updateUI(List<Property> users){
        mListProperty.clear();
        mListProperty.addAll(users);
        mAdapter.notifyDataSetChanged();
    }
}

