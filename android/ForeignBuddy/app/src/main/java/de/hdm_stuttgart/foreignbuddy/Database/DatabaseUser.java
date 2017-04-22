package de.hdm_stuttgart.foreignbuddy.Database;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdm_stuttgart.foreignbuddy.Fragments.MatchesFragment;
import de.hdm_stuttgart.foreignbuddy.Users.User;

/**
 * Created by Marc-JulianFleck on 19.04.17.
 */

public class DatabaseUser {

    private static User currentUser;
    private static List<User> currentUsersMatches = new ArrayList<>();;

    private DatabaseUser(){}

    public static void InstanceCurrentUser() {
            deleteConnection();
            loadCurrentUser();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static List<User> getCurrentUsersMatches() {
        return currentUsersMatches;
    }

    public  static boolean haveCurrentUser() {
        if (currentUser != null) {
            return true;
        }
        return false;
    }

    private static void loadCurrentUser(){
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentUser = dataSnapshot.getValue(User.class);
                        loadCurrentUsersMatches();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private static void loadCurrentUsersMatches(){
        FirebaseDatabase.getInstance().getReference()
                .child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> allUsers = dataSnapshot.getChildren();
                for (DataSnapshot child : allUsers) {
                    User user = child.getValue(User.class);
                    currentUsersMatches.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void deleteConnection() {
        currentUser = null;
    }

}
