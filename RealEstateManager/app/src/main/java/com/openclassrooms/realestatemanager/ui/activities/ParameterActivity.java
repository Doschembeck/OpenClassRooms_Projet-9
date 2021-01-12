package com.openclassrooms.realestatemanager.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityParameterBinding;
import com.openclassrooms.realestatemanager.model.Parameter;

public class ParameterActivity extends AppCompatActivity {

    ActivityParameterBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityParameterBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.activityParameterButtonFilter.setOnClickListener(this::onClickFilterProperty);
    }

    private void onClickFilterProperty(View view){

        Parameter parameter = new Parameter();

        parameter.setPriceMin(Integer.parseInt(mBinding.activityParameterEdittextPricemin.getText().toString()));
        parameter.setPriceMax(Integer.parseInt(mBinding.activityParameterEdittextPricemax.getText().toString()));
        parameter.setAreaMin(Integer.parseInt(mBinding.activityParameterEdittextAreamin.getText().toString()));
        parameter.setAreaMax(Integer.parseInt(mBinding.activityParameterEdittextAreamax.getText().toString()));
        parameter.setNbOfRoomsMin(Integer.parseInt(mBinding.activityParameterEdittextNbOfRoomsmin.getText().toString()));
        parameter.setNbOfRoomsMax(Integer.parseInt(mBinding.activityParameterEdittextNbOfRoomsmax.getText().toString()));
        parameter.setNbOfBedRoomsMin(Integer.parseInt(mBinding.activityParameterEdittextNbOfBedRoomsmin.getText().toString()));
        parameter.setNbOfBedRoomsMax(Integer.parseInt(mBinding.activityParameterEdittextNbOfBedRoomsmax.getText().toString()));
        parameter.setRealEstateAgent(mBinding.activityParameterEdittextRealEstateAgent.getText().toString());

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