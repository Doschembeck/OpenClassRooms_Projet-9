package com.openclassrooms.realestatemanager.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityEditAgentBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.List;

public class EditAgentActivity extends AppCompatActivity {

    private ActivityEditAgentBinding mBinding;
    private PropertyViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initOnCreate();

        initListeners();


    }

    private void initOnCreate(){
        configureViewModel();
        mBinding = ActivityEditAgentBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    private void initListeners(){

        mBinding.activityEditAgentButtonApply.setOnClickListener(v -> {
            createAgent();
            finish();
        });
    }

    private void createAgent(){
        String firstname = mBinding.activityEditAgentEditextFirstname.getText().toString();
        String lastname = mBinding.activityEditAgentEditextLastname.getText().toString();
        String urlPicture = mBinding.activityEditAgentEditextUrlpicture.getText().toString();

        mViewModel.createAgent(new Agent(0, firstname, lastname, urlPicture));
    }

    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
    }
}