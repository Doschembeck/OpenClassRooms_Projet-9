package com.openclassrooms.realestatemanager.ui.fragments.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentListviewItemBinding;
import com.openclassrooms.realestatemanager.model.Property;

import java.util.List;
import java.util.zip.Inflater;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyViewHolder> {

    // FOR DATA
    private List<Property> mListProperty;

    // CONSTRUCTOR
    public PropertyAdapter(List<Property> listProperty) {
        this.mListProperty = listProperty;
    }

    @Override
    public PropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        return new PropertyViewHolder(FragmentListviewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    // UPDATE VIEW HOLDER
    @Override
    public void onBindViewHolder(PropertyViewHolder viewHolder, int position) {
        viewHolder.updateWithProperty(this.mListProperty.get(position));
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.mListProperty.size();
    }
}