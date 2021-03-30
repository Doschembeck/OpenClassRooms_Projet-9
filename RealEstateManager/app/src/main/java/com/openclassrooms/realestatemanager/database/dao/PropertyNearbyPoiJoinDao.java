package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.NearbyPOI;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyNearbyPoiJoin;

import java.util.List;

@Dao
public interface PropertyNearbyPoiJoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createPropertyNearbyPoiJoin(PropertyNearbyPoiJoin propertyNearbyPoiJoin);

    @Query("SELECT * FROM Property INNER JOIN property_nearbypoi_join ON property.id = property_nearbypoi_join.propertyId WHERE property_nearbypoi_join.nearbyPoiId = :nearbyId")
    LiveData<List<Property>> getPropertyForNearbyPoi(final long nearbyId);

    @Query("SELECT * FROM NearbyPOI INNER JOIN property_nearbypoi_join ON NearbyPOI.id = property_nearbypoi_join.nearbyPoiId WHERE property_nearbypoi_join.propertyId = :propertyId")
    LiveData<List<NearbyPOI>> getNearbyPoiForProperty(final long propertyId);

    @Delete
    int deletePropertyNearbyPoi(PropertyNearbyPoiJoin propertyForNearbyPoi);

    @Query("DELETE FROM property_nearbypoi_join WHERE propertyId = :propertyId")
    int deletePropertyNearbyPoiWithPropertyId(long propertyId);

}
