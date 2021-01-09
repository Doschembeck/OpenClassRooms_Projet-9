package com.openclassrooms.realestatemanager.database.dao;

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

    @Query("SELECT * FROM Property WHERE id = :propertyId")
    LiveData<Property> getProperty(long propertyId);

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getAllProperty();

    @RawQuery(observedEntities = Property.class)
    LiveData<List<Property>> searchProperties(SupportSQLiteQuery query);
}
