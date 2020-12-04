package com.openclassrooms.realestatemanager.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import com.openclassrooms.realestatemanager.databinding.ActivitySettingsBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

public class SettingsActivity extends AppCompatActivity {

    //Todo: Section simulateur de pret :
    //  - gerer intervale seekbar
    //  - Max valeurs seekbar

    ActivitySettingsBinding binding;

    private PropertyViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.activitySettingsToolbar.toolbarOnlyback.setOnClickListener(view -> onBackPressed());
        binding.activitySettingsButtonReset.setOnClickListener(v -> deleteAllProperty());

    }

    private void deleteAllProperty(){
        this.mViewModel.getAllProperty().observe(this, properties -> {
            for(Property p : properties){
                deleteProperty(p);
            }
        });
    }

    private void deleteProperty(Property property){
        this.mViewModel.deleteProperty(property);
    }

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        this.mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this)).get(PropertyViewModel.class);
    }

}