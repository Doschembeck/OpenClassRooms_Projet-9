package com.openclassrooms.realestatemanager.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPropertyActivity extends AppCompatActivity {

    @BindView(R.id.activity_edit_property_edittext_area) EditText mEditTextArea;
    @BindView(R.id.activity_edit_property_edittext_price) EditText mEditTextPrice;
    @BindView(R.id.activity_edit_property_edittext_nbofrooms) EditText mEditTextNbOfRooms;
    @BindView(R.id.activity_edit_property_edittext_nbofbedrooms) EditText mEditTextNbOfBedRooms;
    @BindView(R.id.activity_edit_property_edittext_description) EditText mEditTextDescription;
    @BindView(R.id.activity_edit_property_edittext_dateofsold) EditText mEditTextDateOfSold;
    @BindView(R.id.activity_edit_property_edittext_dateofentry) EditText mEditTextDateOfEntry;
    @BindView(R.id.activity_edit_property_edittext_realestateagent) EditText mEditTextRealEstateAgent;

    @BindView(R.id.activity_edit_property_spinner_propertytype) Spinner mSpinnerPropertyType;
    @BindView(R.id.activity_edit_property_spinner_issold) Spinner mSpinnerIsSold;

    private PropertyViewModel mViewModel;
    private static final String[]paths = {"Maison", "Appartement"};
    private static final String[]disponnibilities = {"Disponnible", "Vendu"};

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
        createProperty(getPropertyWithUI());
        finish();
    }

    private Property getPropertyWithUI(){
        //todo: faire une creation complete

        boolean isSold = false;
        switch ((int) mSpinnerIsSold.getSelectedItemId()){
            case 0:
                isSold = false;
                break;
            case 1:
                isSold = true;
                break;
        }

        int propertyType = (int) mSpinnerPropertyType.getSelectedItemId();
        double price = Double.parseDouble(mEditTextPrice.getText().toString());
        float area = Float.parseFloat(mEditTextArea.getText().toString());
        int nbOfRooms = Integer.parseInt(mEditTextNbOfRooms.getText().toString());
        int nbOfBedRooms = Integer.parseInt(mEditTextNbOfBedRooms.getText().toString());
        String description = mEditTextDescription.getText().toString();
        long addressId = 1;
        String dateOfEntry = mEditTextDateOfEntry.getText().toString();
        String dateOfSold = mEditTextDateOfSold.getText().toString();
        String realEstateAgent = mEditTextRealEstateAgent.getText().toString();
        String createdAt = "02-12-2020";
        String updatedAt = "02-12-2020";

        return new Property(0,propertyType,price,area,nbOfRooms,nbOfBedRooms,description,addressId,
                isSold,dateOfEntry,realEstateAgent ,createdAt, updatedAt);
    }

    private void configureSpinnerPropertyType(){
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerPropertyType.setAdapter(adapter);
    }

    private void configureSpinnerIsSold(){
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,disponnibilities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerIsSold.setAdapter(adapter);
    }

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        this.mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this)).get(PropertyViewModel.class);
    }

    private void createProperty(Property property){
        this.mViewModel.createProperty(property);
    }


}