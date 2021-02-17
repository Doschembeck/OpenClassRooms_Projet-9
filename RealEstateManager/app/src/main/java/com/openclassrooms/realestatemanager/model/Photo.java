package com.openclassrooms.realestatemanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Property.class,
        parentColumns = "id",
        childColumns = "property_id",
        onDelete = ForeignKey.CASCADE))
public class Photo {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "property_id", index = true)
    private long propertyId;
    private String urlPicture;
    private String photoDescription;

    public Photo(long id, long propertyId, String urlPicture, String photoDescription) {
        this.id = id;
        this.propertyId = propertyId;
        this.urlPicture = urlPicture;
        this.photoDescription = photoDescription;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
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
