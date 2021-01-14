package com.openclassrooms.realestatemanager.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class Agent {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String firstname;
    private String lastname;
    private String urlPicture;


    public Agent(long id, String firstname, String lastname, String urlPicture) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.urlPicture = urlPicture;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }
}
