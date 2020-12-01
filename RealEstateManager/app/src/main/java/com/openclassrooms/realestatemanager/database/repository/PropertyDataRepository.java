package com.openclassrooms.realestatemanager.database.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.model.Property;

import java.util.List;

public class PropertyDataRepository {

    private final PropertyDao propertyDao;

    public PropertyDataRepository(PropertyDao propertyDao){
        this.propertyDao = propertyDao;
    }

    // GET ALL PROPERTY
    public LiveData<List<Property>> getAllProperty(){
        return this.propertyDao.getAllProperty();
    }

    // GET PROPERTY
    public LiveData<Property> getProperty(long propertyId){
        return this.propertyDao.getProperty(propertyId);
    }

    // CREATE
    public void createProperty(Property property){
        this.propertyDao.createProperty(property);
    }

    // UPDATE
    public void updateProperty(Property property){
        this.propertyDao.updateProperty(property);
    }

}
