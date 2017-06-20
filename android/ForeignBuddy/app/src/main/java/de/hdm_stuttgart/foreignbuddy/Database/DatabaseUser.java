package de.hdm_stuttgart.foreignbuddy.Database;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdm_stuttgart.foreignbuddy.Chat.Conversation;
import de.hdm_stuttgart.foreignbuddy.Users.Match;
import de.hdm_stuttgart.foreignbuddy.Users.User;
import de.hdm_stuttgart.foreignbuddy.UtilityClasses.GPS;

/**
 * Created by Marc-JulianFleck on 19.04.17.
 */

public class DatabaseUser {

    public static final String FINISHED_LOADING = "FINISHED_LOADING";
    private static DatabaseUser instance;

    //Create and get Instance of DatabaseUser
    public static synchronized DatabaseUser getInstance() {
        if (instance == null) {
            instance = new DatabaseUser();
            instance.InstanceCurrentUser();
        }
        return instance;
    }
    //Remove Instance of DatabaseUser
    public static synchronized void removeActualInstance(){
        instance = null;
    }


    //User Data
    private User currentUser;
    private List<Match> currentUsersMatches = new ArrayList<>();
    private List<Conversation> currentUsersConversations = new ArrayList<>();

    private void InstanceCurrentUser() {
        loadCurrentUser();
    }
    public void setContext(Context context) {
        this.context = context;
    }

    //Helper
    private StorageReference riversRef;
    private StorageReference storageReference;
    private Context context;
    private boolean initalLoading = true;

    public User getCurrentUser() {
        return currentUser;
    }

    public List<Match> getCurrentUsersMatches() {
        return currentUsersMatches;
    }

    public List<Conversation> getCurrentUsersConversations() {
        return currentUsersConversations;
    }

    private void loadCurrentUser() {
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentUser = dataSnapshot.getValue(User.class);
                        loadProfilePhotoCurrentUser(currentUser);
                        loadCurrentUsersMatches();
                        loadCurrentUsersConversations();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private synchronized void  loadCurrentUsersMatches() {
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUser.userID)
                .child("matches")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUsersMatches.clear();
                Iterable<DataSnapshot> allMatches = dataSnapshot.getChildren();
                for (DataSnapshot child : allMatches) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(child.getKey())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Match match =  dataSnapshot.getValue(Match.class);
                                    loadProfilePhotoMatches(match);
                                    match.setCommonInterests(currentUser);
                                    match.setDistanceToMyUser(GPS.distanceInKm(currentUser, match));
                                    currentUsersMatches.add(match);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private synchronized void loadCurrentUsersConversations() {
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("conversations")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentUsersConversations.clear();
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

    private void loadProfilePhotoCurrentUser(final User user) {
        String downloadName = user.getEmail() + "_profilePhoto";
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = storageReference.child("images/" + downloadName);
        try {
            //localFile = File.createTempFile("images", downloadName);
            user.setProfilePhoto(File.createTempFile("images", downloadName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Download profile photo via firebase database reference
        riversRef.getFile(user.getProfilePhoto())
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Download", "Profil photo successfully downloaded");
                        user.setProfilePhoto(user.getProfilePhoto());
                        if (initalLoading == true) {
                            initalLoading = false;
                            Intent loadingIntent = new Intent();
                            loadingIntent.setAction(DatabaseUser.FINISHED_LOADING);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(loadingIntent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("Download", "Profil photo download failed xxxxxxxx");
                user.setProfilePhoto(null);
                if (initalLoading == true) {
                    initalLoading = false;
                    Intent loadingIntent = new Intent();
                    loadingIntent.setAction(DatabaseUser.FINISHED_LOADING);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(loadingIntent);
                }
            }
        });
    }
    private void loadProfilePhotoMatches(final User user) {
        String downloadName = user.getEmail() + "_profilePhoto";
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = storageReference.child("images/" + downloadName);
        try {
            //localFile = File.createTempFile("images", downloadName);
            user.setProfilePhoto(File.createTempFile("images", downloadName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Download profile photo via firebase database reference
        riversRef.getFile(user.getProfilePhoto())
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Download", "Profil photo successfully downloaded");
                        user.setProfilePhoto(user.getProfilePhoto());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("Download", "Profil photo download failed");
                user.setProfilePhoto(null);
            }
        });
    }

    public void setInitalLoading(boolean initalLoading) {
        this.initalLoading = initalLoading;
    }
}
