package com.openclassrooms.realestatemanager.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityDetailsBinding;
import com.openclassrooms.realestatemanager.databinding.ContentDetailsBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.NearbyPOI;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;
    private ContentDetailsBinding includeBinding;
    private PropertyViewModel mViewModel;
    SharedPreferences mSharedPreferences;
    private String devise;
    private int currentIndexPicture = 0;
    private List<Photo> mPictureList = new ArrayList<>();
    private Property mCurrentProperty;

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

        // Listeners
        binding.activityDetailsButtonPicturearrowback.setOnClickListener(v -> {
            if (currentIndexPicture != 0){
                currentIndexPicture--;
                displayPicture();
            }
        });
        binding.activityDetailsButtonPicturearrowforward.setOnClickListener(v -> {
            if (mPictureList.size() != currentIndexPicture + 1){
                currentIndexPicture++;
                displayPicture();
            }
        });
        binding.activityDetailsFabFavorite.setOnClickListener(this::onClickFloatingActionButton);

        configureToolBar();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details_activity_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.menu_details_activity_toolbar_compare :

                //todo
                break;

            case R.id.menu_details_activity_toolbar_loansimulate :

                startActivity(new Intent(this, LoanSimulatorActivity.class));

                break;

            case R.id.menu_details_activity_toolbar_editproperty :

                //todo à améliorer
                startActivity(new Intent(this, EditPropertyActivity.class));

                break;

            case R.id.menu_details_activity_toolbar_sold :

                break;

            case R.id.menu_details_activity_toolbar_deleteproperty :

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Etes vous sur de vouloir supprimer cette propriété ?");
                builder.setCancelable(true);
                builder.setPositiveButton("Oui", (dialog, which) -> {
                    if (mCurrentProperty != null){
                        mViewModel.deleteProperty(mCurrentProperty);
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }

        return true;
    }

    private void configureToolBar(){

        //todo: Changer la couleur de la fleche de retour
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        binding.toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
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

    private void displayPicture(){

        if (currentIndexPicture == 0){
            binding.activityDetailsButtonPicturearrowback.setVisibility(View.GONE);
        } else {
            binding.activityDetailsButtonPicturearrowback.setVisibility(View.VISIBLE);
        }

        if (mPictureList.size() == currentIndexPicture + 1){
            binding.activityDetailsButtonPicturearrowforward.setVisibility(View.GONE);
        } else {
            binding.activityDetailsButtonPicturearrowforward.setVisibility(View.VISIBLE);
        }

        if (mPictureList.size() != -1){
            Glide.with(this)
                    .load(mPictureList.get(currentIndexPicture).getPhoto())
                    .centerCrop()
                    .into(binding.activityDetailsPhotolist);

            binding.activityDetailsTextviewPicturedescription.setText(mPictureList.get(currentIndexPicture).getPhotoDescription());
            binding.activityDetailsTextviewCurrentindexpicture.setText(currentIndexPicture + 1 + "/" + mPictureList.size());
        }

    }

    private void updateUIWithAgent(Agent agent){
        includeBinding.contentDetailTextviewRealestateagent.setText(agent.getLastname().toUpperCase() + " " + agent.getFirstname());
    }

    private void updateUIWithNearbyPOI(List<NearbyPOI> nearbyPOIList){

        if (nearbyPOIList.size() != -1){

            includeBinding.contentDetailsLinearlayoutNearbypoi.setVisibility(View.VISIBLE);

            for (int i = 0; i < nearbyPOIList.size(); i++){

                String appendBefore = "";

                if (i != 0) {
                    appendBefore += ", ";
                }

                includeBinding.contentDetailTextviewNearbypoi.append(appendBefore + nearbyPOIList.get(i).getName());

            }
        } else {
            includeBinding.contentDetailsLinearlayoutNearbypoi.setVisibility(View.GONE);
        }

    }

    private void updateUIWithProperty(Property property){

        mCurrentProperty = property;

        // updateUIWith...
        mViewModel.getAddress(property.getAddressId()).observe(this, this::updateUIWithAddress);
        mViewModel.getAllPropertyPhoto(property.getAddressId()).observe(this, photoList -> {
            mPictureList = photoList;
            displayPicture();
        });
        mViewModel.getAgent(property.getAgentId()).observe(this, this::updateUIWithAgent);
        mViewModel.getPropertyForNearbyPoi(property.getId()).observe(this, this::updateUIWithNearbyPOI);

        // UpdateUIWithProperty
        includeBinding.contentDetailTextviewDateofentry.setText(new SimpleDateFormat("dd/MM/yyyy à hh:mm").format(property.getCreatedAt()));
        includeBinding.contentDetailTextviewTypeofproperty.setText(Constants.ListPropertyType[property.getPropertyTypeId()]);
        includeBinding.contentDetailTextviewNbofrooms.setText("" + property.getNbOfRooms());
        includeBinding.contentDetailTextviewNbofbedrooms.setText("" + property.getNbOfBedRooms());
        includeBinding.contentDetailTextviewArea.setText(property.getArea() + " m²");
        includeBinding.contentDetailTextviewPrice.setText(Utils.formatEditTextWithDevise(property.getPrice(), devise));
        includeBinding.contentDetailTextviewPricepersquaremeter.setText(Utils.formatEditTextWithDevise(property.getPrice() / property.getArea(), devise) + "/m²");
        includeBinding.contentDetailTextviewDescription.setText(property.getDescription());
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