package com.openclassrooms.realestatemanager.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.openclassrooms.realestatemanager.database.repository.AddressDataRepository;
import com.openclassrooms.realestatemanager.databinding.ActivityEditPropertyBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.Random;

public class EditPropertyActivity extends AppCompatActivity {

    private PropertyViewModel mViewModel;
    private ActivityEditPropertyBinding binding;

    private static final String[]paths = {"Maison", "Appartement"};
    private static final String[]disponnibilities = {"Disponnible", "Vendu"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();

        binding = ActivityEditPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.activityEditPropertyToolbar.toolbarOnlyback.setOnClickListener(v -> onBackPressed());
        binding.activityEditPropertyAddproperty.setOnClickListener(v -> onClickButtonAddProperty());

        configureSpinnerPropertyType();
        configureSpinnerIsSold();


    }

    private void onClickButtonAddProperty(){
        createProperty(getPropertyWithUI());
        finish();
    }

    private Property getPropertyWithUI(){
        //todo: faire une creation complete

        //todo: Recuperer l'id au lieu d'en faire un aleatoire
        long addressId = createAddress(new Address(0, 69, "rue de la rouete", "Massieux", "01600", "France"));

        boolean isSold = false;
        switch ((int) binding.activityEditPropertySpinnerIssold.getSelectedItemId()){
            case 0:
                isSold = false;
                break;
            case 1:
                isSold = true;
                break;
        }

        int propertyType = (int) binding.activityEditPropertySpinnerPropertytype.getSelectedItemId();
        double price = Double.parseDouble(binding.activityEditPropertyEdittextPrice.getText().toString());
        float area = Float.parseFloat(binding.activityEditPropertyEdittextArea.getText().toString());
        int nbOfRooms = Integer.parseInt(binding.activityEditPropertyEdittextNbofrooms.getText().toString());
        int nbOfBedRooms = Integer.parseInt(binding.activityEditPropertyEdittextNbofbedrooms.getText().toString());
        String description = binding.activityEditPropertyEdittextDescription.getText().toString();
//        long addressId = address.getId(); //todo: a voir
        String dateOfEntry = binding.activityEditPropertyEdittextDateofentry.getText().toString();
        String dateOfSold = binding.activityEditPropertyEdittextDateofsold.getText().toString();
        String realEstateAgent = binding.activityEditPropertyEdittextRealestateagent.getText().toString();
        String createdAt = "02-12-2020";
        String updatedAt = "02-12-2020";

        return new Property(0,propertyType,price,area,nbOfRooms,nbOfBedRooms,description,addressId,
                isSold,dateOfEntry,realEstateAgent ,createdAt, updatedAt);
    }

    private void configureSpinnerPropertyType(){
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.activityEditPropertySpinnerPropertytype.setAdapter(adapter);
    }

    private void configureSpinnerIsSold(){
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,disponnibilities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.activityEditPropertySpinnerIssold.setAdapter(adapter);
    }

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        this.mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this)).get(PropertyViewModel.class);
    }

    private void createProperty(Property property){
        this.mViewModel.createProperty(property);
    }

    private long createAddress(Address address){
        return this.mViewModel.createAddress(address); //todo: need return long
    }

}