package com.openclassrooms.realestatemanager.ui.fragments.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentListviewItemBinding;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.List;
import java.util.zip.Inflater;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyViewHolder> {

    // FOR DATA
    private List<Property> mListProperty;
    private PropertyViewModel mViewModel;

    // CONSTRUCTOR
    public PropertyAdapter(List<Property> listProperty, PropertyViewModel viewModel) {
        this.mListProperty = listProperty;
        this.mViewModel = viewModel;
    }

    @Override
    public PropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        return new PropertyViewHolder(FragmentListviewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), mViewModel);
    }

    // UPDATE VIEW HOLDER
    @Override
    public void onBindViewHolder(PropertyViewHolder viewHolder, int position) {
        viewHolder.updateWithProperty(this.mListProperty.get(position));
    }

    public Property getProperty(int position){
        return this.mListProperty.get(position);
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.mListProperty.size();
    }
}