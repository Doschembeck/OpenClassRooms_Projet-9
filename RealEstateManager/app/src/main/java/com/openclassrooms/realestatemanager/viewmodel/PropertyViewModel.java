package com.openclassrooms.realestatemanager.viewmodel;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.database.repository.AddressDataRepository;
import com.openclassrooms.realestatemanager.database.repository.AgentDataRepository;
import com.openclassrooms.realestatemanager.database.repository.NearbyPoiDataRepository;
import com.openclassrooms.realestatemanager.database.repository.PhotoDataRepository;
import com.openclassrooms.realestatemanager.database.repository.PropertyDataRepository;
import com.openclassrooms.realestatemanager.database.repository.PropertyNearbyPoiJoinDataRepository;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.NearbyPOI;
import com.openclassrooms.realestatemanager.model.Parameter;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyNearbyPoiJoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class PropertyViewModel extends ViewModel {

    // DATA
    public MutableLiveData<List<Photo>> propertyPictureListMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<List<Property>> mListPropertyMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Parameter> mCurrentParameterMutableLiveData = new MutableLiveData<>(new Parameter());
    public MutableLiveData<Long> mCurrentPropertyIdSelected = new MutableLiveData<>();
    public boolean onlyFavorites = false;

    // REPOSITORY
    private final PropertyDataRepository propertyDataRepository;
    private final AddressDataRepository addressDataRepository;
    private final PhotoDataRepository photoDataRepository;
    private final AgentDataRepository agentDataRepository;
    private final NearbyPoiDataRepository nearbyPoiDataRepository;
    private final PropertyNearbyPoiJoinDataRepository propertyNearbyPoiJoinDataRepository;
    private final Executor executor;

    // --- CONSTRUCTOR ---
    public PropertyViewModel(PropertyDataRepository propertyDataRepository, AddressDataRepository addressDataRepository, PhotoDataRepository photoDataRepository, AgentDataRepository agentDataRepository, NearbyPoiDataRepository nearbyPoiDataRepository, PropertyNearbyPoiJoinDataRepository propertyNearbyPoiJoinDataRepository, Executor executor) {
        this.propertyDataRepository = propertyDataRepository;
        this.addressDataRepository = addressDataRepository;
        this.photoDataRepository = photoDataRepository;
        this.agentDataRepository = agentDataRepository;
        this.nearbyPoiDataRepository = nearbyPoiDataRepository;
        this.propertyNearbyPoiJoinDataRepository = propertyNearbyPoiJoinDataRepository;
        this.executor = executor;
    }

    // ---------------
    //  region FOR PROPERTY
    // ---------------

    // GET ALL LIVE DATA PROPERTY
    public LiveData<List<Property>> getAllLiveDataProperty(){
        return this.propertyDataRepository.getAllLiveDataProperty();
    }

    // GET ALL PROPERTY
    public List<Property> getAllProperty(){
        return this.propertyDataRepository.getAllProperty();
    }

    // GET FILTRED PROPERTIES
    public LiveData<List<Property>> searchProperties(Parameter parameter){
        mCurrentParameterMutableLiveData.setValue(parameter);
        return this.propertyDataRepository.searchProperties(parameter.getParamsFormatted());
    }

    // GET PROPERTY
    public LiveData<Property> getProperty(long propertyId){
        return this.propertyDataRepository.getProperty(propertyId);
    }

    // GET THE AVERAGE OF PricePerSquareMeter OF city IN LIST
    public Cursor getListCityWithAveragePricePerSquareMeter(){
        return this.propertyDataRepository.getListCityWithAveragePricePerSquareMeter();
    }

    // CREATE PROPERTY
    public long createProperty(Property property){
        return this.propertyDataRepository.createProperty(property);
    }

    // UPDATE PROPERTY
    public void updateProperty(Property property){
        executor.execute(() -> this.propertyDataRepository.updateProperty(property));
    }

    // DELETE NearbyPOI
    public void deleteProperty(Property property){
        executor.execute(() -> this.propertyDataRepository.deleteProperty(property));
    }

    // endregion

    // ---------------
    //  region FOR NearbyPOI
    // ---------------

    // GET ALL NearbyPOI
    public LiveData<List<NearbyPOI>> getAllNearbyPOI(){
        return this.nearbyPoiDataRepository.getAllNearbyPOI();
    }

    // GET NearbyPOI
    public LiveData<NearbyPOI> getNearbyPOI(long nearbyPoiId){
        return this.nearbyPoiDataRepository.getNearbyPOI(nearbyPoiId);
    }

    // CREATE NearbyPOI
    public long createNearbyPOI(NearbyPOI nearbyPOI){
        return this.nearbyPoiDataRepository.createNearbyPOI(nearbyPOI);
    }

    // UPDATE NearbyPOI
    public void updateNearbyPOI(NearbyPOI nearbyPOI){
        executor.execute(() -> this.nearbyPoiDataRepository.updateNearbyPOI(nearbyPOI));
    }

    // DELETE NearbyPOI
    public void deleteNearbyPOI(NearbyPOI nearbyPOI){
        executor.execute(() -> this.nearbyPoiDataRepository.deleteNearbyPOI(nearbyPOI));
    }

    // endregion

    // ---------------
    //  region FOR PropertyNearbyPoiJoin
    // ---------------

    // CREATE PropertyNearbyPoiJoin
    public long createPropertyNearbyPoiJoin(PropertyNearbyPoiJoin propertyNearbyPoiJoin){
        return this.propertyNearbyPoiJoinDataRepository.createPropertyNearbyPoiJoin(propertyNearbyPoiJoin);
    }

    public void deletePropertyNearbyPoiJoin(PropertyNearbyPoiJoin propertyNearbyPoiJoin){
        executor.execute(() -> {
            this.propertyNearbyPoiJoinDataRepository.deletePropertyNearbyPoiJoin(propertyNearbyPoiJoin);
        });
    }

    public void deletePropertyNearbyPoiJoinWithPropertyId(long propertyId){
        executor.execute(() -> {
            this.propertyNearbyPoiJoinDataRepository.deletePropertyNearbyPoiJoinWithPropertyId(propertyId);
        });
    }

    // GET ALL Property with a NearbyPOI
    public LiveData<List<Property>> getNearbyPoiForProperty(final long nearbyPOI){
        return this.propertyNearbyPoiJoinDataRepository.getPropertyForNearbyPoi(nearbyPOI);
    }

    // GET ALL NearbyPOI with a Property
    public LiveData<List<NearbyPOI>> getPropertyForNearbyPoi(long propertyId){
        return this.propertyNearbyPoiJoinDataRepository.getNearbyPoiForProperty(propertyId);
    }

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
    //  region FOR AGENT
    // ---------------

    // GET ALL AGENT
    public LiveData<List<Agent>> getAllAgent(){
        return this.agentDataRepository.getAllAgent();
    }

    // GET AGENT
    public LiveData<Agent> getAgent(long agentId){
        return this.agentDataRepository.getAgent(agentId);
    }

    // CREATE AGENT
    public void createAgent(Agent agent){
        executor.execute(() -> this.agentDataRepository.createAgent(agent));
    }

    // UPDATE AGENT
    public void updateAgent(Agent agent){
        executor.execute(() -> {
            this.agentDataRepository.updateAgent(agent);
        });
    }

    // DELETE AGENT
    public void deleteAgent(Agent agent){
        executor.execute(() -> {
            this.agentDataRepository.deleteAgent(agent);
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

    public LiveData<Address> getAddressWithPropertyId(long propertyId){
        return this.addressDataRepository.getAddressWithPropertyId(propertyId);
    }

    // CREATE ADDRESS
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

