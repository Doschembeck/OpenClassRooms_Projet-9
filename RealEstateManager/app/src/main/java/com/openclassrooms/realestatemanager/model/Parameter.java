package com.openclassrooms.realestatemanager.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.utils.Constants;

import java.util.ArrayList;

public class Parameter implements Parcelable {

    //todo: rajouter l'address et la PropertyType

    private int typeOfProperty;
    private int priceMin;
    private int priceMax;
    private String realEstateAgent;
    private int nbOfRoomsMin;
    private int nbOfRoomsMax;
    private int nbOfBedRoomsMin;
    private int nbOfBedRoomsMax;
    private int areaMin;
    private int areaMax;
    private long createdAtMin;
    private long createdAtMax;
    private long dateOfSaleMin;
    private long dateOfSaleMax;
    private int nbOfPicturesMin;
    private int nbOfPicturesMax;
    private byte isSold; // 0 = display only unSold | 1 = display only Sold | 2 = display all
    private long[] listNearbyPOI;
    private Constants.OrderBy orderBy;
    private Constants.SortDirection sortDirection;
    private Double latitude;
    private Double longitude;
    private int distanceAddressMin;
    private int distanceAddressMax;

    //    private String city;

    public Parameter() {
        this.typeOfProperty = 999999999;
        this.priceMin = 0;
        this.priceMax = 999999999;
        this.nbOfRoomsMin = 0;
        this.nbOfRoomsMax = 999999999;
        this.nbOfBedRoomsMin = 0;
        this.nbOfBedRoomsMax = 999999999;
        this.areaMin = 0;
        this.areaMax = 999999999;
        this.createdAtMin = 0;
        this.createdAtMax = 0;
        this.dateOfSaleMin = 0;
        this.dateOfSaleMax = 0;
        this.nbOfPicturesMin = 0;
        this.nbOfPicturesMax = 999999999;
        this.isSold = 2;
        this.orderBy = Constants.OrderBy.MARKETING_DATE;
        this.sortDirection = Constants.SortDirection.DESCENDANT;
        this.latitude = 999999999.0;
        this.longitude = 999999999.0;
        this.distanceAddressMin = 0;
        this.distanceAddressMax = 999999999;
    }

//   region ---- PARECELABLE METHODS ---

    protected Parameter(Parcel in) {
        this.typeOfProperty = in.readInt();
        this.priceMin = in.readInt();
        this.priceMax = in.readInt();
        this.realEstateAgent = in.readString();
        this.nbOfRoomsMin = in.readInt();
        this.nbOfRoomsMax = in.readInt();
        this.nbOfBedRoomsMin = in.readInt();
        this.nbOfBedRoomsMax = in.readInt();
        this.areaMin = in.readInt();
        this.areaMax = in.readInt();
        this.createdAtMin = in.readLong();
        this.createdAtMax = in.readLong();
        this.dateOfSaleMin = in.readLong();
        this.dateOfSaleMax = in.readLong();
        this.nbOfPicturesMin = in.readInt();
        this.nbOfPicturesMax = in.readInt();
        this.isSold = in.readByte();
        this.listNearbyPOI = in.createLongArray();
        this.orderBy = Constants.OrderBy.valueOf(in.readString());
        this.sortDirection = Constants.SortDirection.valueOf(in.readString());
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.distanceAddressMin = in.readInt();
        this.distanceAddressMax = in.readInt();
    }

    public static final Creator<Parameter> CREATOR = new Creator<Parameter>() {
        @Override
        public Parameter createFromParcel(Parcel in) {
            return new Parameter(in);
        }

        @Override
        public Parameter[] newArray(int size) {
            return new Parameter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.typeOfProperty);
        dest.writeInt(this.priceMin);
        dest.writeInt(this.priceMax);
        dest.writeString(this.realEstateAgent);
        dest.writeInt(this.nbOfRoomsMin);
        dest.writeInt(this.nbOfRoomsMax);
        dest.writeInt(this.nbOfBedRoomsMin);
        dest.writeInt(this.nbOfBedRoomsMax);
        dest.writeInt(this.areaMin);
        dest.writeInt(this.areaMax);
        dest.writeLong(this.createdAtMin);
        dest.writeLong(this.createdAtMax);
        dest.writeLong(this.dateOfSaleMin);
        dest.writeLong(this.dateOfSaleMax);
        dest.writeInt(this.nbOfPicturesMin);
        dest.writeInt(this.nbOfPicturesMax);
        dest.writeByte(this.isSold);
        dest.writeLongArray(this.listNearbyPOI);
        dest.writeString(this.orderBy.name());
        dest.writeString(this.sortDirection.name());
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.distanceAddressMin);
        dest.writeInt(this.distanceAddressMax);
    }

//    endregion

