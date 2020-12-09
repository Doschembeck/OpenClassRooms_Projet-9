package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import com.openclassrooms.realestatemanager.databinding.ActivityAuthenticationBinding;
import com.openclassrooms.realestatemanager.utils.Constants;


public class AuthenticationActivity extends AppCompatActivity {

    SharedPreferences mSharedPreferences;
    private ActivityAuthenticationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.activityAuthenticationButton.setOnClickListener(v -> onClickButtonContinue());

        initSharedPreferences();

        getRealEstateAgentSaved();

    }

    private void initSharedPreferences(){
        mSharedPreferences = getSharedPreferences(Constants.PREF_SHARED_KEY, MODE_PRIVATE);

        if(Constants.PREF_DEFVALUE_TEST.equals(mSharedPreferences.getString(Constants.PREF_CURRENCY_KEY, Constants.PREF_DEFVALUE_TEST))){
            mSharedPreferences.edit().putString(Constants.PREF_CURRENCY_KEY, "$").apply();
        }

    }

    private void getRealEstateAgentSaved(){
        String realEstateAgentName = mSharedPreferences.getString(Constants.PREF_REAL_ESTATE_AGENT_NAME_KEY,"");

        if (!realEstateAgentName.equals("")){
            binding.activityAuthenticationEdittextRealEstateAgentName.setText(realEstateAgentName);
        }
    }

    private void onClickButtonContinue(){
        String realEstateAgent = binding.activityAuthenticationEdittextRealEstateAgentName.getText().toString();

        if(!realEstateAgent.equals("")){
            mSharedPreferences.edit().putString(Constants.PREF_REAL_ESTATE_AGENT_NAME_KEY, realEstateAgent).apply();

            startActivity(new Intent(this, MainActivity.class));

        } else {
            Toast.makeText(this, "Vous devez entrer votre nom avant de continuer !", Toast.LENGTH_SHORT).show();
        }
    }
}