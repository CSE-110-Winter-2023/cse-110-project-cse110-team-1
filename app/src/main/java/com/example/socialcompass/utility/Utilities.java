package com.example.socialcompass.utility;
import static androidx.test.espresso.intent.Checks.checkNotNull;

import java.security.SecureRandom;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.UUID;

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

    public static String generatePrivateId() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }


    public static String generatePublicId() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        while (sb.length() < 10) {
            int randomInt = random.nextInt(36);
            char c = (randomInt < 10) ? (char) ('0' + randomInt) : (char) ('a' + randomInt - 10);
            sb.append(c);
        }
        return sb.toString();
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
    public static double convertToScale(double distance) {
        if (distance < 0) {
            return -1;
        }
        if (distance >= 0 && distance <= 1) {
            return distance;
        } else if (distance > 1 && distance <= 10) {
            return (distance - 1) / 9;
        } else if (distance > 10 && distance <= 500) {
            return (distance - 10) / 490;
        }
        return 1;

    }
    public static double mapDistanceToLogScale(double distance) {
        double minDistance = 500.0;
        double maxDistance = 12450.5;
        double minLogValue = Math.log(minDistance);
        double maxLogValue = Math.log(maxDistance);

        double logDistance = Math.log(distance);
        double mappedValue = (logDistance - minLogValue) / (maxLogValue - minLogValue);

        return mappedValue;
    }
    public static double zoomDistance(int zoom, double distance) {
        if (zoom < 1 || zoom > 4 || distance < 0) {
            return -1;
        }
        double scaledDistance = convertToScale(distance);
        if (zoom == 1) {
            if (distance <= 1) {
                return scaledDistance / zoom;
            } else return 1;
        } else if (zoom == 2) {
            if (distance <= 1) {
                return scaledDistance / zoom;
            } else if (distance <= 10) {
                return scaledDistance / zoom + (1.0 / zoom);
            } else return 1;
        } else if (zoom == 3) {
            if (distance <= 1) {
                return scaledDistance / zoom;
            } else if (distance <= 10) {
                return scaledDistance / zoom + (1.0 / zoom);
            } else if (distance <= 500) {
                return scaledDistance / zoom + (2.0 / zoom);
            } else return 1;
        } else if (zoom == 4) {
            if (distance <= 1) {
                return scaledDistance / zoom;
            } else if (distance <= 10) {
                return scaledDistance / zoom + (1.0 / zoom);
            } else if (distance <= 500) {
                return scaledDistance / zoom + (2.0 / zoom);
            } else if (distance > 500) {
                return mapDistanceToLogScale(distance) / zoom + (3.0 / zoom);
            } else return 1;
        }
        return -1;
    }
    /**
     * Converts the given distance to a value of raius, based on predefined scales.
     * @param zoom the zoom level, which must be 1, 2, 3, or 4 correspond to 0-1,1-10,10-100,100-500
     * @param distance the distance to convert
     * @return the converted value between 0 and 450
     */
    public static int calculateRadius(int zoom, double distance) {
        int radius = (int)Math.ceil(450*zoomDistance(zoom, distance));
        return radius;
    }

    /**
     * Helper methods for testing with Espresso
     * Used to get the item from a RecyclerView
     * Source: https://stackoverflow.com/questions/31394569/how-to-assert-inside-a-recyclerview-in-espresso
     * To see how this is used, see FriendListActivityTest
     * @param position
     * @param itemMatcher
     * @return
     */
    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<>(RecyclerView.class) {

            @Override
            public void describeTo(Description description) {
//                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}
