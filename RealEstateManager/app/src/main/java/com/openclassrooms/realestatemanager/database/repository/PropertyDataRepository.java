package com.openclassrooms.realestatemanager.database.repository;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.model.Property;

import java.util.List;

public class PropertyDataRepository {

    private final PropertyDao propertyDao;

    public PropertyDataRepository(PropertyDao propertyDao){
        this.propertyDao = propertyDao;
    }

    // GET ALL PROPERTY
    public LiveData<List<Property>> getAllLiveDataProperty(){
        return this.propertyDao.getAllLiveDataProperty();
    }

    // GET ALL PROPERTY
    public List<Property> getAllProperty(){
        return this.propertyDao.getAllProperty();
    }

    // GET PROPERTIES WITH FILTERS
    public LiveData<List<Property>> searchProperties(SupportSQLiteQuery query){
        return this.propertyDao.searchProperties(query);
    }

    // GET THE AVERAGE OF PricePerSquareMeter OF city IN LIST
    public Cursor getListCityWithAveragePricePerSquareMeter(){
        return this.propertyDao.getListCityWithAveragePricePerSquareMeter();
    }

    // GET PROPERTY
    public LiveData<Property> getProperty(long propertyId){
        return this.propertyDao.getProperty(propertyId);
    }

    // CREATE
    public long createProperty(Property property){
        return this.propertyDao.createProperty(property);
    }

    // UPDATE
    public long updateProperty(Property property){
        return this.propertyDao.updateProperty(property);
    }

    // DELETE
    public long deleteProperty(Property property){
        return this.propertyDao.deleteProperty(property);
    }

}
