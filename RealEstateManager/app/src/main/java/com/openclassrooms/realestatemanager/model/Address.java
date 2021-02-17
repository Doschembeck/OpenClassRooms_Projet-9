package com.openclassrooms.realestatemanager.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

@Entity
public class Address {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String streetNumber;
    private String streetName;
    private String city;
    private String zipCode;
    private String country;
    private double latitude;
    private double longitude;

    public Address(long id, String streetNumber, String streetName, String city, String zipCode, String country, double latitude, double longitude) {
        this.id = id;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // region  --- GETTERS / SETTERS --
    //

    public void setId(long id) {
        this.id = id;
    }
    // -

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    //endregion

    public LatLng getLatLng(){
        return new LatLng(latitude, longitude);
    }

    public String getCompleteAddress(){
        return streetNumber  + " " + streetName + ", " + city + " " + zipCode + ", " + country;
    }
}
