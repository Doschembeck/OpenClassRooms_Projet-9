package com.openclassrooms.realestatemanager.ui.fragments.ListView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentListviewItemBinding;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.ui.activities.DetailsActivity;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.Utils;

import static android.content.Context.MODE_PRIVATE;

public class PropertyViewHolder extends RecyclerView.ViewHolder {

    private FragmentListviewItemBinding itemBinding;
    SharedPreferences mSharedPreferences;
    private Context mContext;

    private String devise; //todo: voir si il ce met a jour quand je modifie la devise

    public PropertyViewHolder(FragmentListviewItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
        this.mContext = itemBinding.getRoot().getContext();

        mSharedPreferences = mContext.getSharedPreferences(Constants.PREF_SHARED_KEY, MODE_PRIVATE);
        devise = mSharedPreferences.getString(Constants.PREF_CURRENCY_KEY, "ERROR_CURRENCY");

        itemBinding.fragmentListviewItemCardview.setOnClickListener(v -> onClickCardView());

    }

    private void onClickCardView(){
        mContext.startActivity(new Intent(mContext, DetailsActivity.class));
    }

    public void updateWithProperty(Property property){

        if (property.isSold()){
            itemBinding.fragmentListviewItemImageviewSold.setVisibility(View.VISIBLE);
            itemBinding.fragmentListviewItemTextviewSold.setVisibility(View.VISIBLE);
        }

        //todo: erreur ne charge pas l'image en ligne "property.getPhotoIdList().get(0).getPhoto()"
        Glide.with(mContext)
                .load(R.drawable.test_property)
                .centerCrop()
                .into(itemBinding.fragmentListviewItemImageviewPhoto);

//        mTextViewCity.setText(property.getAddressId().getCity());
        itemBinding.fragmentListviewItemTextviewPrice.setText(Utils.formatEditTextWithDevise(property.getPrice(), devise));
        itemBinding.fragmentListviewItemTextviewRooms.setText(property.getNbOfRooms() + " Pièces");
        itemBinding.fragmentListviewItemTextviewBedrooms.setText(property.getNbOfBedRooms() + " Chambres");
        itemBinding.fragmentListviewItemTextviewArea.setText(property.getArea() + "m²");

    }
}