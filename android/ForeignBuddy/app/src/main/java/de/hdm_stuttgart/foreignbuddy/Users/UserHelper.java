package de.hdm_stuttgart.foreignbuddy.Users;

import android.app.ProgressDialog;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Marc-Julian Fleck on 12.04.17.
 */

public class UserHelper {

    private static User myUser = null;
    private static User resolvedUser = null;

    public static User getMyUser() {
        return myUser;
    }

    public static void downloadMyUser(final ProgressDialog progressDialog) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myUser = dataSnapshot.getValue(User.class);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void resolveUserID(String uid) {

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resolvedUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static User getResolvedUser() {return resolvedUser;}

    public static double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        final int radius = 6371;

        double lat = Math.toRadians(lat2 - lat1);
        double lon = Math.toRadians(lon2- lon1);

        double a = Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lon / 2) * Math.sin(lon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = radius * c;

        double result =  Math.abs(d);
        result = Math.round(100.0 * result) / 100.0;

        return result;
    }
}
