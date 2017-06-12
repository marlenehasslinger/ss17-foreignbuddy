package de.hdm_stuttgart.foreignbuddy.Database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;

import de.hdm_stuttgart.foreignbuddy.Chat.Conversation;
import de.hdm_stuttgart.foreignbuddy.Fragments.ChatsFragment;
import de.hdm_stuttgart.foreignbuddy.R;
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
    private void InstanceCurrentUser() {
        loadCurrentUser();
    }
    public void setContext(Context context) {
        this.context = context;
    }

    //User Data
    private User currentUser;
    private List<Match> currentUsersMatches = new ArrayList<>();
    private List<Conversation> currentUsersConversations = new ArrayList<>();

    //Helper
    private StorageReference riversRef;
    private StorageReference storageReference;
    private Context context;
    private boolean firstLoading = true;
    //private File localFile = null;

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
                        loadProfilePhoto(currentUser);
                        loadCurrentUsersMatches();
                        loadCurrentUsersConversations();
                        if (firstLoading == true) {
                            firstLoading = false;
                            Intent loadingIntent = new Intent();
                            loadingIntent.setAction(FINISHED_LOADING);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(loadingIntent);
                        }
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
                                    loadProfilePhoto(match);
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

    private void loadProfilePhoto(final User user) {
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

}