//    region  --- GETTERS / SETTERS ---

    public int getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public int getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(int priceMax) {
        this.priceMax = priceMax;
    }

    public String getRealEstateAgent() {
        return realEstateAgent;
    }

    public void setRealEstateAgent(String realEstateAgent) {
        this.realEstateAgent = realEstateAgent;
    }

    public int getNbOfRoomsMax() {
        return nbOfRoomsMax;
    }

    public void setNbOfRoomsMax(int nbOfRoomsMax) {
        this.nbOfRoomsMax = nbOfRoomsMax;
    }

    public int getNbOfRoomsMin() {
        return nbOfRoomsMin;
    }

    public void setNbOfRoomsMin(int nbOfRoomsMin) {
        this.nbOfRoomsMin = nbOfRoomsMin;
    }

    public int getNbOfBedRoomsMax() {
        return nbOfBedRoomsMax;
    }

    public void setNbOfBedRoomsMax(int nbOfBedRoomsMax) {
        this.nbOfBedRoomsMax = nbOfBedRoomsMax;
    }

    public int getNbOfBedRoomsMin() {
        return nbOfBedRoomsMin;
    }

    public void setNbOfBedRoomsMin(int nbOfBedRoomsMin) {
        this.nbOfBedRoomsMin = nbOfBedRoomsMin;
    }

    public int getAreaMin() {
        return areaMin;
    }

    public void setAreaMin(int areaMin) {
        this.areaMin = areaMin;
    }

    public int getAreaMax() {
        return areaMax;
    }

    public void setAreaMax(int areaMax) {
        this.areaMax = areaMax;
    }

    public byte getSold() {
        return isSold;
    }

    public void setSold(byte sold) {
        isSold = sold;
    }

    public long getCreatedAtMin() {
        return createdAtMin;
    }

    public void setCreatedAtMin(long createdAtMin) {
        this.createdAtMin = createdAtMin;
    }

    public long getCreatedAtMax() {
        return createdAtMax;
    }

    public void setCreatedAtMax(long createdAtMax) {
        this.createdAtMax = createdAtMax;
    }

    public long getDateOfSaleMin() {
        return dateOfSaleMin;
    }

    public void setDateOfSaleMin(long dateOfSaleMin) {
        this.dateOfSaleMin = dateOfSaleMin;
    }

    public long getDateOfSaleMax() {
        return dateOfSaleMax;
    }

    public void setDateOfSaleMax(long dataOfSaleMax) {
        this.dateOfSaleMax = dataOfSaleMax;
    }

    public int getNbOfPicturesMin() {
        return nbOfPicturesMin;
    }

    public void setNbOfPicturesMin(int nbOfPicturesMin) {
        this.nbOfPicturesMin = nbOfPicturesMin;
    }

    public int getNbOfPicturesMax() {
        return nbOfPicturesMax;
    }

    public void setNbOfPicturesMax(int nbOfPicturesMax) {
        this.nbOfPicturesMax = nbOfPicturesMax;
    }

    public long[] getListNearbyPOI() {
        return listNearbyPOI;
    }

    public void setListNearbyPOI(long[] listNearbyPOI) {
        this.listNearbyPOI = listNearbyPOI;
    }

    public Constants.OrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Constants.OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public Constants.SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Constants.SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public int getTypeOfProperty() {
        return typeOfProperty;
    }

    public void setTypeOfProperty(int typeOfProperty) {
        this.typeOfProperty = typeOfProperty;
    }

    //    endregion

    private String getIsFirstParams(Boolean isFirstParam){
        return isFirstParam ? " WHERE " : " AND ";
    }

    private String queryMinMax(Boolean isFirstParam, String tableName, long min, long max){
        String params = "";

        if(max > 0)
        {
            params += getIsFirstParams(isFirstParam);
            params += tableName + " BETWEEN " + min + " AND " + max;
        }
        else if(min > 0)
        {
            params += getIsFirstParams(isFirstParam);
            params += tableName + " >= " + min;
        }

        return params;
    }

    private String generateCharacterWithInt(int number){
        //todo bloqué a 26 NearbyPOI
//        String result = "";
        String[] alphabet = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q",
                "r","s","t","u","v","w","x","y","z"};

