package com.openclassrooms.realestatemanager.ui.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.R;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapViewFragment extends Fragment {

    @BindView(R.id.fragment_map_view_map) MapView myOpenMapView;

    public static MapViewFragment newInstance() {
        return (new MapViewFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        ButterKnife.bind(this,view);

        myOpenMapView.setBuiltInZoomControls(true);
        myOpenMapView.setClickable(true);
        myOpenMapView.getController().setZoom(15);

        // échelle sur la carte en superposition
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(myOpenMapView);
        myOpenMapView.getOverlays().add(myScaleBarOverlay);

        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(view.getContext()), myOpenMapView);
        mLocationOverlay.enableMyLocation();
        myOpenMapView.setMultiTouchControls(true);
        myOpenMapView.getOverlays().add(mLocationOverlay);


        return view;
    }
}