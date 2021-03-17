package com.openclassrooms.realestatemanager.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.openclassrooms.realestatemanager.utils.interfaces.MyLocationListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationUtils {

    // https://developer.android.com/training/location

    private static String TAG = "TAG1";

    public static String getFournisseur(LocationManager locationManager) {

        Criteria criteres = new Criteria();

        // la précision  : (ACCURACY_FINE pour une haute précision ou ACCURACY_COARSE pour une moins bonne précision)
        criteres.setAccuracy(Criteria.ACCURACY_FINE);

        // l'altitude
        criteres.setAltitudeRequired(true);

        // la direction
        criteres.setBearingRequired(true);

        // la vitesse
        criteres.setSpeedRequired(true);

        // la consommation d'énergie demandée
        criteres.setCostAllowed(true);
        criteres.setPowerRequirement(Criteria.POWER_HIGH);

        String fournisseur = locationManager.getBestProvider(criteres, true);
        Log.d(TAG, "fournisseur : " + fournisseur);

        return fournisseur;

    }

    public static Location getLocation(Context context, LocationManager locationManager, String fournisseur) {

        if (fournisseur != null)
        {
            // Check si la permission est accordé
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // dernière position connue
                Location localisation = locationManager.getLastKnownLocation(fournisseur);

                String coordonnees = String.format("Latitude : %f - Longitude : %f\n", localisation.getLatitude(), localisation.getLongitude());
                String autres = String.format("Vitesse : %f - Altitude : %f - Cap : %f\n", localisation.getSpeed(), localisation.getAltitude(), localisation.getBearing());
                Log.d(TAG, "localisation : " + localisation.toString());
                Log.d(TAG, "coordonnees : " + coordonnees);
                Log.d(TAG, autres);
                Log.d(TAG, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(localisation.getTime())));

                return localisation;
            }

        }

        return null;
    }

    public static Location initLocation(Context context, LocationManager locationManager) {

        String fournisseur = getFournisseur(locationManager);

        Location location = getLocation(context, locationManager, fournisseur);

        return location;
    }

    public static MyLocationListener stopLocation(LocationManager locationManager, MyLocationListener myLocationListener)
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(myLocationListener);
            myLocationListener = null;
        }

        return myLocationListener;
    }

}
