package de.hdm_stuttgart.foreignbuddy.Database;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;

import de.hdm_stuttgart.foreignbuddy.Chat.Conversation;
import de.hdm_stuttgart.foreignbuddy.Fragments.ChatsFragment;
import de.hdm_stuttgart.foreignbuddy.Users.User;

/**
 * Created by Marc-JulianFleck on 19.04.17.
 */

public class DatabaseUser {

    private static DatabaseUser instance;

    //Create and get Instance of DatabaseUser
    public static synchronized DatabaseUser getInstance() {
        if (instance == null) {
            instance = new DatabaseUser();
        }
        return instance;
    }

    //User Data
    private User currentUser;
    private List<User> currentUsersMatches;
    private List<Conversation> currentUsersConversations;
    private File currentUserProfilpicture = null;

    //Helper
    private StorageReference riversRef;
    private StorageReference storageReference;


    public void InstanceCurrentUser() {
        deleteCurrentUser();
        loadCurrentUser();
        /*Intent locationIntent = new Intent();
        locationIntent.setAction("LOCATION_UPDATED");
        locationIntent.putExtra("location","Stuttgart");
        LocalBroadcastManager.getInstance(context).sendBroadcast(locationIntent);*/
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<User> getCurrentUsersMatches() {
        return currentUsersMatches;
    }

    public List<Conversation> getCurrentUsersConversations() {
        return currentUsersConversations;
    }

    private void loadCurrentUser() {
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentUser = dataSnapshot.getValue(User.class);
                        currentUsersMatches = new ArrayList<>();
                        loadCurrentUsersMatches();
                        currentUsersConversations = new ArrayList<>();
                        loadCurrentUsersConversations();
                        downloadProfilePhoto();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void loadCurrentUsersMatches() {
        FirebaseDatabase.getInstance().getReference()
                .child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> allUsers = dataSnapshot.getChildren();
                for (DataSnapshot child : allUsers) {
                    User user = child.getValue(User.class);
                    if (!user.getUserID().equals(currentUser.getUserID())) {
                        currentUsersMatches.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean checkConstraintsMatches(User user) {
        if (user.getUserID().equals(currentUser.getUserID())) {
            return false;
        } else if (user.getNativeLanguage().equals(currentUser.getLanguage())) {
            return false;
        } else if (currentUser.getNativeLanguage().equals(user.getLanguage())) {
            return false;
        }
        return true;
    }

    public void loadCurrentUsersConversations() {
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("conversations")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> allConversations = dataSnapshot.getChildren();
                        for (DataSnapshot child : allConversations) {
                            Conversation conversation = child.getValue(Conversation.class);
                            currentUsersConversations.add(conversation);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void downloadProfilePhoto() {

        String downloadName = FirebaseAuth.getInstance().getCurrentUser().getEmail() + "_profilePhoto";
        storageReference = FirebaseStorage.getInstance().getReference();
        riversRef = storageReference.child("images/" + downloadName);

        try {
            currentUserProfilpicture = File.createTempFile("images", downloadName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Download profile photo via firebase database reference
        riversRef.getFile(currentUserProfilpicture)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Download", "Profil photo successfully downloaded");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("Download", "Profil photo download failed");
            }
        });
    }

    public String getCurrentUserProfilpicture() {
        return currentUserProfilpicture.getPath();
    }

    private void deleteCurrentUser() {
        currentUser = null;
        currentUsersMatches = null;
        currentUsersConversations = null;
        currentUserProfilpicture = null;
    }

}
