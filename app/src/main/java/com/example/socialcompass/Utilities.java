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
        if (input.length() < 1 || input.length() > 20) {
            return false;
        }

        try {
            double number = Double.parseDouble(input);
            return number >= -90 && number <= 90;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLatitudeWithinRange(String input) {
        input = input.trim();
        if (input.length() < 1 || input.length() > 20) {
            return false;
        }

        try {
            double number = Double.parseDouble(input);
            return number >= -180 && number <= 180;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidAll(String label, String longitude, String latitude) {
        return Utilities.isValidString(label) && Utilities.isLongitudeWithinRange(longitude)
                && Utilities.isLatitudeWithinRange(latitude);
    }


    public static float getAngle(float gpsLat, float gpsLong, float addressLat, float addressLong) {
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

}
