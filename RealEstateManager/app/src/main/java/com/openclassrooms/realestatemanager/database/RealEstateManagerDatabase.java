package com.openclassrooms.realestatemanager.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.database.dao.AddressDao;
import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;

import java.util.Arrays;
import java.util.List;

@Database(entities = {Property.class, Address.class}, version = 1, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    // SINGLETON
    private static volatile RealEstateManagerDatabase INSTANCE;

    // DAO
    public abstract PropertyDao propertyDao();
    public abstract AddressDao addressDao();

    // INSTANCE
    public static RealEstateManagerDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (RealEstateManagerDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RealEstateManagerDatabase.class, "REMDatabase2.db") //todo: supprimer "REMDatabase.db" puis le remettre
                            .addCallback(prepopulateDatabase())
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // ----

    private static Callback prepopulateDatabase(){
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
//
////                List<Long> photoList = Arrays.asList(Arrays.asList(1l,2l,3l);
//
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("id", 1);
//                contentValues.put("propertyTypeId", 1);
//                contentValues.put("price", 465213.0);
//                contentValues.put("area", 329);
//                contentValues.put("nbOfRooms", 7);
//                contentValues.put("nbOfBedRooms", 3);
//                contentValues.put("description", "Contenu de la description");
////                contentValues.put("photoIdList", photoList);
//                contentValues.put("addressId", 1);
////                contentValues.put("nearbyPOI", Arrays.asList("restaurant"));
//                contentValues.put("isSold", false);
//                contentValues.put("dateOfEntry", "21/10/2020");
//                contentValues.put("realEstateAgent", "thomas");
//                contentValues.put("createdAt", "21/10/2020");
//                contentValues.put("updatedAt", "21/10/2020");
//
//                db.insert("Property", OnConflictStrategy.IGNORE, contentValues);
            }
        };
    }


}
