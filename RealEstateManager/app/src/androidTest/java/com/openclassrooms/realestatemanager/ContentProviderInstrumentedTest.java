package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.provider.PropertyContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ContentProviderInstrumentedTest {

    // FOR DATA
    private ContentResolver mContentResolver;
    private RealEstateManagerDatabase database;

    // DATA SET FOR TEST
    private static long PROPERTY_ID = 999999;
    private static long AGENT_ID = 999998;

    @Before
    public void setUp() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getContext().getContentResolver();
    }

    @Test
    public void insertAndGetItem() {
        // BEFORE : Adding demo item
        final Uri userUri = mContentResolver.insert(PropertyContentProvider.URI_PROPERTY, generateProperty());
        // TEST
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_PROPERTY, PROPERTY_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        //assertThat(cursor.moveToNext(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("description")), is("Visite cet endroit de rêve !!"));
    }

    // ---

    private ContentValues generateProperty(){
        final ContentValues values = new ContentValues();

        values.put("id", "999999");
        values.put("property_type_id", "1");
        values.put("price", "150000");
        values.put("pricePerSquareMeter", "150");
        values.put("area", "180");
        values.put("nbOfRooms", "4");
        values.put("nbOfBedRooms", "1");
        values.put("description", "Visite cet endroit de rêve !!");
        values.put("rate", "1");
        values.put("city", "Lyon");
        values.put("mainPictureUrl", "");
        values.put("nbOfPictures", "1");
        values.put("agent_id", "1");
        values.put("isSold", "true");
        values.put("dateOfSale", "0");
        values.put("createdAt", "0");
        values.put("updatedAt", "0");

        return values;
    }

}
