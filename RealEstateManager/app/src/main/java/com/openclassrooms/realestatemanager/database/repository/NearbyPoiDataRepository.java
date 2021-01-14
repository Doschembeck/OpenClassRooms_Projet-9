package com.openclassrooms.realestatemanager.database.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.NearbyPoiDao;
import com.openclassrooms.realestatemanager.model.NearbyPOI;

import java.util.List;

public class NearbyPoiDataRepository {

    private final NearbyPoiDao nearbyPoiDao;

    public NearbyPoiDataRepository(NearbyPoiDao nearbyPoiDao){
        this.nearbyPoiDao = nearbyPoiDao;
    }

    // GET ALL NearbyPOI
    public LiveData<List<NearbyPOI>> getAllNearbyPOI(){
        return this.nearbyPoiDao.getAllNearbyPOI();
    }

    // GET NearbyPOI
    public LiveData<NearbyPOI> getNearbyPOI(long nearbyPoiId){
        return this.nearbyPoiDao.getNearbyPOI(nearbyPoiId);
    }

    // CREATE
    public long createNearbyPOI(NearbyPOI nearbyPOI){
        return this.nearbyPoiDao.createNearbyPOI(nearbyPOI);
    }

    // UPDATE
    public long updateNearbyPOI(NearbyPOI nearbyPOI){
        return this.nearbyPoiDao.updateNearbyPOI(nearbyPOI);
    }

    // DELETE
    public long deleteNearbyPOI(NearbyPOI nearbyPOI){
        return this.nearbyPoiDao.deleteNearbyPOI(nearbyPOI);
    }

}
