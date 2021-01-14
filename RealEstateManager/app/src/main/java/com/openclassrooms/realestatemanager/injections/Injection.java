package com.openclassrooms.realestatemanager.injections;

import android.content.Context;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.database.repository.AddressDataRepository;
import com.openclassrooms.realestatemanager.database.repository.AgentDataRepository;
import com.openclassrooms.realestatemanager.database.repository.NearbyPoiDataRepository;
import com.openclassrooms.realestatemanager.database.repository.PhotoDataRepository;
import com.openclassrooms.realestatemanager.database.repository.PropertyDataRepository;
import com.openclassrooms.realestatemanager.database.repository.PropertyNearbyPoiJoinDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static PropertyDataRepository providePropertyDataRepository(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new PropertyDataRepository(database.propertyDao());
    }

    public static AddressDataRepository provideAddressDataRepository(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new AddressDataRepository(database.addressDao());
    }

    public static PhotoDataRepository providePhotoDataRepository(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new PhotoDataRepository(database.PhotoDao());
    }

    public static AgentDataRepository provideAgentDataRepository(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new AgentDataRepository(database.AgentDao());
    }

    public static NearbyPoiDataRepository provideNearbyPoiDataRepository(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new NearbyPoiDataRepository(database.NearbyPoiDao());
    }

    public static PropertyNearbyPoiJoinDataRepository providePropertyNearbyPoiJoinDataRepository(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new PropertyNearbyPoiJoinDataRepository(database.PropertyNearbyPoiJoinDao());
    }

    public static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        PropertyDataRepository dataSourceItem = providePropertyDataRepository(context);
        AddressDataRepository dataSourceUser = provideAddressDataRepository(context);
        PhotoDataRepository dataSourcePhoto = providePhotoDataRepository(context);
        AgentDataRepository dataSourceAgent = provideAgentDataRepository(context);
        NearbyPoiDataRepository nearbyPoiSourceAgent = provideNearbyPoiDataRepository(context);
        PropertyNearbyPoiJoinDataRepository PropertyNearbyPoiJoinSourceAgent = providePropertyNearbyPoiJoinDataRepository(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceItem, dataSourceUser, dataSourcePhoto, dataSourceAgent, nearbyPoiSourceAgent, PropertyNearbyPoiJoinSourceAgent, executor);
    }
}
