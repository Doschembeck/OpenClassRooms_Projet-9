package com.openclassrooms.realestatemanager.utils.interfaces;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MyLocationListener implements LocationListener {

    String TAG = "TAG1";

    public void onLocationChanged(Location localisation)
    {
        Log.d(TAG, "localisation : " + localisation.toString());
        String coordonnees = String.format("Latitude : %f - Longitude : %f\n", localisation.getLatitude(), localisation.getLongitude());
        Log.d(TAG, "coordonnees : " + coordonnees);
    }

    @Override
    public void onStatusChanged(String fournisseur, int status, Bundle extras) {
        Log.d(TAG, "onStatus Changed | Fournisseur : " + fournisseur + " statuts : " + status);

//        Context context;
//        Toast.makeText(context, fournisseur + " état : " + status, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onProviderEnabled(String fournisseur)
    {
        Log.d(TAG, "onProviderEnabled: " + fournisseur + " activé !");
    }

    @Override
    public void onProviderDisabled(String fournisseur)
    {
        Log.d(TAG, "onProviderDisabled: " + fournisseur + " désactivé !");
    }

}
