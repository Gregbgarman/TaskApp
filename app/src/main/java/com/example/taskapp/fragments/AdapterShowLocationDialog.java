package com.example.taskapp.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskapp.R;
import com.example.taskapp.activities.CreateTaskActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AdapterShowLocationDialog extends DialogFragment {

    private String Placename,Address;
    private double lat,lng;
    private Button btnOpenMapsApp;
    private TextView tvAddress;

    public AdapterShowLocationDialog(String Placename,double lat,double lng,String Address){
        this.Placename=Placename;
        this.lat=lat;
        this.lng=lng;
        this.Address=Address;
    }

    private GoogleMap TheMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(Placename)).showInfoWindow();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),10));

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_adapter_show_location_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        btnOpenMapsApp=view.findViewById(R.id.btnFASOpenMaps);
        btnOpenMapsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGoogleMaps();
            }
        });
        tvAddress=view.findViewById(R.id.tvFASaddress);
        tvAddress.setText(Address);
    }

    private void OpenGoogleMaps(){
        Uri uri=Uri.parse("geo:"+ lat +"," + lng+"?q="+Address );
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}