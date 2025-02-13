package com.lakithrathnayake.myapplication02;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.lakithrathnayake.myapplication02.databinding.ActivityMapsBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ActivityMapsBinding binding;
    private ExecutorService executorService;
    private Handler mainHandler;
    private float GEOFENCE_RADIUS = 80;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String GEOFENCE_ID = "MY_GEOFENCE_ID";
    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private GeofencingClient geofencingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        executorService = Executors.newFixedThreadPool(3);
        mainHandler = new Handler(Looper.getMainLooper());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geofencingClient = LocationServices.getGeofencingClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableUserLocation();

        LatLng selectedLocation = getSelectedLocation();
        showSelectedLocationAndCreateGeofence(selectedLocation);

        //add geofence
        addGeofence(selectedLocation);
    }

    private void addGeofence(LatLng latLng) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Location permission not granted");
            return;
        }

        // Create a geofence
        Geofence geofence = new Geofence.Builder()
                .setRequestId(GEOFENCE_ID)
                .setCircularRegion(latLng.latitude, latLng.longitude, GEOFENCE_RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setLoiteringDelay(60000)
                .build();

        // Build the geofencing request
        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();

        // Add the geofence
        geofencingClient.addGeofences(geofencingRequest, getGeofencePendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MapsActivity.this, "Geofence added", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Geofence added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MapsActivity.this, "Geofence failed to add", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Failed to add geofence: " + e.getMessage());
                    }
                });
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
//        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            return   PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }


    private void enableUserLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE);
        }
        mMap.setMyLocationEnabled(true);

    }

    private LatLng getSelectedLocation() {
        return new LatLng(7.145652594871874, 80.56871574361789);
    }

    private void showSelectedLocationAndCreateGeofence(LatLng selectedLatLng) {
        executorService.execute(() -> {
            MarkerOptions markerOptions = new MarkerOptions().position(selectedLatLng);
            CircleOptions circleOptions = new CircleOptions()
                    .center(selectedLatLng)
                    .radius(GEOFENCE_RADIUS)
                    .strokeColor(Color.argb(255, 0, 0, 255))
                    .fillColor(Color.argb(64, 0, 0, 255))
                    .strokeWidth(4);

            mainHandler.post(() -> {
                mMap.addMarker(markerOptions);
                mMap.addCircle(circleOptions);
            });
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
            } else {
                Log.e(TAG, "Location permission denied");
            }
        } else if (requestCode == BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Background location permission granted");
            } else {
                Log.e(TAG, "Background location permission denied");
            }
        }
    }
}