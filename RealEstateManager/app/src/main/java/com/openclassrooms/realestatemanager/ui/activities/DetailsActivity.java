package com.openclassrooms.realestatemanager.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityDetailsBinding;
import com.openclassrooms.realestatemanager.databinding.ContentDetailsBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Devise;
import com.openclassrooms.realestatemanager.model.NearbyPOI;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.ui.fragments.DetailFragment;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.FormatUtils;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetailsActivity extends AppCompatActivity {

    DetailFragment detailFragment;

    private ActivityDetailsBinding binding;
    private ContentDetailsBinding includeBinding;
    private PropertyViewModel mViewModel;
    private Devise mDevise;
    private Property mCurrentProperty;
    private long mCurrentPropertyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mCurrentPropertyId = getIntent().getLongExtra("property_id", 0);
        mViewModel.getProperty(mCurrentPropertyId).observe(this, this::updateCurrentproperty);

        configureAndShowDetailsFragment();

        configureToolBar();

    }

    private void updateCurrentproperty(Property property) {
        mCurrentProperty = property;
    }

    private void configureAndShowDetailsFragment(){
        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.activity_details_frame_layout);

        // Si details fragment n'est pas dans le frame layout alors on le créer et le met dedans
        if (detailFragment == null){
            detailFragment = new DetailFragment();

            // send propertyId
            Bundle bundle = new Bundle();
            bundle.putLong("property_id", mCurrentPropertyId );
            detailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_details_frame_layout, detailFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.setGroupVisible(R.id.menu_toolbar_group_main, false);
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void configureToolBar(){
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_details_activity_toolbar_compare :

                //todo
                break;

            case R.id.menu_details_activity_toolbar_loansimulate :
                Intent intent = new Intent(this, LoanSimulatorActivity.class);
                intent.putExtra("amount_property", mCurrentProperty.getPrice());
                startActivity(intent);
                break;

            case R.id.menu_details_activity_toolbar_editproperty :

                startActivity(new Intent(this, EditPropertyActivity.class)
                        .putExtra("property", mCurrentProperty)
                );

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

        return super.onOptionsItemSelected(item);
    }

}