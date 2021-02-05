package com.openclassrooms.realestatemanager.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;
    private ContentDetailsBinding includeBinding;
    private PropertyViewModel mViewModel;
    SharedPreferences mSharedPreferences;
    private String devise;
    private int currentIndexPicture = 0;
    private List<Photo> mPictureList = new ArrayList<>();
    private Property mCurrentProperty;
    boolean isFavorite;

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

                //todo à améliorer envoyer la property actuel
                Intent intent = new Intent(this, LoanSimulatorActivity.class);
                intent.putExtra("amount_property", mCurrentProperty.getPrice());
                startActivity(intent);

                break;

            case R.id.menu_details_activity_toolbar_editproperty :

                //todo à améliorer envoyer la property actuel
                startActivity(new Intent(this, EditPropertyActivity.class));

                break;

            case R.id.menu_details_activity_toolbar_sold :

                //todo ajouter une date de vente
                mCurrentProperty.setSold(!mCurrentProperty.isSold());
                mViewModel.updateProperty(mCurrentProperty);
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

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void configureToolBar(){
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
    }

    private void updateUIWithAddress(Address address){
        includeBinding.contentDetailTextviewAddress.setText(address.getCompleteAddress());
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

        if (currentIndexPicture <= mPictureList.size()){

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
        if (property == null) return;

        mCurrentProperty = property;

        checkIfPropertyIsFavorite();

        // updateUIWith...
        mViewModel.getAddress(property.getAddressId()).observe(this, this::updateUIWithAddress);
        mViewModel.getAllPropertyPhoto(property.getAddressId()).observe(this, photoList -> {
            if (photoList == null || photoList.size() == 0) return;

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

    private void checkIfPropertyIsFavorite(){
        // Get the list of favorites properties
        Set<String> set = mSharedPreferences.getStringSet(Constants.PREF_FAVORITES_PROPERTIES_KEY, new HashSet<>());

        if (set.contains(String.valueOf(mCurrentProperty.getId()))){
            isFavorite = true;
            binding.activityDetailsFabFavorite.setImageResource(R.drawable.ic_baseline_star_24);
            binding.activityDetailsFabFavorite.setColorFilter(getResources().getColor(R.color.starFavorite), PorterDuff.Mode.SRC_ATOP);
        }else {
            isFavorite = false;
            binding.activityDetailsFabFavorite.setImageResource(R.drawable.ic_baseline_star_outline_24);
        }
    }

    private void onClickFloatingActionButton(View view){
        String message = "ERROR";

        // Get the list of favorites properties
        Set<String> set = mSharedPreferences.getStringSet(Constants.PREF_FAVORITES_PROPERTIES_KEY, new HashSet<>());

        // Add or remove
        if (!isFavorite){
            set.add(String.valueOf(mCurrentProperty.getId()));
            message = "Ajouté aux favoris";

        } else if (isFavorite){
            set.remove(String.valueOf(mCurrentProperty.getId()));
            message = "Supprimé des favoris";
        }

        // Update the favorite list
        mSharedPreferences.edit().putStringSet(Constants.PREF_FAVORITES_PROPERTIES_KEY, set).apply();

        checkIfPropertyIsFavorite();

        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

}