package com.openclassrooms.realestatemanager.ui.fragments;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMapsBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.ui.activities.DetailsActivity;
import com.openclassrooms.realestatemanager.utils.ActivityUtils;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.List;
import java.util.Set;

public class MapsFragment extends Fragment {

    private PropertyViewModel mViewModel;
    private Context mContext;
    private FragmentMapsBinding binding;
    private SharedPreferences mSharedPreferences;
    private float mZoom = 11;

    // DATA FOR CALCULATE CENTER
    int currentIndexProperty;
    double minLat;
    double maxLat;
    double minLon;
    double maxLon;

    private GoogleMap mGoogleMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;

            mGoogleMap.setOnInfoWindowClickListener(marker -> {

                if (ActivityUtils.isTablet(getActivity())){
                    mViewModel.mCurrentPropertyIdSelected.setValue(Long.parseLong(marker.getTag().toString()));
                } else {
                    getActivity().startActivityForResult(new Intent(mContext, DetailsActivity.class)
                                    .putExtra("property_id", Long.parseLong(marker.getTag().toString())),
                            Constants.LAUNCH_DETAILS_ACTIVITY);
                }
            });

            mViewModel.mListPropertyMutableLiveData.observe(getViewLifecycleOwner(), MapsFragment.this::updateUI);
        }
    };

    public static MapsFragment newInstance() {
        return (new MapsFragment());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mContext = view.getContext();
        configureViewModel();

        if (Utils.isInternetAvailable()) {
            binding.fragmentMapsNonetwork.setVisibility(View.VISIBLE);
        }

        mSharedPreferences = mContext.getSharedPreferences(Constants.PREF_SHARED_KEY, Context.MODE_PRIVATE);

        return view;
    }

    private void updateUI(List<Property> properties) {
        createAllMarkers(properties);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) { mapFragment.getMapAsync(callback); }
    }

    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(mContext);
        this.mViewModel = ViewModelProviders.of(requireActivity(), mViewModelFactory).get(PropertyViewModel.class);
    }

    // Créer tout les markers et focus sur le centre de tout les markers
    private void createAllMarkers(List<Property> properties){

        mGoogleMap.clear();
        currentIndexProperty = 0;

        for (int i = 0; i < properties.size(); i++){
            Property property = properties.get(i);

            mViewModel.getAddressWithPropertyId(property.getId()).observe(this, address -> {

                if (address == null) return;

                // Si c'est le premier
                if (currentIndexProperty == 0){
                    minLat = address.getLatitude();
                    maxLat = address.getLatitude();
                    minLon = address.getLongitude();
                    maxLon = address.getLongitude();
                }

                if (address.getLatitude() < minLat) {
                    minLat = address.getLatitude();
                }

                if(address.getLatitude() > maxLat){
                    maxLat = address.getLatitude();
                }

                if (address.getLongitude() < minLon) {
                    minLon = address.getLongitude();
                }

                if (address.getLongitude() > maxLon){
                    maxLon = address.getLongitude();
                }

                // Check if this is a favorite property
                Set<String> listFavorites = mSharedPreferences.getStringSet(Constants.PREF_FAVORITES_PROPERTIES_KEY, null);
                BitmapDescriptor bitmapDescriptor;

                if (listFavorites != null && listFavorites.contains(String.valueOf(property.getId()))){
                    bitmapDescriptor = vectorToBitmap(mContext, R.drawable.marker_home_favorite);
                }else {
                    bitmapDescriptor = vectorToBitmap(mContext, R.drawable.marker_home2);
                }

                // Creation du marker
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(address.getLatLng())
                        .icon(bitmapDescriptor)
                        .title(address.getCompleteAddress()))
                        .setTag(property.getId());

                //Si c'est le dernier
                if (properties.size() - 1 == currentIndexProperty){
                    //todo: voir quelle niveau de zoom lui mettre et/ou voir pour faire des clusters
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getCenter(), getZoom()));
                }

                currentIndexProperty++;
            });
        }
    }

    public static BitmapDescriptor vectorToBitmap(Context context, @DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private LatLng getCenter(){
        return new LatLng(maxLat - (maxLat - minLat) / 2, maxLon - (maxLon - minLon) / 2) ;
    }

    private int getZoom(){
        int zoom = 0;
        double distanceLat = maxLat - minLat;
        double distanceLong = maxLon - minLon;
        double maxDistance =  Math.max(distanceLat, distanceLong);

        if (maxDistance < 0.00015625){
            // inutile max 19
            zoom = 19;
        }else if (maxDistance < 0.0003125){
            // inutile max 19
            zoom = 19;
        }else if (maxDistance < 0.000625){
            zoom = 19;
        }else if (maxDistance < 0.00125){
            zoom = 18;
        }else if (maxDistance < 0.0025){
            zoom = 17;
        }else if (maxDistance < 0.005){
            zoom = 16;
        }else if (maxDistance < 0.01){
            zoom = 15;
        } else if (maxDistance < 0.0275){
            zoom = 14;
        }else if (maxDistance < 0.055){
            zoom = 13;
        }else if (maxDistance < 0.112){
            zoom = 12;
        }else if (maxDistance < 0.225){
            zoom = 11;
        }else if (maxDistance < 0.45){
            zoom = 10;
        }else if (maxDistance < 0.9){
            zoom = 9;
        }else if (maxDistance < 1.8){
            zoom = 8;
        }else if (maxDistance < 3.6){
            zoom = 7;
        }else if (maxDistance < 7.2){
            zoom = 6;
        }else if (maxDistance < 14.4){
            zoom = 5;
        }else if (maxDistance < 28.8){
            zoom = 4;
        }else if (maxDistance < 57.6){
            zoom = 3;
        }else if (maxDistance < 115.2){
            // inutile 3 est le min
            zoom = 2;
        } else if (maxDistance < 230.4){
            // inutile 3 est le min
            zoom = 1;
        }

        return zoom;
    }

}