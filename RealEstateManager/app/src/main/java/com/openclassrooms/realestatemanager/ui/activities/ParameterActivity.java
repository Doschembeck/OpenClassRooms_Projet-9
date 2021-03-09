package com.openclassrooms.realestatemanager.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
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
        mBinding.activityParameterImageviewAddress.setOnClickListener(this::startAutoComplete);
        mBinding.activityParameterImageviewAddressdelete.setOnClickListener(view -> {
            mBinding.activityParameterEdittextAddress.setText("");
            mAddress = null;
        });
        mBinding.activityParameterImageviewCreatedatmin.setOnClickListener(v -> ActivityUtils.createDatePickerDialog(this, mBinding.activityParameterEdittextCreatedatmin));
        mBinding.activityParameterImageviewCreatedatmax.setOnClickListener(v -> ActivityUtils.createDatePickerDialog(this, mBinding.activityParameterEdittextCreatedatmax));
        mBinding.activityParameterImageviewDateofsalemin.setOnClickListener(v -> ActivityUtils.createDatePickerDialog(this, mBinding.activityParameterEdittextDateofsalemin));
        mBinding.activityParameterImageviewDateofsalemax.setOnClickListener(v -> ActivityUtils.createDatePickerDialog(this, mBinding.activityParameterEdittextDateofsalemax));
        mBinding.activityParameterButtonRemovedate.setOnClickListener(view -> {
            mBinding.activityParameterEdittextCreatedatmin.setText("");
            mBinding.activityParameterEdittextCreatedatmax.setText("");
        });
        mBinding.activityParameterRadiobuttonSold.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                mBinding.activityParameterLinearlayoutDateofsale.setVisibility(View.VISIBLE);
            } else {
                mBinding.activityParameterLinearlayoutDateofsale.setVisibility(View.GONE);
            }
        });

        mBinding.activityParameterSeekbarNbOfRoomsMin.setOnSeekBarChangeListener(onSeekBarChangeNbOfRoomsMin());
        mBinding.activityParameterSeekbarNbOfRoomsMax.setOnSeekBarChangeListener(onSeekBarChangeNbOfRoomsMax());
        mBinding.activityParameterSeekbarNbOfBedRoomsMin.setOnSeekBarChangeListener(onSeekBarChangeNbOfBedRoomsMin());
        mBinding.activityParameterSeekbarNbOfBedRoomsMax.setOnSeekBarChangeListener(onSeekBarChangeNbOfBedRoomsMax());
        mBinding.activityParameterSeekbarAreaMin.setOnSeekBarChangeListener(onSeekBarChangeAreaMin());
        mBinding.activityParameterSeekbarAreaMax.setOnSeekBarChangeListener(onSeekBarChangeAreaMax());
        mBinding.activityParameterSeekbarNbofPicturesMin.setOnSeekBarChangeListener(onSeekBarChangeNbOfPicturesMin());
        mBinding.activityParameterSeekbarNbofPicturesMax.setOnSeekBarChangeListener(onSeekBarChangeNbOfPicturesMax());
        mBinding.activityParameterSeekbarPriceMin.setOnSeekBarChangeListener(onSeekBarChangePriceMin());
        mBinding.activityParameterSeekbarPriceMax.setOnSeekBarChangeListener(onSeekBarChangePriceMax());
        mBinding.activityParameterSeekbarDistanceaddressMin.setOnSeekBarChangeListener(onSeekBarChangeDistanceMin());
        mBinding.activityParameterSeekbarDistanceaddressMax.setOnSeekBarChangeListener(onSeekBarChangeDistanceMax());

        mViewModel.getAllNearbyPOI().observe(this, this::updateUIWithAllNearbyPoi);
    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeDistanceMin(){
        return onSeekBarChangeMin(mBinding.activityParameterSeekbarDistanceaddressMax, mBinding.activityParameterTextviewDistanceaddressMin, mBinding.activityParameterTextviewDistanceaddressMax);
    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeDistanceMax(){
        return onSeekBarChangeMax(mBinding.activityParameterSeekbarDistanceaddressMin, mBinding.activityParameterTextviewDistanceaddressMin, mBinding.activityParameterTextviewDistanceaddressMax);
    }

    private void startAutoComplete(View view) {

        startActivityForResult(
                new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN,
                        Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
                        .build(this),
                Constants.AUTOCOMPLETE_REQUEST_CODE);
    }

    private String formatNumberSeek(int number){
        if (number >= 1000000){
            return "" + number / 1000000 + "M";
        }else if (number >= 1000){
            return "" + number / 1000 + "K";
        } else {
            return "" + number;
        }
    }

    private void updateTextview(SeekBar seekBarMin, SeekBar seekBarMax, TextView textViewMin, TextView textViewMax){
        String max = "";
        if (seekBarMax.getProgress() >= seekBarMax.getMax()){
            max += "+";
        }

        textViewMax.setText(max + formatNumberSeek(seekBarMax.getProgress()));
        textViewMin.setText(formatNumberSeek(seekBarMin.getProgress()));
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeMin(SeekBar seekBarMax, TextView textViewMin, TextView textViewMax){
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(progress > seekBarMax.getProgress()){
                    seekBarMax.setProgress(progress);
                }

                updateTextview(seekBar, seekBarMax, textViewMin, textViewMax);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeMax(SeekBar seekBarMin, TextView textViewMin, TextView textViewMax){
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(progress < seekBarMin.getProgress()){
                    seekBarMin.setProgress(progress);
                }

                updateTextview(seekBarMin , seekBar, textViewMin, textViewMax);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangePriceMin(){
        return onSeekBarChangeMin(mBinding.activityParameterSeekbarPriceMax, mBinding.activityParameterTextviewPricemin, mBinding.activityParameterTextviewPricemax);
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangePriceMax(){
        return onSeekBarChangeMax(mBinding.activityParameterSeekbarPriceMin, mBinding.activityParameterTextviewPricemin, mBinding.activityParameterTextviewPricemax);
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeNbOfRoomsMin(){
        return onSeekBarChangeMin(mBinding.activityParameterSeekbarNbOfRoomsMax, mBinding.activityParameterTextviewNbOfRoomsmin, mBinding.activityParameterTextviewNbOfRoomsmax);
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeNbOfRoomsMax(){
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                SeekBar seekBarMin = mBinding.activityParameterSeekbarNbOfRoomsMin;

                if(progress < seekBarMin.getProgress()){
                    seekBarMin.setProgress(progress);
                }

                if (progress < mBinding.activityParameterSeekbarNbOfBedRoomsMax.getProgress()){
                    mBinding.activityParameterSeekbarNbOfBedRoomsMax.setProgress(progress);
                }

                updateTextview(seekBarMin , seekBar, mBinding.activityParameterTextviewNbOfRoomsmin, mBinding.activityParameterTextviewNbOfRoomsmax);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeNbOfBedRoomsMin(){
        return onSeekBarChangeMin(mBinding.activityParameterSeekbarNbOfBedRoomsMax, mBinding.activityParameterTextviewNbOfBedRoomsmin, mBinding.activityParameterTextviewNbOfBedRoomsmax);
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeNbOfBedRoomsMax(){
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                SeekBar seekBarMin = mBinding.activityParameterSeekbarNbOfBedRoomsMin;

                if(progress < seekBarMin.getProgress()){
                    seekBarMin.setProgress(progress);
                }

                if (progress > mBinding.activityParameterSeekbarNbOfRoomsMax.getProgress()){
                    mBinding.activityParameterSeekbarNbOfRoomsMax.setProgress(progress);
                }

                updateTextview(seekBarMin , seekBar, mBinding.activityParameterTextviewNbOfBedRoomsmin, mBinding.activityParameterTextviewNbOfBedRoomsmax);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeAreaMin(){
        return onSeekBarChangeMin(mBinding.activityParameterSeekbarAreaMax, mBinding.activityParameterTextviewAreamin, mBinding.activityParameterTextviewAreamax);
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeAreaMax(){
        return onSeekBarChangeMax(mBinding.activityParameterSeekbarAreaMin, mBinding.activityParameterTextviewAreamin, mBinding.activityParameterTextviewAreamax);
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeNbOfPicturesMin(){
        return onSeekBarChangeMin(mBinding.activityParameterSeekbarNbofPicturesMax, mBinding.activityParameterTextviewNbofpicturesmin, mBinding.activityParameterTextviewNbofpicturesmax);
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeNbOfPicturesMax(){
        return onSeekBarChangeMax(mBinding.activityParameterSeekbarNbofPicturesMin, mBinding.activityParameterTextviewNbofpicturesmin, mBinding.activityParameterTextviewNbofpicturesmax);
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

        if (parameter.getListNearbyPOI() != null){
            for (long id : parameter.getListNearbyPOI()){
                mViewModel.getNearbyPOI(id).observe(this, nearbyPOI -> selectedNearbyPOI.add(nearbyPOI));
            }
        }

        if(parameter.getLatitude() != 999999999.0 && parameter.getLongitude() != 999999999.0){
            getAddress(parameter.getLatitude(), parameter.getLongitude());
        }

        mBinding.activityParameterSeekbarNbOfRoomsMin.setProgress(parameter.getNbOfRoomsMin());
        mBinding.activityParameterSeekbarNbOfRoomsMax.setProgress(parameter.getNbOfRoomsMax());
        mBinding.activityParameterSeekbarNbOfBedRoomsMin.setProgress(parameter.getNbOfBedRoomsMin());
        mBinding.activityParameterSeekbarNbOfBedRoomsMax.setProgress(parameter.getNbOfBedRoomsMax());
        mBinding.activityParameterSeekbarAreaMin.setProgress(parameter.getAreaMin());
        mBinding.activityParameterSeekbarAreaMax.setProgress(parameter.getAreaMax());
        mBinding.activityParameterSeekbarNbofPicturesMin.setProgress(parameter.getNbOfPicturesMin());
        mBinding.activityParameterSeekbarNbofPicturesMax.setProgress(parameter.getNbOfPicturesMax());
        mBinding.activityParameterSeekbarPriceMin.setProgress(parameter.getPriceMin());
        mBinding.activityParameterSeekbarPriceMax.setProgress(parameter.getPriceMax());
        mBinding.activityParameterSeekbarDistanceaddressMin.setProgress(parameter.getDistanceAddressMin() / 1000);
        mBinding.activityParameterSeekbarDistanceaddressMax.setProgress(parameter.getDistanceAddressMax() / 1000);

        updateTextview(mBinding.activityParameterSeekbarAreaMin, mBinding.activityParameterSeekbarAreaMax, mBinding.activityParameterTextviewAreamin,mBinding.activityParameterTextviewAreamax);
        updateTextview(mBinding.activityParameterSeekbarNbOfRoomsMin, mBinding.activityParameterSeekbarNbOfRoomsMax, mBinding.activityParameterTextviewNbOfRoomsmin,mBinding.activityParameterTextviewNbOfRoomsmax);
        updateTextview(mBinding.activityParameterSeekbarNbOfBedRoomsMin, mBinding.activityParameterSeekbarNbOfBedRoomsMax, mBinding.activityParameterTextviewNbOfBedRoomsmin,mBinding.activityParameterTextviewNbOfBedRoomsmax);
        updateTextview(mBinding.activityParameterSeekbarNbofPicturesMin, mBinding.activityParameterSeekbarNbofPicturesMax, mBinding.activityParameterTextviewNbofpicturesmin,mBinding.activityParameterTextviewNbofpicturesmax);
        updateTextview(mBinding.activityParameterSeekbarPriceMin, mBinding.activityParameterSeekbarPriceMax, mBinding.activityParameterTextviewPricemin,mBinding.activityParameterTextviewPricemax);
        updateTextview(mBinding.activityParameterSeekbarDistanceaddressMin, mBinding.activityParameterSeekbarDistanceaddressMax, mBinding.activityParameterTextviewDistanceaddressMin,mBinding.activityParameterTextviewDistanceaddressMax);

        mBinding.activityParameterEdittextRealEstateAgent.setText(parameter.getRealEstateAgent());
        mBinding.activityParameterEdittextCreatedatmin.setText(!(parameter.getCreatedAtMin() == 0) ? FormatUtils.formatDate(new Date(parameter.getCreatedAtMin())) : "");
        mBinding.activityParameterEdittextCreatedatmax.setText(!(parameter.getCreatedAtMax() == 0) ? FormatUtils.formatDate(new Date(parameter.getCreatedAtMax())) : "");
        mBinding.activityParameterEdittextDateofsalemin.setText(!(parameter.getDateOfSaleMin() == 0) ? FormatUtils.formatDate(new Date(parameter.getDateOfSaleMin())) : "");
        mBinding.activityParameterEdittextDateofsalemax.setText(!(parameter.getDateOfSaleMax() == 0) ? FormatUtils.formatDate(new Date(parameter.getDateOfSaleMax())) : "");

        switch (parameter.getSortDirection())
        {
            case ASCENDANT:
                mBinding.activityParameterRadiobuttonAscendant.setChecked(true);
                break;

            case DESCENDANT:
                mBinding.activityParameterRadiobuttonDescendant.setChecked(true);
                break;
        }

        switch (parameter.getOrderBy())
        {
            case PRICE:
                mBinding.activityParameterRadiobuttonPrice.setChecked(true);
                break;

            case NB_OF_ROOMS:
                mBinding.activityParameterRadiobuttonNbofrooms.setChecked(true);
                break;

            case NB_OF_BEDROOMS:
                mBinding.activityParameterRadiobuttonDateofsale.setChecked(true);
                break;

            case AREA:
                mBinding.activityParameterRadiobuttonArea.setChecked(true);
                break;

            case MARKETING_DATE:
                mBinding.activityParameterRadiobuttonMarketingdate.setChecked(true);
                break;

            case DATE_OF_SALE:
                mBinding.activityParameterRadiobuttonNbofbedrooms.setChecked(true);
                break;
        }

        switch (parameter.getSold()){
            case 0:
                mBinding.activityParameterRadiobuttonAvailable.setChecked(true);
                break;

            case 1:
                mBinding.activityParameterRadiobuttonSold.setChecked(true);
                mBinding.activityParameterLinearlayoutDateofsale.setVisibility(View.VISIBLE);
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

        int areaProgressMax = mBinding.activityParameterSeekbarAreaMax.getMax();
        int areaMax = mBinding.activityParameterSeekbarAreaMax.getProgress();
        parameter.setAreaMax(areaMax == areaProgressMax ? 999999999 : areaMax);
        parameter.setAreaMin(mBinding.activityParameterSeekbarAreaMin.getProgress());

        int nbOfRoomsProgressMax = mBinding.activityParameterSeekbarNbOfRoomsMax.getMax();
        int nbOfRoomsMax = mBinding.activityParameterSeekbarNbOfRoomsMax.getProgress();
        parameter.setNbOfRoomsMax(nbOfRoomsMax == nbOfRoomsProgressMax ? 999999999 : nbOfRoomsMax);
        parameter.setNbOfRoomsMin(mBinding.activityParameterSeekbarNbOfRoomsMin.getProgress());

        int nbOfBedRoomsProgressMax = mBinding.activityParameterSeekbarNbOfBedRoomsMax.getMax();
        int nbOfBedRoomsMax = mBinding.activityParameterSeekbarNbOfBedRoomsMax.getProgress();
        parameter.setNbOfBedRoomsMax(nbOfBedRoomsMax == nbOfBedRoomsProgressMax ? 999999999 : nbOfBedRoomsMax);
        parameter.setNbOfBedRoomsMin(mBinding.activityParameterSeekbarNbOfBedRoomsMin.getProgress());

        int nbOfNbOfPicturesProgressMax = mBinding.activityParameterSeekbarNbofPicturesMax.getMax();
        int nbOfNbOfPicturesMax = mBinding.activityParameterSeekbarNbofPicturesMax.getProgress();
        parameter.setNbOfPicturesMax(nbOfNbOfPicturesMax == nbOfNbOfPicturesProgressMax ? 999999999 : nbOfNbOfPicturesMax);
        parameter.setNbOfPicturesMin(mBinding.activityParameterSeekbarNbofPicturesMin.getProgress());

        int priceProgressMax = mBinding.activityParameterSeekbarPriceMax.getMax();
        int priceMax = mBinding.activityParameterSeekbarPriceMax.getProgress();
        parameter.setPriceMax(priceMax == priceProgressMax ? 999999999 : priceMax);
        parameter.setPriceMin(mBinding.activityParameterSeekbarPriceMin.getProgress());

        parameter.setRealEstateAgent(!mBinding.activityParameterEdittextRealEstateAgent.getText().toString().equals("") ? mBinding.activityParameterEdittextRealEstateAgent.getText().toString() : null);
        parameter.setCreatedAtMin(!mBinding.activityParameterEdittextCreatedatmin.getText().toString().equals("") ? FormatUtils.formatStringFormattedToDate(mBinding.activityParameterEdittextCreatedatmin.getText().toString()).getTime() : 0);
        parameter.setCreatedAtMax(!mBinding.activityParameterEdittextCreatedatmax.getText().toString().equals("") ? FormatUtils.formatStringFormattedToDate(mBinding.activityParameterEdittextCreatedatmax.getText().toString()).getTime() : 0);
        parameter.setDateOfSaleMin(!mBinding.activityParameterEdittextDateofsalemin.getText().toString().equals("") ? FormatUtils.formatStringFormattedToDate(mBinding.activityParameterEdittextDateofsalemin.getText().toString()).getTime() : 0);
        parameter.setDateOfSaleMax(!mBinding.activityParameterEdittextDateofsalemax.getText().toString().equals("") ? FormatUtils.formatStringFormattedToDate(mBinding.activityParameterEdittextDateofsalemax.getText().toString()).getTime() : 0);

        if (mBinding.activityParameterRadiobuttonAscendant.isChecked()){
            parameter.setSortDirection(Constants.SortDirection.ASCENDANT);
        } else if (mBinding.activityParameterRadiobuttonDescendant.isChecked()){
            parameter.setSortDirection(Constants.SortDirection.DESCENDANT);
        }

        if(mBinding.activityParameterRadiobuttonPrice.isChecked()){
          parameter.setOrderBy(Constants.OrderBy.PRICE);
        } else if(mBinding.activityParameterRadiobuttonArea.isChecked()){
            parameter.setOrderBy(Constants.OrderBy.AREA);
        } else if(mBinding.activityParameterRadiobuttonNbofrooms.isChecked()){
            parameter.setOrderBy(Constants.OrderBy.NB_OF_ROOMS);
        } else if(mBinding.activityParameterRadiobuttonNbofbedrooms.isChecked()){
            parameter.setOrderBy(Constants.OrderBy.NB_OF_BEDROOMS);
        } else if(mBinding.activityParameterRadiobuttonMarketingdate.isChecked()) {
            parameter.setOrderBy(Constants.OrderBy.MARKETING_DATE);
        } else if(mBinding.activityParameterRadiobuttonDateofsale.isChecked()) {
            parameter.setOrderBy(Constants.OrderBy.DATE_OF_SALE);
        }


        if (selectedNearbyPOI.size() != 0){
            long[] listIdNearbyPOI = new long[selectedNearbyPOI.size()];
            for (int i = 0; i < selectedNearbyPOI.size(); i++){
                listIdNearbyPOI[i] = selectedNearbyPOI.get(i).getId();
            }
            parameter.setListNearbyPOI(listIdNearbyPOI);
        }

        if(mBinding.activityParameterRadiobuttonAvailable.isChecked()){
            parameter.setSold((byte) 0);
        }else if(mBinding.activityParameterRadiobuttonSold.isChecked()){
            parameter.setSold((byte) 1);
        } else if(mBinding.activityParameterRadiobuttonAll.isChecked()){
            parameter.setSold((byte) 2);
        }

        if (mAddress != null){
            parameter.setLatitude(mAddress.getLatitude());
            parameter.setLongitude(mAddress.getLongitude());

            int distanceProgressMax = mBinding.activityParameterSeekbarDistanceaddressMax.getMax();
            int distanceRoomsMax = mBinding.activityParameterSeekbarDistanceaddressMax.getProgress();
            parameter.setDistanceAddressMax(distanceRoomsMax == distanceProgressMax ? 999999999 : distanceRoomsMax * 1000);
            parameter.setDistanceAddressMin(mBinding.activityParameterSeekbarDistanceaddressMin.getProgress() * 1000);
        }

        setResult(Activity.RESULT_OK, new Intent().putExtra("result", parameter));

        finish();
    }
}