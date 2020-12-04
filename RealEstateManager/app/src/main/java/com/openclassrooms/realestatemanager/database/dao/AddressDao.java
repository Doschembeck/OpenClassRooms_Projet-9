package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Property;

@Dao
public interface AddressDao {

    @Query("SELECT * FROM Address WHERE id = :addressId")
    LiveData<Address> getAddress(long addressId);

    @Insert
    long createAddress(Address address);

    @Update
    int updateAddress(Address address);

    // 'SELECT id FROM Address WHERE id = LAST_INSERT_ID()'

}
