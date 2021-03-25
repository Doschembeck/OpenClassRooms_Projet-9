package com.openclassrooms.realestatemanager.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = {
            @ForeignKey(entity = Address.class,
                    parentColumns = "id",
                    childColumns = "address_id"
            ),
            @ForeignKey(entity = Agent.class,
                    parentColumns = "id",
                    childColumns = "agent_id"
            )
        })
public class Property implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "property_type_id", index = true)
    private int propertyTypeId; // appartement, loft, manoir, etc
    private Double price; // in dollars
    private Double pricePerSquareMeter;
    private float area; // La surface du bien (en m2)
    private int nbOfRooms;
    private int nbOfBedRooms;
    private String description; // The full description of the property;
    @ColumnInfo(name = "address_id", index = true)
    private long addressId;
    private Float rate;
    private String city;
    private String mainPictureUrl;
    private int nbOfPictures;
    @ColumnInfo(name = "agent_id", index = true)
    private long agentId; // The real estate agent in charge of this property.
    private Boolean isSold;
    private Date dateOfSale; // The date of sale of the property, if it has been sold;
    private Date createdAt;
    private Date updatedAt;

    public Property(long id, int propertyTypeId, Double price, Double pricePerSquareMeter, float area, int nbOfRooms, int nbOfBedRooms, String description, long addressId, Float rate, String city, String mainPictureUrl, int nbOfPictures, Long agentId, Date dateOfSale, Date createdAt, Date updatedAt) {
        this.id = id;
        this.propertyTypeId = propertyTypeId;
        this.price = price;
        this.pricePerSquareMeter = pricePerSquareMeter;
        this.area = area;
        this.nbOfRooms = nbOfRooms;
        this.nbOfBedRooms = nbOfBedRooms;
        this.description = description;
        this.addressId = addressId;
        this.rate = rate;
        this.city = city;
        this.mainPictureUrl = mainPictureUrl;
        this.nbOfPictures = nbOfPictures;
        this.agentId = agentId;
        this.isSold = dateOfSale.getTime() != 0;
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

    public long getAgentId() {
        return agentId;
    }

    public void setAgentId(long agentId) {
        this.agentId = agentId;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMainPictureUrl() {
        return mainPictureUrl;
    }

    public void setMainPictureUrl(String mainPictureUrl) {
        this.mainPictureUrl = mainPictureUrl;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Double getPricePerSquareMeter() {
        return pricePerSquareMeter;
    }

    public void setPricePerSquareMeter(Double pricePerSquareMeter) {
        this.pricePerSquareMeter = pricePerSquareMeter;
    }

    public int getNbOfPictures() {
        return nbOfPictures;
    }

    public void setNbOfPictures(int nbOfPictures) {
        this.nbOfPictures = nbOfPictures;
    }

//   region ---- PARECELABLE METHODS ---

    private Property(){

    };

    protected Property(Parcel in) {
        this.id = in.readLong();
        this.propertyTypeId = in.readInt();
        this.price = in.readDouble();
        this.pricePerSquareMeter = in.readDouble();;
        this.area = in.readFloat();;
        this.nbOfRooms = in.readInt();;
        this.nbOfBedRooms = in.readInt();;
        this.description = in.readString();;
        this.addressId = in.readLong();;
        this.rate = in.readFloat();;
        this.city = in.readString();;
        this.mainPictureUrl = in.readString();;
        this.nbOfPictures = in.readInt();
        this.agentId = in.readLong();
        this.dateOfSale = new Date(in.readLong());
        this.isSold = this.dateOfSale.getTime() != 0;
        this.createdAt = new Date(in.readLong());
        this.updatedAt = new Date(in.readLong());
    }

    public static final Creator<Property> CREATOR = new Creator<Property>() {
        @Override
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        @Override
        public Property[] newArray(int size) {
            return new Property[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(propertyTypeId);
        dest.writeDouble(price);
        dest.writeDouble(pricePerSquareMeter);
        dest.writeFloat(area);
        dest.writeInt(nbOfRooms);
        dest.writeInt(nbOfBedRooms);
        dest.writeString(description);
        dest.writeLong(addressId);
        dest.writeFloat(rate);
        dest.writeString(city);
        dest.writeString(mainPictureUrl);
        dest.writeInt(nbOfPictures);
        dest.writeLong(agentId);
        dest.writeLong(dateOfSale.getTime() != 0 ? dateOfSale.getTime() : 0);
        dest.writeLong(createdAt.getTime());
        dest.writeLong(updatedAt.getTime());
    }

    // --- UTILS ---
    public static Property fromContentValues(ContentValues values) {

        final Property property = new Property();
        if (values.containsKey("id")) property.setId(values.getAsLong("id"));
        if (values.containsKey("property_type_id")) property.setPropertyTypeId(values.getAsInteger("property_type_id"));
        if (values.containsKey("price")) property.setPrice(values.getAsDouble("price"));
        if (values.containsKey("pricePerSquareMeter")) property.setPricePerSquareMeter(values.getAsDouble("pricePerSquareMeter"));
        if (values.containsKey("area")) property.setArea(values.getAsFloat("area"));
        if (values.containsKey("nbOfRooms")) property.setNbOfRooms(values.getAsInteger("nbOfRooms"));
        if (values.containsKey("nbOfBedRooms")) property.setNbOfBedRooms(values.getAsInteger("nbOfBedRooms"));
        if (values.containsKey("description")) property.setDescription(values.getAsString("description"));
        if (values.containsKey("address_id")) property.setAddressId(values.getAsLong("address_id"));
        if (values.containsKey("rate")) property.setRate(values.getAsFloat("rate"));
        if (values.containsKey("city")) property.setCity(values.getAsString("city"));
        if (values.containsKey("mainPictureUrl")) property.setMainPictureUrl(values.getAsString("mainPictureUrl"));
        if (values.containsKey("nbOfPictures")) property.setNbOfPictures(values.getAsInteger("nbOfPictures"));
        if (values.containsKey("agent_id")) property.setAgentId(values.getAsLong("agent_id"));
        if (values.containsKey("isSold")) property.setSold(values.getAsBoolean("isSold"));
        if (values.containsKey("dateOfSale")) property.setDateOfSale(DateConverter.toDate(values.getAsLong("dateOfSale")));
        if (values.containsKey("createdAt")) property.setCreatedAt(DateConverter.toDate(values.getAsLong("createdAt")));
        if (values.containsKey("updatedAt")) property.setUpdatedAt(DateConverter.toDate(values.getAsLong("updatedAt")));

        return property;
    }

//    endregion
}
