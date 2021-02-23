package com.example.final_project.fragments.running;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.example.final_project.utils.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class Fragment_Map extends Fragment {
    private SupportMapFragment supportMapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment_FRG_googleMaps);

        return view;
    }

    public void updateMap(ArrayList<Location> locations) {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Location currentLocation = locations.get(locations.size() - 1);
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);

                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        latLng, Constants.MAP_ZOOM
                ));
                googleMap.addMarker(markerOptions);
                googleMap.addPolyline(getPolylineOptions(locations));
            }
        });
    }

    private PolylineOptions getPolylineOptions(ArrayList<Location> locations) {
        PolylineOptions polylineOptions = new PolylineOptions().width(Constants.MAP_POLYLINE_WIDTH).color(Color.BLUE).geodesic(true);

        for (Location location : locations) {
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
            polylineOptions.add(point);
        }

        return polylineOptions;
    }
}