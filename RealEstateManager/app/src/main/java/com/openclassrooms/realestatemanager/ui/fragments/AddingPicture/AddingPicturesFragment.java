package com.openclassrooms.realestatemanager.ui.fragments.AddingPicture;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentAddingpicturesBinding;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;
import com.r2te.codehelper.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

public class AddingPicturesFragment extends Fragment implements AddingPicturesAdapter.Listener {

    private FragmentAddingpicturesBinding binding;

    //FOR DATA
    private List<Photo> propertyPictureList;
    private AddingPicturesAdapter mAdapter;
    private PropertyViewModel mViewModel;

    public AddingPicturesFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(requireActivity()).get(PropertyViewModel.class);
        
        binding = FragmentAddingpicturesBinding.inflate(inflater,container,false);
        View view  = binding.getRoot();

        this.configureRecyclerView();

        mViewModel.propertyPictureListMutableLiveData.observe(getViewLifecycleOwner(), this::updateUI);

        return view;
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    // 3 - Configure RecyclerView, Adapter, LayoutManager & glue it together
    private void configureRecyclerView(){

        configureOnClickRecyclerView();

        // 3.1 - Reset list
        this.propertyPictureList = new ArrayList<>();

        // 3.2 - Create mAdapter passing the list of %ItemNameLower%
        this.mAdapter = new AddingPicturesAdapter(this, this.propertyPictureList);
        // 3.3 - Attach the mAdapter to the recyclerview to populate items
        binding.fragmentAddingpicturesRecyclerView.setAdapter(this.mAdapter);
        // 3.4 - Set layout manager to position the items
        binding.fragmentAddingpicturesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false));
    }

    // -----------------
    // ACTION
    // -----------------

    // 1 - Configure item click on RecyclerView
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(binding.fragmentAddingpicturesRecyclerView, R.layout.fragment_addingpictures_item)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    //todo
                    String picture = mAdapter.getPicture(position).getUrlPicture();
                    Toast.makeText(getContext(), "You clicked on Picture : "+ picture, Toast.LENGTH_SHORT).show();

                });
    }

    @Override
    public void onClickDeleteButton(int position) {
        List<Photo> pictureList = mViewModel.propertyPictureListMutableLiveData.getValue();
        pictureList.remove(position);
        mViewModel.propertyPictureListMutableLiveData.setValue(pictureList);
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void updateUI(List<Photo> propertyPictureList){
        this.propertyPictureList.clear();
        this.propertyPictureList.addAll(propertyPictureList);
        mAdapter.notifyDataSetChanged();
    }
}

