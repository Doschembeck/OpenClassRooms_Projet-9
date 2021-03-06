package com.openclassrooms.realestatemanager.injections;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.database.repository.AddressDataRepository;
import com.openclassrooms.realestatemanager.database.repository.AgentDataRepository;
import com.openclassrooms.realestatemanager.database.repository.NearbyPoiDataRepository;
import com.openclassrooms.realestatemanager.database.repository.PhotoDataRepository;
import com.openclassrooms.realestatemanager.database.repository.PropertyDataRepository;
import com.openclassrooms.realestatemanager.database.repository.PropertyNearbyPoiJoinDataRepository;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final PropertyDataRepository propertyDataRepository;
    private final AddressDataRepository addressDataRepository;
    private final PhotoDataRepository photoDataRepository;
    private final AgentDataRepository agentDataRepository;
    private final NearbyPoiDataRepository nearbyPoiDataRepository;
    private final PropertyNearbyPoiJoinDataRepository propertyNearbyPoiJoinDataRepository;
    private final Executor executor;

    public ViewModelFactory(PropertyDataRepository propertyDataRepository, AddressDataRepository addressDataRepository, PhotoDataRepository photoDataRepository, AgentDataRepository agentDataRepository, NearbyPoiDataRepository nearbyPoiDataRepository, PropertyNearbyPoiJoinDataRepository propertyNearbyPoiJoinDataRepository, Executor executor) {
        this.propertyDataRepository = propertyDataRepository;
        this.addressDataRepository = addressDataRepository;
        this.photoDataRepository = photoDataRepository;
        this.agentDataRepository = agentDataRepository;
        this.nearbyPoiDataRepository = nearbyPoiDataRepository;
        this.propertyNearbyPoiJoinDataRepository = propertyNearbyPoiJoinDataRepository;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertyViewModel.class)) {
            return (T) new PropertyViewModel(propertyDataRepository, addressDataRepository, photoDataRepository, agentDataRepository, nearbyPoiDataRepository, propertyNearbyPoiJoinDataRepository, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
