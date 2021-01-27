package com.openclassrooms.realestatemanager.ui.fragments.ListView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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

import static android.content.Context.MODE_PRIVATE;

public class PropertyViewHolder extends RecyclerView.ViewHolder {

    private FragmentListviewItemBinding itemBinding;
    long propertyId;
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
        mContext.startActivity(new Intent(mContext, DetailsActivity.class).putExtra("property_id", propertyId));
    }

    public void updateWithAddress(Address address){
        itemBinding.fragmentListviewItemTextviewCity.setText(address.getCity());
    }

    private void updateWithPhoto(List<Photo> photoList){

        if (photoList == null) return;

        Photo photo = photoList.get(0);

        if (photo.getPhoto() != null){
            Glide.with(mContext)
                    .load(photo.getPhoto())
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

        //todo: Recuperer l'address et la passer en parametre de "updateWithAddress()"
//        mViewModel.getAddress(property.getAddressId()).observe(this, this::updateWithAddress);
// recyclerview callBack

        propertyId = property.getId();

        if (property.isSold()){
            itemBinding.fragmentListviewItemImageviewSold.setVisibility(View.VISIBLE);
            itemBinding.fragmentListviewItemTextviewSold.setVisibility(View.VISIBLE);
        } else {
            itemBinding.fragmentListviewItemImageviewSold.setVisibility(View.GONE);
            itemBinding.fragmentListviewItemTextviewSold.setVisibility(View.GONE);
        }

        //todo: Recuperer la liste de photos et la passer en parametre de "updateWithPhoto()"
//        mViewModel.getAllPropertyPhoto(property.getId()).observe(this, this::updateWithPhoto);

//        mTextViewCity.setText(property.getAddressId().getCity());
        itemBinding.fragmentListviewItemTextviewPrice.setText(Utils.formatEditTextWithDevise(property.getPrice(), devise));
        itemBinding.fragmentListviewItemTextviewRooms.setText(property.getNbOfRooms() + " Pièces");
        itemBinding.fragmentListviewItemTextviewBedrooms.setText(property.getNbOfBedRooms() + " Chambres");
        itemBinding.fragmentListviewItemTextviewArea.setText(property.getArea() + "m²");

        //todo: a supprimer
        itemBinding.fragmentListviewItemTextviewCity.setText("%VILLE_NAME%");

    }
}