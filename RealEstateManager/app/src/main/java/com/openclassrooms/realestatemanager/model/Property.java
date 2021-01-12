package com.openclassrooms.realestatemanager.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;
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
    private List<String> NearbyPOI;
    private String photoUrlList; // todo; créer un objet special une description dois etre associé
    private long addressId;
    private String realEstateAgent; // The real estate agent in charge of this property.
    private Boolean isSold;
    private Date dateOfSale; // The date of sale of the property, if it has been sold;
    private Date createdAt;
    private Date updatedAt;

    public Property(long id, int propertyTypeId, Double price, float area, int nbOfRooms, int nbOfBedRooms, String description, List<String> nearbyPOI, String photoUrlList, long addressId, String realEstateAgent, Date dateOfSale, Date createdAt, Date updatedAt) {
        this.id = id;
        this.propertyTypeId = propertyTypeId;
        this.price = price;
        this.area = area;
        this.nbOfRooms = nbOfRooms;
        this.nbOfBedRooms = nbOfBedRooms;
        this.description = description;
        NearbyPOI = nearbyPOI;
        this.photoUrlList = photoUrlList;
        this.addressId = addressId;
        this.realEstateAgent = realEstateAgent;
        this.isSold = dateOfSale != null;
        this.dateOfSale = dateOfSale;
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

    public String getRealEstateAgent() {
        return realEstateAgent;
    }

    public void setRealEstateAgent(String realEstateAgent) {
        this.realEstateAgent = realEstateAgent;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
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

    public String getPhotoUrlList() {
        return photoUrlList;
    }

    public void setPhotoUrlList(String photoUrlList) {
        this.photoUrlList = photoUrlList;
    }

    public Date getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(Date dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public Boolean isSold(){
        return this.isSold;
    }

    public void setSold(Boolean sold) {
        isSold = sold;
    }

    public List<String> getNearbyPOI() {
        return NearbyPOI;
    }

    public void setNearbyPOI(List<String> nearbyPOI) {
        NearbyPOI = nearbyPOI;
    }
}
