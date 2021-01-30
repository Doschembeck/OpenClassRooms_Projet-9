package com.openclassrooms.realestatemanager.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.openclassrooms.realestatemanager.databinding.ActivitySettingsBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.ScriptsStats;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.HashMap;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    //Todo: Section simulateur de pret :
    //  - gerer intervale seekbar
    //  - Max valeurs seekbar

    ActivitySettingsBinding binding;

    private PropertyViewModel mViewModel;
    private SharedPreferences mSharedPreferences;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;

        mSharedPreferences = getSharedPreferences(Constants.PREF_SHARED_KEY, MODE_PRIVATE);

        configureSpinnerCurrency();

        binding.toolbarOnlyback.setOnClickListener(view -> onBackPressed());
        binding.activitySettingsButtonReset.setOnClickListener(v -> deleteAllProperty());
        binding.activitySettingsButtonScriptrate.setOnClickListener(v -> ScriptsStats.scriptStatsAllRate(mViewModel));

    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void configureSpinnerCurrency(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constants.LIST_OF_DEVISES_NAME);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.activitySettingsSpinnerCurrency.setAdapter(adapter);

        binding.activitySettingsSpinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSharedPreferences.edit().putString(Constants.PREF_CURRENCY_KEY, Constants.LIST_OF_DEVISES_ISO[position]).apply(); //todo: doit gerer les liveDate (Observables)
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void deleteAllProperty(){
        this.mViewModel.getAllLiveDataProperty().observe(this, properties -> {
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