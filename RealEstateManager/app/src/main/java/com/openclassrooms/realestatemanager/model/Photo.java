package com.openclassrooms.realestatemanager.model;

public class Photo {
    private String photo;
    private String photoDescription;

    public Photo(String photo, String photoDescription) {
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
}
