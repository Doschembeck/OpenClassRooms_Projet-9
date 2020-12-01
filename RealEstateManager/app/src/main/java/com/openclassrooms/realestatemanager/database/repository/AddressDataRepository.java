package com.openclassrooms.realestatemanager.database.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.AddressDao;
import com.openclassrooms.realestatemanager.model.Address;

public class AddressDataRepository {

    private final AddressDao addressDao;

    public AddressDataRepository(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    // GET ADDRESS
    public LiveData<Address> getAddress(long addressId){
        return this.addressDao.getAddress(addressId);
    }

    // CREATE
    public void createAddress(Address address){
        this.addressDao.createAddress(address);
    }

    // UPDATE
    public void updateAddress(Address address){
        this.addressDao.updateAddress(address);
    }
}
