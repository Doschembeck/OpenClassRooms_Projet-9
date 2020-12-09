package com.openclassrooms.realestatemanager.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.openclassrooms.realestatemanager.databinding.ActivitySettingsBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

public class SettingsActivity extends AppCompatActivity {

    //Todo: Section simulateur de pret :
    //  - gerer intervale seekbar
    //  - Max valeurs seekbar

    ActivitySettingsBinding binding;

    private PropertyViewModel mViewModel;
    private SharedPreferences mSharedPreferences;
    private static final String[]paths = {"$", "€", "£", "¥", "₩", "CHF"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mSharedPreferences = getSharedPreferences(Constants.PREF_SHARED_KEY, MODE_PRIVATE);

        configureSpinnerCurrency();

        binding.activitySettingsToolbar.toolbarOnlyback.setOnClickListener(view -> onBackPressed());
        binding.activitySettingsButtonReset.setOnClickListener(v -> deleteAllProperty());

    }

    private void configureSpinnerCurrency(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.activitySettingsSpinnerCurrency.setAdapter(adapter);

        binding.activitySettingsSpinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSharedPreferences.edit().putString(Constants.PREF_CURRENCY_KEY, paths[position]).apply(); //todo: doit gerer les liveDate (Observables)
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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