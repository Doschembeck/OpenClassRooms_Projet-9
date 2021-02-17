package com.openclassrooms.realestatemanager.ui.fragments.AddingPicture;

import android.view.LayoutInflater;
import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.databinding.FragmentAddingpicturesItemBinding;
import com.openclassrooms.realestatemanager.model.Photo;

import java.util.List;

public class AddingPicturesAdapter extends RecyclerView.Adapter<AddingPicturesViewHolder> {

    // Create interface for callback
    public interface Listener {
        void onClickDeleteButton(int position);
    }

    // Declaring callback
    private final Listener callback;

    // FOR DATA
    private List<Photo> propertyPictureList;

    // CONSTRUCTOR
    public AddingPicturesAdapter(Listener callback, List<Photo> propertyPictureList) {
        this.callback = callback;
        this.propertyPictureList = propertyPictureList;
    }

    @Override
    public AddingPicturesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
         return new AddingPicturesViewHolder(FragmentAddingpicturesItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    // UPDATE VIEW HOLDER
    @Override
    public void onBindViewHolder(AddingPicturesViewHolder viewHolder, int position) {
        viewHolder.updateWithPropertyPicture(this.propertyPictureList.get(position), this.callback);
    }

    public Photo getPicture(int position){
        return this.propertyPictureList.get(position);
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.propertyPictureList.size();
    }
}