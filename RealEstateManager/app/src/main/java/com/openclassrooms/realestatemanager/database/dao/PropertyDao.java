package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.model.Property;

import java.util.List;

@Dao
public interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createProperty(Property property);

    @Update
    int updateProperty(Property property);

    @Delete
    int deleteProperty(Property property);

    @Query("DELETE FROM Property WHERE id = :propertyId")
    int deleteProperty(long propertyId);

    @Query("SELECT * FROM Property WHERE id = :propertyId")
    LiveData<Property> getProperty(long propertyId);

    @Query("SELECT * FROM Property WHERE id = :propertyId")
    Cursor getPropertyWithCursor(long propertyId);

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getAllLiveDataProperty();

    @Query("SELECT * FROM Property")
    List<Property> getAllProperty();

    @RawQuery(observedEntities = Property.class)
    LiveData<List<Property>> searchProperties(SupportSQLiteQuery query);

    // "SELECT city, AVG(pricePerSquareMeter) FROM property GROUP BY city"
    @Query("SELECT city , AVG(pricePerSquareMeter) FROM Property GROUP BY city")
    Cursor getListCityWithAveragePricePerSquareMeter();

}
