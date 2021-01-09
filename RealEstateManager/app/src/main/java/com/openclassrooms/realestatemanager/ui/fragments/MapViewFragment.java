package com.openclassrooms.realestatemanager.ui.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMapViewBinding;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class MapViewFragment extends Fragment {

    FragmentMapViewBinding binding;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;

    private Context mContext;

    public static MapViewFragment newInstance() {
        return (new MapViewFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMapViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mContext = view.getContext();

        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = mContext;
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's
        //tile servers will get you banned based on this string

        //inflate and create the map
//        setContentView(R.layout.activity_main);

        map = binding.fragmentMapViewMap;
        map.setTileSource(TileSourceFactory.MAPNIK);

        requestPermissionsIfNecessary(new String[] {
                // if you need to show the current location, uncomment the line below
                // Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
//        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
//        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            requestPermissions(
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            requestPermissions(
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Configuration.getInstance().load(mContext, PreferenceManager.getDefaultSharedPreferences(mContext));
//
//        binding = FragmentMapViewBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        mContext = view.getContext();
//
//        initMap();
//
//        //handle permissions first, before map is created. not depicted here
////        checkPermissions();
//
//        //load/initialize the osmdroid configuration, this can be done
//
////        Configuration.getInstance().load(mContext, PreferenceManager.getDefaultSharedPreferences(mContext));
//        //setting this before the layout is inflated is a good idea
//        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
//        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
//        //see also StorageUtils
//        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string
//
////        //inflate and create the map
//////        setContentView(R.layout.activity_main);
////
////        myOpenMapView = (MapView) view.findViewById(R.id.fragment_map_view_map);
//////        map.setTileSource(TileSourceFactory.MAPNIK);
////
////        myOpenMapView.setBuiltInZoomControls(true);
////        myOpenMapView.setClickable(true);
////        myOpenMapView.getController().setZoom(15);
////
////        // échelle sur la carte en superposition
////        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(myOpenMapView);
////        myOpenMapView.getOverlays().add(myScaleBarOverlay);
////
////        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(view.getContext()), myOpenMapView);
////        mLocationOverlay.enableMyLocation();
////        myOpenMapView.setMultiTouchControls(true);
////        myOpenMapView.getOverlays().add(mLocationOverlay);
//
//
//        return view;
//    }

    private void initMap(){

        binding.fragmentMapViewMap.setTileSource(TileSourceFactory.MAPNIK);

        binding.fragmentMapViewMap.setBuiltInZoomControls(true);
        binding.fragmentMapViewMap.setClickable(true);
        binding.fragmentMapViewMap.getController().setZoom(15);

        // se deplace a un point de la carte
        binding.fragmentMapViewMap.getController().setCenter(new GeoPoint(48.858093, 2.294694));

        // Listener qui actualise deplace le point de la carte quand la position de l'user change
        LocationListener ecouteurGPS = new LocationListener() {
            @Override
            public void onLocationChanged(Location localisation)
            {
//            ...
                binding.fragmentMapViewMap.getController().setCenter(new GeoPoint(localisation.getLatitude(), localisation.getLongitude()));
//            ...
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        // Ajoute une échelle sur la carte en superposition
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(binding.fragmentMapViewMap);
        binding.fragmentMapViewMap.getOverlays().add(myScaleBarOverlay);

        // Ajoute une boussole sur la carte en superposition
        CompassOverlay mCompassOverlay = new CompassOverlay(mContext, new InternalCompassOrientationProvider(mContext), binding.fragmentMapViewMap);
        mCompassOverlay.enableCompass();
        binding.fragmentMapViewMap.getOverlays().add(mCompassOverlay);

        // Ajoute sa position GPS en superposition
//        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(mContext), binding.fragmentMapViewMap);
//        mLocationOverlay.enableMyLocation();
//        binding.fragmentMapViewMap.setMultiTouchControls(true);
//        binding.fragmentMapViewMap.getOverlays().add(mLocationOverlay);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void checkPermissions(){
        Dexter.withContext(mContext)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(mContext, "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(mContext, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

//    public void onResume(){
//        super.onResume();
//        //this will refresh the osmdroid configuration on resuming.
//        //if you make changes to the configuration, use
//        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
//        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
//    }
//
//    public void onPause(){
//        super.onPause();
//        //this will refresh the osmdroid configuration on resuming.
//        //if you make changes to the configuration, use
//        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        //Configuration.getInstance().save(this, prefs);
//        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
//    }
}