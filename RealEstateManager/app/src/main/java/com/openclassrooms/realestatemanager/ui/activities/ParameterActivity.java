package com.openclassrooms.realestatemanager.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.openclassrooms.realestatemanager.databinding.ActivityParameterBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.model.Parameter;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

public class ParameterActivity extends AppCompatActivity {

    ActivityParameterBinding mBinding;
    PropertyViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        mBinding = ActivityParameterBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        Parameter parameter = getIntent().getParcelableExtra("parameter");

        if (parameter != null){
            updateUI(parameter);
        }

        mBinding.activityParameterButtonFilter.setOnClickListener(this::onClickFilterProperty);
    }

    private void updateUI(Parameter parameter) {

        mBinding.activityParameterEdittextPricemin.setText(String.valueOf(parameter.getPriceMin()));
        mBinding.activityParameterEdittextPricemax.setText(String.valueOf(parameter.getPriceMax()));
        mBinding.activityParameterEdittextAreamin.setText(String.valueOf(parameter.getAreaMin()));
        mBinding.activityParameterEdittextAreamax.setText(String.valueOf(parameter.getAreaMax()));
        mBinding.activityParameterEdittextNbOfRoomsmin.setText(String.valueOf(parameter.getNbOfRoomsMin()));
        mBinding.activityParameterEdittextNbOfRoomsmax.setText(String.valueOf(parameter.getNbOfRoomsMax()));
        mBinding.activityParameterEdittextNbOfBedRoomsmin.setText(String.valueOf(parameter.getNbOfBedRoomsMin()));
        mBinding.activityParameterEdittextNbOfBedRoomsmax.setText(String.valueOf(parameter.getNbOfBedRoomsMax()));
        mBinding.activityParameterEdittextRealEstateAgent.setText(parameter.getRealEstateAgent());

        switch (parameter.getSold()){
            case 0:
                mBinding.activityParameterRadiobuttonAvailable.setChecked(true);
                break;

            case 1:
                mBinding.activityParameterRadiobuttonSold.setChecked(true);
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

        parameter.setPriceMin(!mBinding.activityParameterEdittextPricemin.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextPricemin.getText().toString()) : 0);
        parameter.setPriceMax(!mBinding.activityParameterEdittextPricemax.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextPricemax.getText().toString()) : 0);
        parameter.setAreaMin(!mBinding.activityParameterEdittextAreamin.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextAreamin.getText().toString()) : 0);
        parameter.setAreaMax(!mBinding.activityParameterEdittextAreamax.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextAreamax.getText().toString()) : 0);
        parameter.setNbOfRoomsMin(!mBinding.activityParameterEdittextNbOfRoomsmin.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextNbOfRoomsmin.getText().toString()) : 0);
        parameter.setNbOfRoomsMax(!mBinding.activityParameterEdittextNbOfRoomsmax.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextNbOfRoomsmax.getText().toString()) : 0);
        parameter.setNbOfBedRoomsMin(!mBinding.activityParameterEdittextNbOfBedRoomsmin.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextNbOfBedRoomsmin.getText().toString()) : 0);
        parameter.setNbOfBedRoomsMax(!mBinding.activityParameterEdittextNbOfBedRoomsmax.getText().toString().equals("") ? Integer.parseInt(mBinding.activityParameterEdittextNbOfBedRoomsmax.getText().toString()) : 0);
        parameter.setRealEstateAgent(!mBinding.activityParameterEdittextRealEstateAgent.getText().toString().equals("") ? mBinding.activityParameterEdittextRealEstateAgent.getText().toString() : null);

        if(mBinding.activityParameterRadiobuttonAvailable.isChecked()){
            parameter.setSold((byte) 0);
        }else if(mBinding.activityParameterRadiobuttonSold.isChecked()){
            parameter.setSold((byte) 1);
        } else if(mBinding.activityParameterRadiobuttonAll.isChecked()){
            parameter.setSold((byte) 2);
        }

        setResult(Activity.RESULT_OK, new Intent().putExtra("result", parameter));

        finish();
    }
}