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
    private static User resolvedUser;

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

    public static User resolveUserID(String uid) {

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

        return resolvedUser;
    }

}