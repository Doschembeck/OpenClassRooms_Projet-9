package com.openclassrooms.realestatemanager.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;
import com.openclassrooms.realestatemanager.databinding.ActivityParameterBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.NearbyPOI;
import com.openclassrooms.realestatemanager.model.Parameter;
import com.openclassrooms.realestatemanager.utils.ActivityUtils;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.FormatUtils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Observable;

public class ParameterActivity extends AppCompatActivity {

    private ActivityParameterBinding mBinding;
    private PropertyViewModel mViewModel;
    private List<Long> selectedNearbyPOI = new ArrayList<>();
    private Geocoder mGeocoder;
    private Address mAddress;
    private FusedLocationProviderClient fusedLocationClient;
    private List<Agent> agents = new ArrayList<>();
    private Parameter mCurrentParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        mBinding = ActivityParameterBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // --- INIT ---
        mGeocoder = ActivityUtils.setupAutoComplete(this);
        configureSpinnerPropertyType();
        configureSpinnerFilter();
        initLocation();

        Parameter parameter = getIntent().getParcelableExtra("parameter");

        if (parameter != null){
            updateUI(parameter);
        }

        // LISTENERS
        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
        mBinding.activityParameterButtonFilter.setOnClickListener(this::onClickFilterProperty);
        mBinding.activityParameterEdittextAddress.setOnFocusChangeListener((view, b) -> {
            if(b){
                view.clearFocus();
                startAutoComplete(view);
            }
        });
        mBinding.activityParameterImageviewMylocation.setOnClickListener(this::onClickMyLocation);
        mBinding.activityParameterImageviewAddressdelete.setOnClickListener(view -> {
            mBinding.activityParameterEdittextAddress.setText("");
            mAddress = null;
        });
        mBinding.activityParameterImageviewCreatedatmin.setOnClickListener(v -> ActivityUtils.createDatePickerDialog(this, mBinding.activityParameterEdittextCreatedatmin));
        mBinding.activityParameterImageviewCreatedatmax.setOnClickListener(v -> ActivityUtils.createDatePickerDialog(this, mBinding.activityParameterEdittextCreatedatmax));
        mBinding.activityParameterImageviewDateofsalemin.setOnClickListener(v -> ActivityUtils.createDatePickerDialog(this, mBinding.activityParameterEdittextDateofsalemin));
        mBinding.activityParameterImageviewDateofsalemax.setOnClickListener(v -> ActivityUtils.createDatePickerDialog(this, mBinding.activityParameterEdittextDateofsalemax));
        mBinding.activityParameterSwitchAgent.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                mBinding.activityParameterSpinnerAgents.setEnabled(true);
            } else {
                mBinding.activityParameterSpinnerAgents.setEnabled(false);
            }
        });

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

        initRangeSlider(mBinding.activityParameterRangeSeekBarNbofroom, mBinding.activityParameterTextviewNbOfRoomsmin, mBinding.activityParameterTextviewNbOfRoomsmax);
        initRangeSlider(mBinding.activityParameterRangeSeekBarNbofbedroom, mBinding.activityParameterTextviewNbOfBedRoomsmin, mBinding.activityParameterTextviewNbOfBedRoomsmax);
        initRangeSlider(mBinding.activityParameterRangeSeekBarArea, mBinding.activityParameterTextviewAreamin, mBinding.activityParameterTextviewAreamax);
        initRangeSlider(mBinding.activityParameterRangeSeekBarNbofpictures, mBinding.activityParameterTextviewNbofpicturesmin, mBinding.activityParameterTextviewNbofpicturesmax);
        initRangeSlider(mBinding.activityParameterRangeSeekBarPrice, mBinding.activityParameterTextviewPricemin, mBinding.activityParameterTextviewPricemax);
        initRangeSlider(mBinding.activityParameterRangeSeekBarDistance, mBinding.activityParameterTextviewDistanceaddressMin, mBinding.activityParameterTextviewDistanceaddressMax);

        mViewModel.getAllAgent().observe(this, this::configureSpinnerAgent);

    }

    private void initRangeSlider(RangeSlider rangeSlider, TextView textViewMin, TextView textViewMax){
        rangeSlider.setLabelFormatter(value -> String.valueOf((int) value));
        rangeSlider.addOnChangeListener((slider, value, fromUser) -> updateTextview(slider, textViewMin, textViewMax));
    }

    private void initLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void onClickMyLocation(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // Si les permissions ne sont pas acceptées
        } else {

            /** Récupère le locationManager qui gère la localisation */
            LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            /** Test si le gps est activé ou non */
            if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                /** on lance notre activity (qui est une dialog) */
//                Intent localIntent = new Intent(this, PermissionGps.class);
//                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(localIntent);
                startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));

            } else {
                fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        Log.d("TAG1", "LA LOCALISATION EST : " + location.getLatitude() + " | " + location.getLongitude());

                        getAddress(location.getLatitude(), location.getLongitude());

                    } else {
                        Toast.makeText(this, "Localisation non disponnible veuillez faire une recherche manuel", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void configureSpinnerPropertyType(){
        List<String> listPropertyType = new ArrayList<>();
        listPropertyType.add("Tout les types");
        listPropertyType.addAll(Arrays.asList(Constants.ListPropertyType));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listPropertyType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.activityParameterSpinnerPropertytype.setAdapter(adapter);
    }

    private void configureSpinnerFilter(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constants.arrayOrderBy);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.activityParameterSpinnerFilter.setAdapter(adapter);
    }

    private void configureSpinnerAgent(List<Agent> agents){
        //todo get list des agents

        this.agents = agents;

        List<String> agentsName = new ArrayList<>();
        for (Agent agent : agents){
            agentsName.add(agent.getLastname() + " " + agent.getFirstname());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, agentsName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.activityParameterSpinnerAgents.setAdapter(adapter);

        if(mCurrentParameter.getRealEstateAgent() != null) {
            mBinding.activityParameterSpinnerAgents.setEnabled(true);
            mBinding.activityParameterSwitchAgent.setChecked(true);

            // todo agents is empty
            long agentId = Long.parseLong(mCurrentParameter.getRealEstateAgent());
            int agentIdList = 0;

            for (int i = 0; i < agents.size(); i++) {
                if (agents.get(i).getId() == agentId) {
                    agentIdList = i;
                }
            }

            mBinding.activityParameterSpinnerAgents.setEnabled(true);
            mBinding.activityParameterSpinnerAgents.setSelection(agentIdList);

        } else {
            mBinding.activityParameterSpinnerAgents.setEnabled(false);
            mBinding.activityParameterSwitchAgent.setChecked(false);
        }

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

    private void updateTextview(RangeSlider slider, TextView textViewMin, TextView textViewMax){
        int min = Math.round(slider.getValues().get(0));
        int max = Math.round(slider.getValues().get(1));
        int sliderMax = Math.round(slider.getValueTo());

        String plusMax = "";
        if (max >= sliderMax){
            plusMax += "+";
        }

        textViewMax.setText(String.format("%s%s", plusMax, formatNumberSeek(max)));
        textViewMin.setText(formatNumberSeek(min));
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
                    mAddress = new Address(0, 0, address.getFeatureName(), address.getThoroughfare(), address.getLocality(), address.getPostalCode(), address.getCountryName(), address.getLatitude(), address.getLongitude());
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

        //mBinding.activityParameterLinearlayoutNearbypoi.removeAllViewsInLayout();
        for (NearbyPOI nearbyPOI : nearbyPOIList){
            CheckBox checkBox = new CheckBox(this);

            // Si le nearby est selectionné alors on le check
            for (int i = 0; i < selectedNearbyPOI.size(); i++){
                if (selectedNearbyPOI.get(i) == nearbyPOI.getId()){
                    checkBox.setChecked(true);
                }
            }

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked){
                    selectedNearbyPOI.add(nearbyPOI.getId());
                }else {
                    for (int i = 0; i < selectedNearbyPOI.size(); i++){
                        if (selectedNearbyPOI.get(i) == nearbyPOI.getId()){
                            selectedNearbyPOI.remove(i);
                        }
                    }
                }
            });
            checkBox.setText(nearbyPOI.getName());
            mBinding.activityParameterLinearlayoutNearbypoi.addView(checkBox);
        }
    }

    private void updateRangeSlider(int parameterMin, int parameterMax, RangeSlider slider, TextView textViewMin, TextView textViewMax){
        float sliderMax = slider.getValueTo();

        List<Float> listDistance = new ArrayList<>();
        listDistance.add((float) (Math.min(parameterMin, (int) sliderMax)));
        listDistance.add((float) (Math.min(parameterMax, (int) sliderMax)));

        slider.setValues(listDistance);
        updateTextview(slider, textViewMin, textViewMax);
    }

    private void updateUI(Parameter parameter) {

        mCurrentParameter = parameter;

        if (parameter.getListNearbyPOI() != null){
            for (long l : parameter.getListNearbyPOI()){
                selectedNearbyPOI.add(l);
            }
        }

        if(parameter.getLatitude() != 999999999.0 && parameter.getLongitude() != 999999999.0){
            getAddress(parameter.getLatitude(), parameter.getLongitude());
        }

        if(parameter.getTypeOfProperty() != 999999999.0){
            mBinding.activityParameterSpinnerPropertytype.setSelection(parameter.getTypeOfProperty() + 1);
        }

        updateRangeSlider(parameter.getNbOfRoomsMin(), parameter.getNbOfRoomsMax(), mBinding.activityParameterRangeSeekBarNbofroom, mBinding.activityParameterTextviewNbOfRoomsmin, mBinding.activityParameterTextviewNbOfRoomsmax);
        updateRangeSlider(parameter.getNbOfBedRoomsMin(), parameter.getNbOfBedRoomsMax(), mBinding.activityParameterRangeSeekBarNbofbedroom, mBinding.activityParameterTextviewNbOfBedRoomsmin, mBinding.activityParameterTextviewNbOfBedRoomsmax);
        updateRangeSlider(parameter.getAreaMin(), parameter.getAreaMax(), mBinding.activityParameterRangeSeekBarArea, mBinding.activityParameterTextviewAreamin, mBinding.activityParameterTextviewAreamax);
        updateRangeSlider(parameter.getNbOfPicturesMin(), parameter.getNbOfPicturesMax(), mBinding.activityParameterRangeSeekBarNbofpictures, mBinding.activityParameterTextviewNbofpicturesmin, mBinding.activityParameterTextviewNbofpicturesmax);
        updateRangeSlider(parameter.getPriceMin(), parameter.getPriceMax(), mBinding.activityParameterRangeSeekBarPrice, mBinding.activityParameterTextviewPricemin, mBinding.activityParameterTextviewPricemax);
        updateRangeSlider(parameter.getDistanceAddressMin() / 1000, parameter.getDistanceAddressMax() / 1000, mBinding.activityParameterRangeSeekBarDistance, mBinding.activityParameterTextviewDistanceaddressMin, mBinding.activityParameterTextviewDistanceaddressMax);

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

        Constants.OrderBy orderBy = parameter.getOrderBy();
        Constants.OrderBy[] listOrderBy = Constants.OrderBy.values();
        for (int i = 0; i < listOrderBy.length; i++){
            if(listOrderBy[i] == orderBy){
                mBinding.activityParameterSpinnerFilter.setSelection(i);
            }
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

        mViewModel.getAllNearbyPOI().observe(this, this::updateUIWithAllNearbyPoi);

    }

    private void configureViewModel(){
        this.mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this)).get(PropertyViewModel.class);
    }

    private void onClickFilterProperty(View view){

        Parameter parameter = new Parameter();

        int propertyType = (int) mBinding.activityParameterSpinnerPropertytype.getSelectedItemId();
        if (propertyType != 0){
            parameter.setTypeOfProperty(propertyType - 1);
        }

        float areaProgressMax = mBinding.activityParameterRangeSeekBarArea.getValueTo();
        float areaMax = mBinding.activityParameterRangeSeekBarArea.getValues().get(1);
        parameter.setAreaMax(Math.round(areaMax == areaProgressMax ? 999999999 : areaMax));
        parameter.setAreaMin(Math.round(mBinding.activityParameterRangeSeekBarArea.getValues().get(0)) );

        float nbOfRoomsProgressMax = mBinding.activityParameterRangeSeekBarNbofroom.getValueTo();
        float nbOfRoomsMax = mBinding.activityParameterRangeSeekBarNbofroom.getValues().get(1);
        parameter.setNbOfRoomsMax(Math.round(nbOfRoomsMax == nbOfRoomsProgressMax ? 999999999 : nbOfRoomsMax));
        parameter.setNbOfRoomsMin(Math.round(mBinding.activityParameterRangeSeekBarNbofroom.getValues().get(0)) );

        float nbOfBedRoomsProgressMax = mBinding.activityParameterRangeSeekBarNbofbedroom.getValueTo();
        float nbOfBedRoomsMax = mBinding.activityParameterRangeSeekBarNbofbedroom.getValues().get(1);
        parameter.setNbOfBedRoomsMax(Math.round(nbOfBedRoomsMax == nbOfBedRoomsProgressMax ? 999999999 : nbOfBedRoomsMax));
        parameter.setNbOfBedRoomsMin(Math.round(mBinding.activityParameterRangeSeekBarNbofbedroom.getValues().get(0)));

        float nbOfNbOfPicturesProgressMax = mBinding.activityParameterRangeSeekBarNbofpictures.getValueTo();
        float nbOfNbOfPicturesMax = mBinding.activityParameterRangeSeekBarNbofpictures.getValues().get(1);
        parameter.setNbOfPicturesMax(Math.round(nbOfNbOfPicturesMax == nbOfNbOfPicturesProgressMax ? 999999999 : nbOfNbOfPicturesMax));
        parameter.setNbOfPicturesMin(Math.round(mBinding.activityParameterRangeSeekBarNbofpictures.getValues().get(0)));

        float priceProgressMax = mBinding.activityParameterRangeSeekBarPrice.getValueTo();
        float priceMax = mBinding.activityParameterRangeSeekBarPrice.getValues().get(1);
        parameter.setPriceMax(Math.round(priceMax == priceProgressMax ? 999999999 : priceMax));
        parameter.setPriceMin(Math.round(mBinding.activityParameterRangeSeekBarPrice.getValues().get(0)));

        if (mBinding.activityParameterSpinnerAgents.isEnabled()){
            parameter.setRealEstateAgent(String.valueOf(agents.get((int) mBinding.activityParameterSpinnerAgents.getSelectedItemId()).getId()));
        }

        parameter.setCreatedAtMin(!mBinding.activityParameterEdittextCreatedatmin.getText().toString().equals("") ? FormatUtils.formatStringFormattedToDate(mBinding.activityParameterEdittextCreatedatmin.getText().toString()).getTime() : 0);
        parameter.setCreatedAtMax(!mBinding.activityParameterEdittextCreatedatmax.getText().toString().equals("") ? FormatUtils.formatStringFormattedToDate(mBinding.activityParameterEdittextCreatedatmax.getText().toString()).getTime() : 0);
        parameter.setDateOfSaleMin(!mBinding.activityParameterEdittextDateofsalemin.getText().toString().equals("") ? FormatUtils.formatStringFormattedToDate(mBinding.activityParameterEdittextDateofsalemin.getText().toString()).getTime() : 0);
        parameter.setDateOfSaleMax(!mBinding.activityParameterEdittextDateofsalemax.getText().toString().equals("") ? FormatUtils.formatStringFormattedToDate(mBinding.activityParameterEdittextDateofsalemax.getText().toString()).getTime() : 0);

        if (mBinding.activityParameterRadiobuttonAscendant.isChecked()){
            parameter.setSortDirection(Constants.SortDirection.ASCENDANT);
        } else if (mBinding.activityParameterRadiobuttonDescendant.isChecked()){
            parameter.setSortDirection(Constants.SortDirection.DESCENDANT);
        }

        parameter.setOrderBy(Constants.OrderBy.values()[(int)mBinding.activityParameterSpinnerFilter.getSelectedItemId()]);

        if (selectedNearbyPOI.size() != 0){
            long[] listIdNearbyPOI = new long[selectedNearbyPOI.size()];
            for (int i = 0; i < selectedNearbyPOI.size(); i++){
                listIdNearbyPOI[i] = selectedNearbyPOI.get(i);
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

            float distanceProgressMax = mBinding.activityParameterRangeSeekBarDistance.getValueTo();
            float distanceRoomsMax = mBinding.activityParameterRangeSeekBarDistance.getValues().get(1);
            parameter.setDistanceAddressMax(Math.round(distanceRoomsMax == distanceProgressMax ? 999999999 : distanceRoomsMax * 1000));
            parameter.setDistanceAddressMin(Math.round(mBinding.activityParameterRangeSeekBarDistance.getValues().get(0) * 1000));
        }

        setResult(Activity.RESULT_OK, new Intent().putExtra("result", parameter));

        finish();
    }
}