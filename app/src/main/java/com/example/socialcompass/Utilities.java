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
        if (input.length() < 2) {
            return false;
        }
        char direction = input.charAt(input.length() - 1);
        String numberString = input.substring(0, input.length() - 1);

        try {
            int number = Integer.parseInt(numberString);

            if (direction == 'N' || direction == 'S') {
                return number >= 0 && number <= 90;
            }
            else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isLatitudeWithinRange(String input) {
        input = input.trim();
        if (input.length() < 2 || input.length()>4) {
            return false;
        }
        char direction = input.charAt(input.length() - 1);
        String numberString = input.substring(0, input.length() - 1);

        try {
            int number = Integer.parseInt(numberString);

            if (direction == 'E' || direction == 'W') {
                return number >= 0 && number <= 180;
            }
            else {
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
