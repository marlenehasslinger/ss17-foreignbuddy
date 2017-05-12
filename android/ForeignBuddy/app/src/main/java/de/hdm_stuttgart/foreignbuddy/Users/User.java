package de.hdm_stuttgart.foreignbuddy.Users;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdm_stuttgart.foreignbuddy.Chat.Conversation;

/**
 * Created by Marc-JulianFleck on 29.03.17.
 */

public class User {

    public String userID;
    public String username;
    public String email;
    public String nativeLanguage;
    public String language;
    public String urlProfilephoto;
    public String lastKnownCity;
    public Double latitude;
    public Double longitude;
    public int distanceToMatch;
    public Map<String,Boolean> interests;
    public File profilePhoto;

    File localFile = null;

    //Constructors
    public User (){}
    public User(String userID) {
        this.userID = userID;
    }

    //Getters
    public String getUserID() {
        return userID;
    }
    public String getUsername() {
        return username;
    }
    public String getNativeLanguage() {
        return nativeLanguage;
    }
    public String getLanguage() {
        return language;
    }
    public int getDistanceToMatch() {return distanceToMatch;}
    public File getProfilePhoto() {return profilePhoto;}

    //Setters
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }
    public void setLanguage(String Language) { this.language = Language; }
    public void setDistanceToMatch(int distanceToMatch) {this.distanceToMatch = distanceToMatch;}
    public void setProfilePhoto(File profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void loadProfilePhoto() {
        String downloadName = FirebaseAuth.getInstance().getCurrentUser().getEmail() + "_profilePhoto";
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = storageReference.child("images/" + downloadName);
        try {
            localFile = File.createTempFile("images", downloadName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Download profile photo via firebase database reference
        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Download", "Profil photo successfully downloaded");
                        profilePhoto = localFile;
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("Download", "Profil photo download failed");
            }
        });
    }
}
