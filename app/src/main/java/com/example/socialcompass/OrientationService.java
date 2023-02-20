package com.example.socialcompass;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class OrientationService implements SensorEventListener {
    private static OrientationService instance;

    private final SensorManager sensorManager;
    private float[] accelerometerReading;
    private float[] magnetometerReading;
    private MutableLiveData<Float> azimuth;


    OrientationService(Activity activity) {
        this.azimuth = new MutableLiveData<>();
        this.sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        this.registerSensorListeners();
    }

    private void registerSensorListeners() {

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public static OrientationService singleton(Activity activity) {
        if (instance == null) {
            instance = new OrientationService(activity);
        }
        return instance;
    }


    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            accelerometerReading = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            magnetometerReading = event.values;
        }
        if (accelerometerReading != null && magnetometerReading != null) {
            onBothSensorDataAvailable();
        }
    }

    private void onBothSensorDataAvailable() {
        if (accelerometerReading == null || magnetometerReading == null) {
            throw new IllegalStateException("Both sensors must be available to compute orientation.");
        }

        var r = new float[9];
        var i = new float[9];
        var success = SensorManager.getRotationMatrix(r, i, accelerometerReading, magnetometerReading);

        if (success) {

            var orientation = new float[3];
            SensorManager.getOrientation(r, orientation);

            this.azimuth.postValue(orientation[0]);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Auto-generated method stub required by the interface
        // we don't care about this at all lol
    }

    public void unregisterSensorListeners() {
        sensorManager.unregisterListener(this);
    }

    public LiveData<Float> getOrientation() {
        return this.azimuth;
    }

    public void setMockOrientationData(MutableLiveData<Float> mockData) {
        unregisterSensorListeners();
        this.azimuth = mockData;
    }
}

