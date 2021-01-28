package com.openclassrooms.realestatemanager.ui.fragments.ListView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentListviewItemBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.ui.activities.DetailsActivity;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class PropertyViewHolder extends RecyclerView.ViewHolder {

    private FragmentListviewItemBinding itemBinding;
    SharedPreferences mSharedPreferences;
    private Context mContext;
    private PropertyViewModel mViewModel;

    private String devise; //todo: voir si il ce met a jour quand je modifie la devise

    public PropertyViewHolder(FragmentListviewItemBinding itemBinding, PropertyViewModel viewModel) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
        this.mContext = itemBinding.getRoot().getContext();
        this.mViewModel = viewModel;

        mSharedPreferences = mContext.getSharedPreferences(Constants.PREF_SHARED_KEY, MODE_PRIVATE);
        devise = mSharedPreferences.getString(Constants.PREF_CURRENCY_KEY, "ERROR_CURRENCY");

    }

    private void checkIsFavorite(long propertyId){

        Set<String> listFavorites = mSharedPreferences.getStringSet(Constants.PREF_FAVORITES_PROPERTIES_KEY, null);

        if (listFavorites != null && listFavorites.contains(String.valueOf(propertyId))){
            itemBinding.fragmentListviewItemImageviewStarfavorite.setVisibility(View.VISIBLE);
            itemBinding.fragmentListviewItemImageviewStarfavorite.setColorFilter(mContext.getResources().getColor(R.color.starFavorite), PorterDuff.Mode.SRC_ATOP);
        } else {
            itemBinding.fragmentListviewItemImageviewStarfavorite.setVisibility(View.GONE);
        }

    }

    private void checkIsSold(Property property){

        if (property.isSold()){
            itemBinding.fragmentListviewItemImageviewSold.setVisibility(View.VISIBLE);
            itemBinding.fragmentListviewItemTextviewSold.setVisibility(View.VISIBLE);
        } else {
            itemBinding.fragmentListviewItemImageviewSold.setVisibility(View.GONE);
            itemBinding.fragmentListviewItemTextviewSold.setVisibility(View.GONE);
        }
    }

    private void displayMainPicture(String mainPictureUrl){
        if (mainPictureUrl != null){
            Glide.with(mContext)
                    .load(mainPictureUrl)
                    .error(R.drawable.image_not_found_scaled)
                    .centerCrop()
                    .into(itemBinding.fragmentListviewItemImageviewPhoto);
        }else {
            Glide.with(mContext)
                    .load(R.drawable.image_not_found)
                    .centerCrop()
                    .into(itemBinding.fragmentListviewItemImageviewPhoto);
        }
    }

    public void updateWithProperty(Property property){

        checkIsFavorite(property.getId());
        checkIsSold(property);
        displayMainPicture(property.getMainPictureUrl());

        itemBinding.fragmentListviewItemTextviewCity.setText(property.getCity());
        itemBinding.fragmentListviewItemTextviewPrice.setText(Utils.formatEditTextWithDevise(property.getPrice(), devise));
        itemBinding.fragmentListviewItemTextviewRooms.setText(property.getNbOfRooms() + " Pièces");
        itemBinding.fragmentListviewItemTextviewBedrooms.setText(property.getNbOfBedRooms() + " Chambres");
        itemBinding.fragmentListviewItemTextviewArea.setText(property.getArea() + "m²");

    }
}