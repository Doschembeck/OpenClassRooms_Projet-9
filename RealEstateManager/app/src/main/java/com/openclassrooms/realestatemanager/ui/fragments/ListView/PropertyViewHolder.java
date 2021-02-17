package com.openclassrooms.realestatemanager.ui.fragments.ListView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentListviewItemBinding;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.FormatUtils;
import com.openclassrooms.realestatemanager.utils.ScriptsStats;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class PropertyViewHolder extends RecyclerView.ViewHolder {

    private FragmentListviewItemBinding itemBinding;
    SharedPreferences mSharedPreferences;
    private Context mContext;

    private String devise;

    public PropertyViewHolder(FragmentListviewItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
        this.mContext = itemBinding.getRoot().getContext();

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
                    .error(R.drawable.image_not_found_scaled) //todo: tombe en error lors de l'affichage d'un url local
                    .centerCrop()
                    .into(itemBinding.fragmentListviewItemImageviewPhoto);
        }else {
            Glide.with(mContext)
                    .load(R.drawable.image_not_found)
                    .centerCrop()
                    .into(itemBinding.fragmentListviewItemImageviewPhoto);
        }
    }

    private void updateRate(float rate){
        // rate < -15% = tres bonne affaire
        // rate < -5% = bonne affaire
        // rate < +5% = Affaire equitable
        // rate < +15% = Au dessus du marché
        // rate > +15% = Trop chere

        if (rate <= 0.85){
            itemBinding.fragmentListviewItemProgressbarRate.setProgress(100);
            itemBinding.fragmentListviewItemProgressbarRate.getProgressDrawable().setColorFilter( Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
            itemBinding.fragmentListviewItemTextviewRate.setText("Trés bonne affaire");
       }else if (rate <= 0.95){
            itemBinding.fragmentListviewItemProgressbarRate.setProgress(75);
            itemBinding.fragmentListviewItemProgressbarRate.getProgressDrawable().setColorFilter( Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
            itemBinding.fragmentListviewItemTextviewRate.setText("Bonne affaire");
       }else if (rate <= 1.05){
            itemBinding.fragmentListviewItemProgressbarRate.setProgress(50);
            itemBinding.fragmentListviewItemProgressbarRate.getProgressDrawable().setColorFilter( Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
            itemBinding.fragmentListviewItemTextviewRate.setText("Offre equitable");
       }else if (rate <= 1.15){
            itemBinding.fragmentListviewItemProgressbarRate.setProgress(25);
            itemBinding.fragmentListviewItemProgressbarRate.getProgressDrawable().setColorFilter( Color.parseColor("#F4511E"), android.graphics.PorterDuff.Mode.SRC_IN);
           itemBinding.fragmentListviewItemTextviewRate.setText("Au dessus du marché");
       }else if (rate > 1.25){
            itemBinding.fragmentListviewItemProgressbarRate.setProgress(5);
            itemBinding.fragmentListviewItemProgressbarRate.getProgressDrawable().setColorFilter( Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            itemBinding.fragmentListviewItemTextviewRate.setText("Loin au dessus du marché");
       }

    }

    public void updateWithProperty(Property property){

        checkIsFavorite(property.getId());
        checkIsSold(property);
        displayMainPicture(property.getMainPictureUrl());

        updateRate(property.getRate());

        itemBinding.fragmentListviewItemTextviewCity.setText(property.getCity());
        itemBinding.fragmentListviewItemTextviewPrice.setText(FormatUtils.formatEditTextWithDevise(property.getPrice(), devise));
        itemBinding.fragmentListviewItemTextviewRooms.setText(property.getNbOfRooms() + " Pièces");
        itemBinding.fragmentListviewItemTextviewBedrooms.setText(property.getNbOfBedRooms() + " Chambres");
        itemBinding.fragmentListviewItemTextviewArea.setText(property.getArea() + "m²");

    }
}