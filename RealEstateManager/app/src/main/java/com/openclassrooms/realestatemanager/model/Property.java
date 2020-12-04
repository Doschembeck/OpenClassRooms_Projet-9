package com.openclassrooms.realestatemanager.model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(foreignKeys = @ForeignKey(entity = Address.class,
        parentColumns = "id",
        childColumns = "addressId",
        onDelete = ForeignKey.CASCADE))
public class Property {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private int propertyTypeId; // appartement, loft, manoir, etc
    private Double price; // in dollars
    private float area; // La surface du bien (en m2)
    private int nbOfRooms;
    private int nbOfBedRooms;
    private String description; // The full description of the property;
//    private List<Long> photoIdList;
    private long addressId;
//    private List<String> nearbyPOI; // Nearby points of interest (school, shops, park, etc.);
    private boolean isSold; // The status of the property (still available or sold);
    private String dateOfEntry; // The date of entry of the property on the market;
    private String realEstateAgent; // The real estate agent in charge of this property.
    private String createdAt;
    private String updatedAt;
    private String dateOfSale; // The date of sale of the property, if it has been sold;

    public Property(long id, int propertyTypeId, Double price, float area, int nbOfRooms, int nbOfBedRooms, String description, long addressId, boolean isSold, String dateOfEntry, String realEstateAgent, String createdAt, String updatedAt) {
        this.id = id;
        this.propertyTypeId = propertyTypeId;
        this.price = price;
        this.area = area;
        this.nbOfRooms = nbOfRooms;
        this.nbOfBedRooms = nbOfBedRooms;
        this.description = description;
        this.addressId = addressId;
        this.isSold = isSold;
        this.dateOfEntry = dateOfEntry;
        this.realEstateAgent = realEstateAgent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(int propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public int getNbOfRooms() {
        return nbOfRooms;
    }

    public void setNbOfRooms(int nbOfRooms) {
        this.nbOfRooms = nbOfRooms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        this.isSold = sold;
    }

    public String getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(String dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public String getRealEstateAgent() {
        return realEstateAgent;
    }

    public void setRealEstateAgent(String realEstateAgent) {
        this.realEstateAgent = realEstateAgent;
    }

    public String getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(String dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getNbOfBedRooms() {
        return nbOfBedRooms;
    }

    public void setNbOfBedRooms(int nbOfBedRooms) {
        this.nbOfBedRooms = nbOfBedRooms;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
