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
    private long createdAtMin;
    private long createdAtMax;
    private long dateOfSaleMin;
    private long dateOfSaleMax;
    private int nbOfPicturesMin;
    private int nbOfPicturesMax;
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
        this.createdAtMin = 0;
        this.createdAtMax = 0;
        this.dateOfSaleMin = 0;
        this.dateOfSaleMax = 0;
        this.nbOfPicturesMin = 0;
        this.nbOfPicturesMax = 0;
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
        createdAtMin = in.readLong();
        createdAtMax = in.readLong();
        dateOfSaleMin = in.readLong();
        dateOfSaleMax = in.readLong();
        nbOfPicturesMin = in.readInt();
        nbOfPicturesMax = in.readInt();
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
        dest.writeLong(createdAtMin);
        dest.writeLong(createdAtMax);
        dest.writeLong(dateOfSaleMin);
        dest.writeLong(dateOfSaleMax);
        dest.writeInt(nbOfPicturesMin);
        dest.writeInt(nbOfPicturesMax);
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
            params += tableName + " > " + min;
        }

        return params;
    }

    public SimpleSQLiteQuery getParamsFormatted(){

        boolean isFirstParam = true;
        String params = "SELECT * FROM " + "Property";

        String priceQuery = queryMinMax(isFirstParam, "price", getPriceMin(), getPriceMax());
        params += priceQuery;
        isFirstParam = priceQuery.equals("");

        String nbOfRoomsQuery = queryMinMax(isFirstParam, "nbOfRooms", getNbOfRoomsMin(), getNbOfRoomsMax());
        params += nbOfRoomsQuery;
        isFirstParam = nbOfRoomsQuery.equals("");

        String nbOfBedRoomsQuery = queryMinMax(isFirstParam, "nbOfBedRooms", getNbOfBedRoomsMin(), getNbOfBedRoomsMax());
        params += nbOfBedRoomsQuery;
        isFirstParam = nbOfBedRoomsQuery.equals("");

        String areaQuery = queryMinMax(isFirstParam, "area", getAreaMin(), getAreaMax());
        params += areaQuery;
        isFirstParam = areaQuery.equals("");

        String createdAtQuery = queryMinMax(isFirstParam, "createdAt", getCreatedAtMin() - 1, getCreatedAtMax());
        params += createdAtQuery;
        isFirstParam = createdAtQuery.equals("");

        if (getSold() == 1){
            String dateOfSaleQuery = queryMinMax(isFirstParam, "dateOfSale", getDateOfSaleMin() - 1, getDateOfSaleMax());
            params += dateOfSaleQuery;
            isFirstParam = dateOfSaleQuery.equals("");
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


        //todo lancer une requete dans la table photos pour compter les photos de cette id
        if(getNbOfPicturesMax() > 0)
        {
            params += getIsFirstParams(isFirstParam);
            params += tableName + " BETWEEN " + getNbOfPicturesMin() + " AND " + getNbOfPicturesMax();
            if (isFirstParam) isFirstParam = false;
        }
        else if(getNbOfPicturesMin() > 0)
        {
            params += getIsFirstParams(isFirstParam);
            params += tableName + " > " + getNbOfPicturesMin();
            if (isFirstParam) isFirstParam = false;
        }





//        if(city != null){
//            params += " city = '" + city + "'";
//        }

        Log.i("TAG1", "getParamsFormatted: " + params);
        return new SimpleSQLiteQuery(params);
    }

}