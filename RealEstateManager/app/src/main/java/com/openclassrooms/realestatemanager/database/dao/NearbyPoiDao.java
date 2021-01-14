package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.NearbyPOI;

import java.util.List;

@Dao
public interface NearbyPoiDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createNearbyPOI(NearbyPOI nearbyPOI);

    @Update
    int updateNearbyPOI(NearbyPOI nearbyPOI);

    @Delete
    int deleteNearbyPOI(NearbyPOI nearbyPOI);

    @Query("SELECT * FROM NearbyPOI WHERE id = :nearbyPoiId")
    LiveData<NearbyPOI> getNearbyPOI(long nearbyPoiId);

    @Query("SELECT * FROM NearbyPOI")
    LiveData<List<NearbyPOI>> getAllNearbyPOI();

}
