package com.openclassrooms.realestatemanager.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Address {

    @PrimaryKey(autoGenerate = true)
    private long id; //todo: enlerver l'id du constructor
    private int streetNumber;
    private String streetName;
    private String city;
    private String zipCode;
    private String country;

    public Address(long id, int streetNumber, String streetName, String city, String zipCode, String country) {
        this.id = id;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getId() {
        return id;
    }
}
