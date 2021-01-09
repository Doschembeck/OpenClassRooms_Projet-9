package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.realestatemanager.databinding.ActivityDetailsBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;
    private PropertyViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        long propertyId = getIntent().getLongExtra("property_id", 0);

        mViewModel.getProperty(propertyId).observe(this, this::updateUI);

        binding.fab.setOnClickListener(this::onClickFloatingActionButton);

        setSupportActionBar(binding.toolbar);
        binding.toolbarLayout.setTitle(getTitle());

    }

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
    }

    private void updateUI(Property property){
        
    }

    private void onClickFloatingActionButton(View view){
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}