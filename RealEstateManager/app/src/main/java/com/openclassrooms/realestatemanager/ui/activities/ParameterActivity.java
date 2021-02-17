package com.openclassrooms.realestatemanager.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.databinding.ActivityParameterBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.NearbyPOI;
import com.openclassrooms.realestatemanager.model.Parameter;
import com.openclassrooms.realestatemanager.utils.ActivityUtils;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.FormatUtils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ParameterActivity extends AppCompatActivity {

    private ActivityParameterBinding mBinding;
    private PropertyViewModel mViewModel;
    private List<NearbyPOI> selectedNearbyPOI = new ArrayList<>();
    private Geocoder mGeocoder;
    private Address mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        mBinding = ActivityParameterBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // --- INIT ---
        mGeocoder = ActivityUtils.setupAutoComplete(this);

        Parameter parameter = getIntent().getParcelableExtra("parameter");

        if (parameter != null){
            updateUI(parameter);
        }

        // LISTENERS
        mBinding.activityParameterButtonFilter.setOnClickListener(this::onClickFilterProperty);
        mBinding.activityParameterButtonAddress.setOnClickListener(this::startAutoComplete);
        mBinding.activityParameterImageviewCreatedatmin.setOnClickListener(v -> ActivityUtils.createDatePickerDialog(this, mBinding.activityParameterEdittextCreatedatmin));
        mBinding.activityParameterImageviewCreatedatmax.setOnClickListener(v -> ActivityUtils.createDatePickerDialog(this, mBinding.activityParameterEdittextCreatedatmax));
        mBinding.activityParameterImageviewDateofsalemin.setOnClickListener(v -> ActivityUtils.createDatePickerDialog(this, mBinding.activityParameterEdittextDateofsalemin));
        mBinding.activityParameterImageviewDateofsalemax.setOnClickListener(v -> ActivityUtils.createDatePickerDialog(this, mBinding.activityParameterEdittextDateofsalemax));

        mViewModel.getAllNearbyPOI().observe(this, this::updateUIWithAllNearbyPoi);
    }

    private void startAutoComplete(View view) {

        startActivityForResult(
                new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN,
                        Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
                        .build(this),
                Constants.AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.AUTOCOMPLETE_REQUEST_CODE){
            if (resultCode == AutocompleteActivity.RESULT_OK){
                LatLng latLng = Autocomplete.getPlaceFromIntent(data).getLatLng();;

                getAddress(latLng.latitude, latLng.longitude);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED){
            }
        }

    }

    private void getAddress(Double latitude, Double longitude) {

        try {
            if (mGeocoder != null){
                List<android.location.Address> addresses = mGeocoder.getFromLocation(latitude, longitude, 1);

                if (addresses != null && addresses.size() > 0) {
                    android.location.Address address = addresses.get(0);
                    mAddress = new Address(0, address.getFeatureName(), address.getThoroughfare(), address.getLocality(), address.getPostalCode(), address.getCountryName(), address.getLatitude(), address.getLongitude());
                    mBinding.activityParameterEdittextAddress.setText(mAddress.getCompleteAddress());
                }
            } else {
                Toast.makeText(this, "Erreur veuillez choisir une adresse valide", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUIWithAllNearbyPoi(List<NearbyPOI> nearbyPOIList){

        mBinding.activityParameterLinearlayoutNearbypoi.removeAllViewsInLayout();
        for (NearbyPOI nearbyPOI : nearbyPOIList){
            CheckBox checkBox = new CheckBox(this);

            // Si le nearby est selectionné alors on le check
            for (int i = 0; i < selectedNearbyPOI.size(); i++){
                if (selectedNearbyPOI.get(i).getId() == nearbyPOI.getId()){
                    checkBox.setChecked(true);
                }
            }

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked){
                    selectedNearbyPOI.add(nearbyPOI);
                }else {
                    for (int i = 0; i < selectedNearbyPOI.size(); i++){
                        if (selectedNearbyPOI.get(i).getId() == nearbyPOI.getId()){
                            selectedNearbyPOI.remove(i);
                        }
                    }
                }
            });
            checkBox.setText(nearbyPOI.getName());
            mBinding.activityParameterLinearlayoutNearbypoi.addView(checkBox);
        }
    }

    private void updateUI(Parameter parameter) {

        mBinding.activityParameterEdittextPricemin.setText(String.valueOf(parameter.getPriceMin()));
        mBinding.activityParameterEdittextPricemax.setText(String.valueOf(parameter.getPriceMax()));
        mBinding.activityParameterEdittextAreamin.setText(String.valueOf(parameter.getAreaMin()));
        mBinding.activityParameterEdittextAreamax.setText(String.valueOf(parameter.getAreaMax()));
        mBinding.activityParameterEdittextNbOfRoomsmin.setText(String.valueOf(parameter.getNbOfRoomsMin()));
        mBinding.activityParameterEdittextNbOfRoomsmax.setText(String.valueOf(parameter.getNbOfRoomsMax()));
        mBinding.activityParameterEdittextNbOfBedRoomsmin.setText(String.valueOf(parameter.getNbOfBedRoomsMin()));
        mBinding.activityParameterEdittextNbOfBedRoomsmax.setText(String.valueOf(parameter.getNbOfBedRoomsMax()));
        mBinding.activityParameterEdittextRealEstateAgent.setText(parameter.getRealEstateAgent());

        switch (parameter.getSold()){
            case 0:
                mBinding.activityParameterRadiobuttonAvailable.setChecked(true);
                break;

            case 1:
                mBinding.activityParameterRadiobuttonSold.setChecked(true);
                break;

            case 2:
                mBinding.activityParameterRadiobuttonAll.setChecked(true);
                break;
        }

    }

    private void configureViewModel(){
        this.mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this)).get(PropertyViewModel.class);
    }

    private void onClickFilterProperty(View view){

        Parameter parameter = new Parameter();

        parameter.setPriceMin(!mBinding.activityParameterEdittextPricemin.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextPricemin.getText().toString()) : 0);
        parameter.setPriceMax(!mBinding.activityParameterEdittextPricemax.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextPricemax.getText().toString()) : 0);
        parameter.setAreaMin(!mBinding.activityParameterEdittextAreamin.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextAreamin.getText().toString()) : 0);
        parameter.setAreaMax(!mBinding.activityParameterEdittextAreamax.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextAreamax.getText().toString()) : 0);
        parameter.setNbOfRoomsMin(!mBinding.activityParameterEdittextNbOfRoomsmin.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextNbOfRoomsmin.getText().toString()) : 0);
        parameter.setNbOfRoomsMax(!mBinding.activityParameterEdittextNbOfRoomsmax.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextNbOfRoomsmax.getText().toString()) : 0);
        parameter.setNbOfBedRoomsMin(!mBinding.activityParameterEdittextNbOfBedRoomsmin.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextNbOfBedRoomsmin.getText().toString()) : 0);
        parameter.setNbOfBedRoomsMax(!mBinding.activityParameterEdittextNbOfBedRoomsmax.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextNbOfBedRoomsmax.getText().toString()) : 0);
        parameter.setRealEstateAgent(!mBinding.activityParameterEdittextRealEstateAgent.getText().toString().equals("") ? mBinding.activityParameterEdittextRealEstateAgent.getText().toString() : null);

        if(mBinding.activityParameterRadiobuttonAvailable.isChecked()){
            parameter.setSold((byte) 0);
        }else if(mBinding.activityParameterRadiobuttonSold.isChecked()){
            parameter.setSold((byte) 1);
        } else if(mBinding.activityParameterRadiobuttonAll.isChecked()){
            parameter.setSold((byte) 2);
        }

        setResult(Activity.RESULT_OK, new Intent().putExtra("result", parameter));

        finish();
    }
}