package com.example.socialcompass.old;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GPSLocationHandler implements LocationListener {
    private MutableLiveData<Pair<Double,Double>> currentLocation;
    private final LocationManager locationManager;

    public static final int MIN_MS_UPDATE = 10, MIN_DIST_UPDATE = 0;

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.currentLocation.postValue(new Pair<Double,Double>(
                location.getLatitude(),location.getLongitude()));
    }

    public LiveData<Pair<Double,Double>> getLocation() { return currentLocation; }

    public GPSLocationHandler(@NonNull Activity me) {
        currentLocation = new MutableLiveData<>();
        locationManager = (LocationManager) me.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    MIN_MS_UPDATE, MIN_DIST_UPDATE, this);
        } catch(SecurityException e) {
            throw new IllegalStateException("Does not have app permissions to get GPS Location!");
        }
//        this.currentLocation.postValue(new Pair<Double,Double>(0D,0D));
    }




}
