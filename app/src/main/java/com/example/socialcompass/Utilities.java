package com.example.socialcompass;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

//import java.util.Optional;

public class Utilities {
    public static void showAlert(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle("Alert")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty() && input.trim().length() < 20;
    }

    public static boolean isLongitudeWithinRange(String input) {

        input = input.trim();
        if (input.length() < 1 || input.length() > 25) {
            return false;
        }

        try {
            double number = Double.parseDouble(input);
            if (number >= -180 && number <= 180) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLatitudeWithinRange(String input) {
        input = input.trim();
        if (input.length() < 1 || input.length() > 25) {
            return false;
        }

        try {
            double number = Double.parseDouble(input);
            if (number >= -90 && number <= 90) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidAll(String label, String longitude, String latitude) {
        if (Utilities.isValidString(label) && Utilities.isLongitudeWithinRange(longitude)
                && Utilities.isLatitudeWithinRange(latitude)) {
            return true;
        }
        return false;
    }


    public static float getAngle(float gpsLat, float gpsLong, float addressLat, float addressLong) {
        if(Math.abs(gpsLong-addressLong)>180){
            if(gpsLong < 0){
                addressLong = -180-(180-addressLong);
            }
            else{
                addressLong = 180+(180+addressLong);

            }
        }
        if(Math.abs(gpsLat-addressLat) > 90){
            if(gpsLat < 0){
                addressLat = -90-(90-addressLat);
            }
            else{
                addressLat = 90+(90+addressLat);

            }
        }

        float lenA = Math.abs(addressLong - gpsLong);
        float lenB = Math.abs(addressLat - gpsLat);

        double angle_rad = Math.atan2(lenB, lenA);

        double angle_deg = angle_rad * 180.0 / Math.PI;
        float degree = (float) angle_deg;

        if (gpsLat <= addressLat && gpsLong <= addressLong) {
            // first quadrant
            degree = 90 - degree;
        } else if (gpsLat <= addressLat && gpsLong >= addressLong) {
            // second quadrant
            degree = degree + 270;
        } else if (gpsLat >= addressLat && gpsLong >= addressLong) {
            // third quadrant
            degree = 270 - degree;
        } else if (gpsLat >= addressLat && gpsLong <= addressLong) {
            // forth quadrant
            degree = degree + 90;
        }

        return degree;
    }

    public static boolean checkForLocationPermissions(Activity a) {
        return ActivityCompat.checkSelfPermission(a, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(a, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean validOrientationValue(String input) {
        input = input.trim();
        if (input.length() < 1 || input.length() > 25) {
            return false;
        }

        try {
            double number = Double.parseDouble(input);
            if (number <= 359 && number >= 0) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static double calculateDistanceInMiles(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusInMiles = 3958.8; // radius of the earth in miles
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanceInMiles = earthRadiusInMiles * c;
        return distanceInMiles;
    }

}
