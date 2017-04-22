package de.hdm_stuttgart.foreignbuddy.UtilityClasses;

/**
 * Created by Marc-JulianFleck on 22.04.17.
 */

public class GPS {

    public static double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        final int radius = 6371;

        double lat = Math.toRadians(lat2 - lat1);
        double lon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lon / 2) * Math.sin(lon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = radius * c;

        double result = Math.abs(d);
        result = Math.round(100.0 * result) / 100.0;

        return result;
    }

}
