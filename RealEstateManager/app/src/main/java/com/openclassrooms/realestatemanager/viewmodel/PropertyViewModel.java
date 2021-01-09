package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.database.repository.AddressDataRepository;
import com.openclassrooms.realestatemanager.database.repository.PropertyDataRepository;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class PropertyViewModel extends ViewModel {

    // OLD START
    public MutableLiveData<List<Property>> mListPropertyMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<String>> mListFilterPropertyMutableLiveData = new MutableLiveData<>(); //todo: a mettre dans un autre viewmodel
    // OLD END

    // REPOSITORY
    private final PropertyDataRepository propertyDataRepository;
    public final AddressDataRepository addressDataRepository; //todo: remettre en priver si pas utiliser ailleur
    private final Executor executor;

    // --- CONSTRUCTOR ---

    public PropertyViewModel(PropertyDataRepository propertyDataRepository, AddressDataRepository addressDataRepository, Executor executor) {
        this.propertyDataRepository = propertyDataRepository;
        this.addressDataRepository = addressDataRepository;
        this.executor = executor;
    }

    // ---------------
    //  FOR PROPERTY
    // ---------------

    // GET ALL PROPERTY
    public LiveData<List<Property>> getAllProperty(){
        return this.propertyDataRepository.getAllProperty();
    }

    // GET FILTRED PROPERTIES
    public LiveData<List<Property>> searchProperties(SupportSQLiteQuery query){
        return this.propertyDataRepository.searchProperties(query);
    }

    // GET PROPERTY
    public LiveData<Property> getProperty(long propertyId){
        return this.propertyDataRepository.getProperty(propertyId);
    }

    // CREATE PROPERTY
    public void createProperty(Property property){
        executor.execute(() -> {
            this.propertyDataRepository.createProperty(property);
        });
    }

    // UPDATE PROPERTY
    public void updateProperty(Property property){
        executor.execute(() -> {
            this.propertyDataRepository.updateProperty(property);
        });
    }

    public void deleteProperty(Property property){
        executor.execute(() -> {
            this.propertyDataRepository.deleteProperty(property);
        });
    }

    // ---------------
    //  FOR ADDRESS
    // ---------------

    // GET ADDRESS
    public LiveData<Address> getAddress(long addressId){
        return this.addressDataRepository.getAddress(addressId);
    }

    // CREATE ADDRESS
    //todo: lancer de maniére asynchrone et recuperer le retour de la methode
    public long createAddress(Address address){
          return this.addressDataRepository.createAddress(address);
    }

    // UPDATE ADDRESS
    public void updateAddress(Address address){
        executor.execute(() -> {
            this.addressDataRepository.updateAddress(address);
        });
    }
}

