package com.openclassrooms.realestatemanager.model;

public class Photo {

    //todo: créer la table dans la bdd
    private long id;
    private long propertyId;
    private String photo;
    private String photoDescription;

    public Photo(long id, long propertyId, String photo, String photoDescription) {
        this.id = id;
        this.propertyId = propertyId;
        this.photo = photo;
        this.photoDescription = photoDescription;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }
}
