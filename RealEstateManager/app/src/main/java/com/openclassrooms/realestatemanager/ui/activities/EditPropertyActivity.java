package com.openclassrooms.realestatemanager.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPropertyActivity extends AppCompatActivity {

    private PropertyViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        setContentView(R.layout.activity_edit_property);
        ButterKnife.bind(this);



    }

    @OnClick(R.id.activity_edit_property_addproperty)
    public void onClickButtonAddProperty(){
        Property property1 = new Property(0, 1, 240600d, 290,
                5, 2, "description", 1, false,
                "22/11/2020", "Thomas", "22/11/2020", "22/11/2020");

        createProperty(property1);
        finish();
    }

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
    }

    private void createProperty(Property property){
        this.mViewModel.createProperty(property);
    }
}