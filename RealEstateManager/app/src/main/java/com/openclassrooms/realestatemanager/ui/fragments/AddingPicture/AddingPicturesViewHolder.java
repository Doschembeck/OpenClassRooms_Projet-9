package com.openclassrooms.realestatemanager.ui.fragments.AddingPicture;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentAddingpicturesItemBinding;
import com.openclassrooms.realestatemanager.model.Photo;

import java.lang.ref.WeakReference;

public class AddingPicturesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private FragmentAddingpicturesItemBinding mItemBinding;
    private Context mContext;

    // Declare a Weak Reference to our Callback
    private WeakReference<AddingPicturesAdapter.Listener> callbackWeakRef;


    public AddingPicturesViewHolder(FragmentAddingpicturesItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.mItemBinding = itemBinding;
        mContext = itemBinding.getRoot().getContext();
    }

    public void updateWithPropertyPicture(Photo propertyPicture, AddingPicturesAdapter.Listener callback){

        //3 - Implement Listener on ImageButton
        mItemBinding.fragmentAddingpicturesItemButtonDelete.setOnClickListener(this);
        //4 - Create a new weak Reference to our Listener
        this.callbackWeakRef = new WeakReference<>(callback);

        mItemBinding.fragmentAddingpicturesItemTextviewDescription.setText(propertyPicture.getPhotoDescription());

        Glide.with(mContext)
                .load(propertyPicture.getUrlPicture())
                .error(R.drawable.image_not_found_scaled)
                .centerCrop()
                .into(mItemBinding.fragmentAddingpicturesItemImageviewPicture);

    }

    @Override
    public void onClick(View view) {
        // 5 - When a click happens, we fire our listener.
        AddingPicturesAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickDeleteButton(getAdapterPosition());
    }
}