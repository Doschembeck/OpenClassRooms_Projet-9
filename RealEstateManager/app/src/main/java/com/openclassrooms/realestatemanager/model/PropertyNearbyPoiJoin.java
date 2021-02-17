package com.openclassrooms.realestatemanager.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "property_nearbypoi_join",
        primaryKeys = { "propertyId", "nearbyPoiId" },
        foreignKeys = {
            @ForeignKey(entity = Property.class,
                parentColumns = "id",
                childColumns = "propertyId",
                onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(entity = NearbyPOI.class,
                parentColumns = "id",
                childColumns = "nearbyPoiId",
                onDelete = ForeignKey.CASCADE
            )
        })
public class PropertyNearbyPoiJoin {

    private final long propertyId;
    private final long nearbyPoiId;

    public PropertyNearbyPoiJoin(long propertyId, long nearbyPoiId) {
        this.propertyId = propertyId;
        this.nearbyPoiId = nearbyPoiId;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public long getNearbyPoiId() {
        return nearbyPoiId;
    }
}
