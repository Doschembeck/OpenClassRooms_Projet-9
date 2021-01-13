package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.database.repository.AddressDataRepository;
import com.openclassrooms.realestatemanager.database.repository.PhotoDataRepository;
import com.openclassrooms.realestatemanager.database.repository.PropertyDataRepository;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Photo;
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
    private final PhotoDataRepository photoDataRepository;
    private final Executor executor;

    // --- CONSTRUCTOR ---

    public PropertyViewModel(PropertyDataRepository propertyDataRepository, AddressDataRepository addressDataRepository, PhotoDataRepository photoDataRepository, Executor executor) {
        this.propertyDataRepository = propertyDataRepository;
        this.addressDataRepository = addressDataRepository;
        this.photoDataRepository = photoDataRepository;
        this.executor = executor;
    }

    // ---------------
    //  region FOR PROPERTY
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
    public long createProperty(Property property){
        return this.propertyDataRepository.createProperty(property);
    }

    // UPDATE PROPERTY
    public void updateProperty(Property property){
        executor.execute(() -> this.propertyDataRepository.updateProperty(property));
    }

    public void deleteProperty(Property property){
        executor.execute(() -> this.propertyDataRepository.deleteProperty(property));
    }

    // endregion

    // ---------------
    //  region FOR PHOTO
    // ---------------

    // GET ALL PHOTOS
    public LiveData<List<Photo>> getAllPropertyPhoto(long propertyId){
        return this.photoDataRepository.getAllPropertyPhotos(propertyId);
    }

    // GET PHOTO
    public LiveData<Photo> getPhoto(long photoId){
        return this.photoDataRepository.getPhoto(photoId);
    }

    // CREATE PHOTO
    public void createPhoto(Photo photo){
        executor.execute(() -> this.photoDataRepository.createPhoto(photo));
    }

    // UPDATE PHOTO
    public void updatePhoto(Photo photo){
        executor.execute(() -> {
            this.photoDataRepository.updatePhoto(photo);
        });
    }

    public void deletePhoto(Photo photo){
        executor.execute(() -> {
            this.photoDataRepository.deletePhoto(photo);
        });
    }

    // endregion

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

