package com.openclassrooms.realestatemanager.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPropertyActivity extends AppCompatActivity {

    private PropertyViewModel mViewModel;

    private Spinner spinnerPropertyType;
    private Spinner spinnerIsSold;
    private static final String[]paths = {"Maison", "Appartement"};
    private int mPropertyType = 0;
    private boolean isSold = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        setContentView(R.layout.activity_edit_property);
        ButterKnife.bind(this);

        configureSpinnerPropertyType();
        configureSpinnerIsSold();

    }

    @OnClick(R.id.activity_edit_property_addproperty)
    public void onClickButtonAddProperty(){
        Property property1 = new Property(0, 1, 240600d, 290,
                5, 2, "description", 1, false,
                "22/11/2020", "Thomas", "22/11/2020", "22/11/2020");

        createProperty(property1);
        finish();
    }

    private void configureSpinnerPropertyType(){
        spinnerPropertyType = (Spinner)findViewById(R.id.activity_edit_property_spinner_propertytype);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPropertyType.setAdapter(adapter);
        spinnerPropertyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPropertyType = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void configureSpinnerIsSold(){
        spinnerIsSold = (Spinner)findViewById(R.id.activity_edit_property_spinner_issold);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerIsSold.setAdapter(adapter);
        spinnerIsSold.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0){
                    isSold = false;
                } else if (i == 1){
                    isSold = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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