package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.realestatemanager.databinding.ActivityDetailsBinding;
import com.openclassrooms.realestatemanager.databinding.ContentDetailsBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.text.SimpleDateFormat;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;
    private ContentDetailsBinding includeBinding;
    private PropertyViewModel mViewModel;
    SharedPreferences mSharedPreferences;
    private String devise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        includeBinding = binding.activityDetailsIncludeContentDetails;
        setContentView(binding.getRoot());

        mSharedPreferences = getSharedPreferences(Constants.PREF_SHARED_KEY, MODE_PRIVATE);
        devise = mSharedPreferences.getString(Constants.PREF_CURRENCY_KEY, "ERROR_CURRENCY");

        long propertyId = getIntent().getLongExtra("property_id", 0);

        mViewModel.getProperty(propertyId).observe(this, this::updateUIWithProperty);

        binding.activityDetailsFabFavorite.setOnClickListener(this::onClickFloatingActionButton);

        setSupportActionBar(binding.toolbar);
//        binding.activityDetailsToolbarLayout.setTitle("NOM");

    }

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
    }

    private void updateUIWithAddress(Address address){
        includeBinding.contentDetailTextviewAddress.setText(String.format("%d %s %s %s %s",
                address.getStreetNumber(),
                address.getStreetName(),
                address.getZipCode(),
                address.getCity(),
                address.getCountry()));
    }

    private void updateUIWithProperty(Property property){

        mViewModel.getAddress(property.getAddressId()).observe(this, this::updateUIWithAddress);

        Glide.with(this)
                .load(property.getPhotoUrlList())
                .centerCrop()
                .into(binding.activityDetailsPhotolist);

        includeBinding.contentDetailTextviewDateofentry.setText(new SimpleDateFormat("dd/MM/yyyy à hh:mm").format(property.getCreatedAt()));
        includeBinding.contentDetailTextviewTypeofproperty.setText(Constants.ListPropertyType[property.getPropertyTypeId()]);
        includeBinding.contentDetailTextviewNbofrooms.setText("" + property.getNbOfRooms());
        includeBinding.contentDetailTextviewNbofbedrooms.setText("" + property.getNbOfBedRooms());
        includeBinding.contentDetailTextviewArea.setText(property.getArea() + " m²");
        includeBinding.contentDetailTextviewPrice.setText(Utils.formatEditTextWithDevise(property.getPrice(), devise));
        includeBinding.contentDetailTextviewPricepersquaremeter.setText(Utils.formatEditTextWithDevise(property.getPrice() / property.getArea(), devise) + "/m²");
        includeBinding.contentDetailTextviewDescription.setText(property.getDescription());
        includeBinding.contentDetailTextviewRealestateagent.setText(property.getRealEstateAgent());


        if (property.getNearbyPOI() != null){
            includeBinding.activityEditPropertyLinearlayoutNearbypoi.setVisibility(View.VISIBLE);
            includeBinding.contentDetailTextviewNearbypoi.setText(property.getNearbyPOI());
        } else {
            includeBinding.activityEditPropertyLinearlayoutNearbypoi.setVisibility(View.GONE);
        }

        if (property.isSold()){
            includeBinding.contentDetailLinearlayoutDateofsale.setVisibility(View.VISIBLE);
            includeBinding.contentDetailTextviewDateofsale.setText(new SimpleDateFormat("dd/MM/yyyy à hh:mm").format(property.getDateOfSale()));
        }else {
            includeBinding.contentDetailLinearlayoutDateofsale.setVisibility(View.GONE);
        }
    }

    private void onClickFloatingActionButton(View view){
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}