package com.openclassrooms.realestatemanager.database.repository;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.database.dao.PropertyNearbyPoiJoinDao;
import com.openclassrooms.realestatemanager.model.NearbyPOI;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyNearbyPoiJoin;

import java.util.List;

public class PropertyNearbyPoiJoinDataRepository {

    private final PropertyNearbyPoiJoinDao propertyNearbyPoiJoinDao;

    public PropertyNearbyPoiJoinDataRepository(PropertyNearbyPoiJoinDao propertyNearbyPoiJoinDao){
        this.propertyNearbyPoiJoinDao = propertyNearbyPoiJoinDao;
    }

    // CREATE NearbyPOI
    public long createPropertyNearbyPoiJoin(PropertyNearbyPoiJoin propertyNearbyPoiJoin){
        return this.propertyNearbyPoiJoinDao.createPropertyNearbyPoiJoin(propertyNearbyPoiJoin);
    }

    // DELETE
    public long deletePropertyNearbyPoiJoin(PropertyNearbyPoiJoin propertyNearbyPoiJoin){
        return this.propertyNearbyPoiJoinDao.deletePropertyNearbyPoi(propertyNearbyPoiJoin);
    }

    // GET ALL NearbyPOI link with a Property
    public LiveData<List<Property> > getPropertyForNearbyPoi(final long nearbyId){
        return this.propertyNearbyPoiJoinDao.getPropertyForNearbyPoi(nearbyId);
    }

    // GET ALL Property link with a NearbyPOI
    public LiveData<List<NearbyPOI> > getNearbyPoiForProperty(final long propertyId){
        return this.propertyNearbyPoiJoinDao.getNearbyPoiForProperty(propertyId);
    }

}