//        int multipleOfAlphabet = number / alphabet.length;
//
//        // 0 < number < 26 = un caractere de a à z | ex: 5 = e
//        // 27 < number < 52 = 2 caractere le premier a et le deuxieme entre a et z | ex: 38 = al | a + number modulo 26
//        // 52 < number < 78 = 2 caractere le pemier b et le deuxieme entre a et z | ex: 60 = bh (2 et 8) | b + number modulo 26
//
//        for (int nbOfLetter = 0; nbOfLetter <= multipleOfAlphabet; nbOfLetter++){
//            result += alphabet[number % alphabet.length];
//        }
//
//        // 1
//        int counter = 0;
//        int puissanceMax = 1;
//        while(puissanceMax < number){
//            puissanceMax *= 26;
//            counter++;
//        }

        // 1- nombre de lettre = nombre de puissance max possible
        // 2- index de chaque lettre = number / 26
        // 3- index de la derniere lettre = number modulo 26

        return alphabet[number];
    }

    public SimpleSQLiteQuery getParamsFormatted(){

        boolean isFirstParam = true;
        String params = "SELECT * FROM " + "Property";

        // ===== JOINTURES =====

        //todo: je pense qu'il y a mieu a faire
        if (getListNearbyPOI() != null){
            for (int i = 0; i < getListNearbyPOI().length; i++){
                String asName = generateCharacterWithInt(i);
                params += " INNER JOIN property_nearbypoi_join AS " + asName + " ON " + asName + ".propertyId = Property.id AND " + asName + ".nearbyPoiId = " + getListNearbyPOI()[i];
            }
        }

        if (typeOfProperty != 999999999){
            params += getIsFirstParams(isFirstParam);
            params += "property_type_id" + " = " + this.typeOfProperty;
            if (isFirstParam) isFirstParam = false;
        }

        String nbOfPicturesQuery = queryMinMax(isFirstParam, "nbOfPictures", getNbOfPicturesMin(), getNbOfPicturesMax());
        params += nbOfPicturesQuery;
        if (!nbOfPicturesQuery.equals("")) isFirstParam = false;

        String priceQuery = queryMinMax(isFirstParam, "price", getPriceMin(), getPriceMax());
        params += priceQuery;
        if (!priceQuery.equals("")) isFirstParam = false;

        String nbOfRoomsQuery = queryMinMax(isFirstParam, "nbOfRooms", getNbOfRoomsMin(), getNbOfRoomsMax());
        params += nbOfRoomsQuery;
        if (!nbOfRoomsQuery.equals("")) isFirstParam = false;

        String nbOfBedRoomsQuery = queryMinMax(isFirstParam, "nbOfBedRooms", getNbOfBedRoomsMin(), getNbOfBedRoomsMax());
        params += nbOfBedRoomsQuery;
        if (!nbOfBedRoomsQuery.equals("")) isFirstParam = false;

        String areaQuery = queryMinMax(isFirstParam, "area", getAreaMin(), getAreaMax());
        params += areaQuery;
        if (!areaQuery.equals("")) isFirstParam = false;

        String createdAtQuery = queryMinMax(isFirstParam, "createdAt", getCreatedAtMin() - 1, getCreatedAtMax());
        params += createdAtQuery;
        if (!createdAtQuery.equals("")) isFirstParam = false;

        if (getSold() == 1){
            String dateOfSaleQuery = queryMinMax(isFirstParam, "dateOfSale", getDateOfSaleMin() - 1, getDateOfSaleMax());
            params += dateOfSaleQuery;
            if (!dateOfSaleQuery.equals("")) isFirstParam = false;
        }

        if(getSold() != 2) {
            params += getIsFirstParams(isFirstParam);
            params += "isSold" + " = " + getSold();

            if (isFirstParam) isFirstParam = false;
        }

        if(getRealEstateAgent() != null) {
            params += getIsFirstParams(isFirstParam);
            params += "agent_id" + " = '" + getRealEstateAgent() + "'";

            if (isFirstParam) isFirstParam = false;
        }


        // === ORDER BY ===
        if (this.sortDirection != null && this.orderBy != null){
            params += " ORDER BY ";

            switch (this.orderBy){

                case PRICE:
                    params += "price";
                    break;

                case NB_OF_ROOMS:
                    params += "nbOfRooms";
                    break;

                case NB_OF_BEDROOMS:
                    params += "nbOfBedrooms";
                    break;

                case AREA:
                    params += "area";
                    break;

                case MARKETING_DATE:
                    params += "createdAt";
                    break;

                case DATE_OF_SALE:
                    params += "dateOfSale";
                    break;
            }

            switch (this.sortDirection){
                case ASCENDANT:
                    params += " ASC";
                    break;
                case DESCENDANT:
                    params += " DESC";
                    break;
            }

        }



        Log.i("TAG1", "getParamsFormatted: " + params);
        return new SimpleSQLiteQuery(params);
    }

    public int getDistanceAddressMin() {
        return distanceAddressMin;
    }

    public void setDistanceAddressMin(int distanceAddressMin) {
        this.distanceAddressMin = distanceAddressMin;
    }

    public int getDistanceAddressMax() {
        return distanceAddressMax;
    }

    public void setDistanceAddressMax(int distanceAddressMax) {
        this.distanceAddressMax = distanceAddressMax;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}