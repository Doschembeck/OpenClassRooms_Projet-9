package com.openclassrooms.realestatemanager.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.sqlite.db.SimpleSQLiteQuery;

public class Parameter implements Parcelable {

    //todo: rajouter l'address et la PropertyType

    private int priceMin;
    private int priceMax;
    private String realEstateAgent;
    private int nbOfRoomsMin;
    private int nbOfRoomsMax;
    private int nbOfBedRoomsMin;
    private int nbOfBedRoomsMax;
    private int areaMin;
    private int areaMax;
    private byte isSold; // 0 = display only unSold | 1 = display only Sold | 2 = display all

    //    private String city;

    public Parameter() {
        this.priceMin = 0;
        this.priceMax = 0;
        this.nbOfRoomsMin = 0;
        this.nbOfRoomsMax = 0;
        this.nbOfBedRoomsMin = 0;
        this.nbOfBedRoomsMax = 0;
        this.areaMin = 0;
        this.areaMax = 0;
        this.isSold = 2;
    }

//   region ---- PARECELABLE METHODS ---

    protected Parameter(Parcel in) {
        priceMin = in.readInt();
        priceMax = in.readInt();
        realEstateAgent = in.readString();
        nbOfRoomsMin = in.readInt();
        nbOfRoomsMax = in.readInt();
        nbOfBedRoomsMin = in.readInt();
        nbOfBedRoomsMax = in.readInt();
        areaMin = in.readInt();
        areaMax = in.readInt();
        isSold = in.readByte();
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
        //todo ajouter les attributs rajouter en haut !

        dest.writeInt(priceMin);
        dest.writeInt(priceMax);
        dest.writeString(realEstateAgent);
        dest.writeInt(nbOfRoomsMin);
        dest.writeInt(nbOfRoomsMax);
        dest.writeInt(nbOfBedRoomsMin);
        dest.writeInt(nbOfBedRoomsMax);
        dest.writeInt(areaMin);
        dest.writeInt(areaMax);
        dest.writeByte(isSold);
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

//    endregion

    public SimpleSQLiteQuery getParamsFormatted(){

        String params = "SELECT * FROM Property";

        // Si aucun des attributs n'a été changer alors on recupere toutes les properties
        if (getAreaMin() == 0 && getAreaMax() == 0 &&
                getNbOfRoomsMin() == 0 && getNbOfRoomsMax() == 0 &&
                getSold() == 2 &&
                getNbOfBedRoomsMin() == 0 && getNbOfBedRoomsMax() == 0 &&
                getPriceMin() == 0 && getPriceMax() == 0 &&
                getRealEstateAgent() == null)
        {
            return new SimpleSQLiteQuery(params);
        }

        Boolean isFirstParam = true;
        params += " WHERE ";

        if(getPriceMax() > 0)
        {
            if (isFirstParam) isFirstParam = false; else  params += " AND ";
            params += "price BETWEEN " + getPriceMin() + " AND " + getPriceMax();
        }
        else if(priceMin > 0)
        {
            if (isFirstParam) isFirstParam = false; else  params += " AND ";
            params += "price > " + getPriceMin();
        }

        if(getRealEstateAgent() != null) {
            if (isFirstParam) isFirstParam = false; else  params += " AND ";
            params += "realEstateAgent = '" + getRealEstateAgent() + "'";
        }

        if(getNbOfRoomsMax() > 0)
        {
            if (isFirstParam) isFirstParam = false; else  params += " AND ";
            params += "nbOfRooms BETWEEN " + getNbOfRoomsMin() + " AND " + getNbOfRoomsMax();
        }
        else if(getNbOfRoomsMin() > 0)
        {
            if (isFirstParam) isFirstParam = false; else  params += " AND ";
            params += "nbOfRooms > " + getNbOfRoomsMin();
        }

        if(getNbOfBedRoomsMax() > 0)
        {
            if (isFirstParam) isFirstParam = false; else  params += " AND ";
            params += "nbOfBedRooms BETWEEN " + getNbOfBedRoomsMin() + " AND " + getNbOfBedRoomsMax();
        }
        else if(getNbOfBedRoomsMin() > 0)
        {
            if (isFirstParam) isFirstParam = false; else  params += " AND ";
            params += "nbOfBedRooms > " + getNbOfBedRoomsMin();
        }

//        if(city != null){
//            params += " city = '" + city + "'";
//        }

        if(getAreaMax() > 0)
        {
            if (isFirstParam) isFirstParam = false; else  params += " AND ";
            params += "area BETWEEN " + getAreaMin() + " AND " + getAreaMax();
        }
        else if(getAreaMin() > 0)
        {
            if (isFirstParam) isFirstParam = false; else  params += " AND ";
            params += "area > " + getAreaMin();
        }

        //todo: isSold est formaté comme ceci : (true, false)
        if(getSold() != 2) {
            if (isFirstParam) isFirstParam = false; else  params += " AND ";

            params += "isSold = " + getSold();

        }else {
            Log.e("TAG1", "getParamsFormatted: NO SOLD !");
        }

        Log.i("TAG1", "getParamsFormatted: " + params);
        return new SimpleSQLiteQuery(params);
    }

}