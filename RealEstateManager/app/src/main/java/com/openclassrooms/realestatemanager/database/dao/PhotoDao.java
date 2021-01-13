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

import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;

import java.util.List;

@Dao
public interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createPhoto(Photo photo);

    @Update
    int updatePhoto(Photo photo);

    @Delete
    int deletePhoto(Photo photo);

    @Query("SELECT * FROM Photo WHERE id = :photoId")
    LiveData<Photo> getPhoto(long photoId);

    @Query("SELECT * FROM Photo WHERE propertyId = :propertyId")
    LiveData<List<Photo>> getAllPropertyPhotos(long propertyId);

}

