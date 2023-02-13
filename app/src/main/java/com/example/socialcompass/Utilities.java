package com.example.socialcompass;

import android.app.Activity;
import android.app.AlertDialog;

//import java.util.Optional;

public class Utilities {
    public static void showAlert(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle("Alert")
                .setMessage(message)
                .setPositiveButton("OK",(dialog,id) -> {
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
        if (input.length() < 1 || input.length()>20) {
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
    public static boolean isLatitudeWithinRange(String input) {
        input = input.trim();
        if (input.length() < 1 || input.length()>20) {
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

    public static boolean isValidAll(String label, String longitude, String latitude) {
        if (Utilities.isValidString(label) && Utilities.isLongitudeWithinRange(longitude)
                && Utilities.isLatitudeWithinRange(latitude)) {
            return true;
        }
        return false;
    }
}