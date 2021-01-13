package com.openclassrooms.realestatemanager.database.repository;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.database.dao.PhotoDao;
import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;

import java.util.List;

public class PhotoDataRepository {

    private final PhotoDao photoDao;

    public PhotoDataRepository(PhotoDao photoDao){
        this.photoDao = photoDao;
    }

    // GET ALL PHOTOS
    public LiveData<List<Photo>> getAllPropertyPhotos(long propertyId){
        return this.photoDao.getAllPropertyPhotos(propertyId);
    }

    // GET PHOTO
    public LiveData<Photo> getPhoto(long photoId){
        return this.photoDao.getPhoto(photoId);
    }

    // CREATE
    public long createPhoto(Photo photo){
        return this.photoDao.createPhoto(photo);
    }

    // UPDATE
    public long updatePhoto(Photo photo){
        return this.photoDao.updatePhoto(photo);
    }

    // DELETE
    public long deletePhoto(Photo photo){
        return this.photoDao.deletePhoto(photo);
    }

}
