package de.hdm_stuttgart.foreignbuddy.Database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;

import de.hdm_stuttgart.foreignbuddy.Chat.Conversation;
import de.hdm_stuttgart.foreignbuddy.Fragments.ChatsFragment;
import de.hdm_stuttgart.foreignbuddy.R;
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

    //Helper
    private StorageReference riversRef;
    private StorageReference storageReference;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void InstanceCurrentUser() {
        deleteCurrentUser();
        loadCurrentUser();
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
                        currentUser.loadProfilePhoto();
                        currentUsersMatches = new ArrayList<>();
                        loadCurrentUsersMatches();
                        currentUsersConversations = new ArrayList<>();
                        loadCurrentUsersConversations();
                        //downloadProfilePhoto();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void loadCurrentUsersMatches() {
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUser.userID)
                .child("matches")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> allMatches = dataSnapshot.getChildren();
                for (DataSnapshot child : allMatches) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(child.getKey())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    currentUsersMatches.add(user);
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

    private void deleteCurrentUser() {
        currentUser = null;
        currentUsersMatches = null;
        currentUsersConversations = null;
    }

}
