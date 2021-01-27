package com.openclassrooms.realestatemanager.ui.fragments;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.List;

public class MapsFragment extends Fragment {

    private PropertyViewModel mViewModel;
    private Context mContext;
    private FragmentMapsBinding binding;

    // DATA FOR CALCULATE CENTER
    int currentIndexProperty;
    double minLat;
    double maxLat;
    double minLon;
    double maxLon;

    private GoogleMap mGoogleMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;

            mGoogleMap.setOnInfoWindowClickListener(marker -> {
                long test = Long.parseLong(marker.getTag().toString());
                startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("property_id", test));
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

            mViewModel.getAddress(property.getAddressId()).observe(this, address -> {

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

                mGoogleMap.addMarker(new MarkerOptions()
                        .position(address.getLatLng())
                        .icon(vectorToBitmap(mContext, R.drawable.marker_home2))
                        .title(address.getCompleteAddress()))
                        .setTag(property.getId()
                );

                //Si c'est le dernier
                if (properties.size() - 1 == currentIndexProperty){
                    //todo: voir quelle niveau de zoom lui mettre et/ou voir pour faire deds clusters
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getCenter(), 0));
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

}