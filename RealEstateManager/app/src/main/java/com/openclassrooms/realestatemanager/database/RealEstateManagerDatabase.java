package com.openclassrooms.realestatemanager.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.database.dao.AddressDao;
import com.openclassrooms.realestatemanager.database.dao.AgentDao;
import com.openclassrooms.realestatemanager.database.dao.NearbyPoiDao;
import com.openclassrooms.realestatemanager.database.dao.PhotoDao;
import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.database.dao.PropertyNearbyPoiJoinDao;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.DateConverter;
import com.openclassrooms.realestatemanager.model.NearbyPOI;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyNearbyPoiJoin;

import java.util.Arrays;
import java.util.List;

@Database(entities = {Property.class, Address.class, Photo.class, Agent.class, NearbyPOI.class, PropertyNearbyPoiJoin.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    // SINGLETON
    private static volatile RealEstateManagerDatabase INSTANCE;

    // DAO
    public abstract PropertyDao propertyDao();
    public abstract AddressDao addressDao();
    public abstract PhotoDao PhotoDao();
    public abstract AgentDao AgentDao();
    public abstract NearbyPoiDao NearbyPoiDao();
    public abstract PropertyNearbyPoiJoinDao PropertyNearbyPoiJoinDao();

    // INSTANCE
    public static RealEstateManagerDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (RealEstateManagerDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RealEstateManagerDatabase.class, "REMDatabase.db") //todo: supprimer "REMDatabase.db" / "REMDatabase2.db" puis le remettre
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
