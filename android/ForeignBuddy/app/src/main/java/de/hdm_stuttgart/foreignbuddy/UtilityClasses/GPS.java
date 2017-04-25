package de.hdm_stuttgart.foreignbuddy.UtilityClasses;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Marc-JulianFleck on 22.04.17.
 */

public class GPS implements LocationListener{


    public static final String LOCATION_UPDATED = "LOCATION_UPDATED";
    private static GPS instance;

    //Helper
    private Geocoder geocoder;


    private Context context;


    private void showSettingsAlertForGPS() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is off");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public static GPS getInstance(){
        if (instance == null) {
            instance = new GPS();
        }
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                String city = addresses.get(0).getLocality();
                //txt_location_profil.setText("in " + city);

                Intent locationIntent = new Intent();
                locationIntent.setAction(GPS.LOCATION_UPDATED);
                locationIntent.putExtra("city",city);
                LocalBroadcastManager.getInstance(context).sendBroadcast(locationIntent);


                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("latitude").setValue(location.getLatitude());
                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("longitude").setValue(location.getLongitude());
                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("lastKnownCity").setValue(city);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void startLocationRequest(){

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            //Check if GPS or Network Location functionality of device is activated
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
                }

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
                }

            } else {
                showSettingsAlertForGPS();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }



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
