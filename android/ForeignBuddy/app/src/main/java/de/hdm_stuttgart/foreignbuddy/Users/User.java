package de.hdm_stuttgart.foreignbuddy.Users;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Marc-JulianFleck on 29.03.17.
 */

public class User{

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
    public Map<String, Boolean> interests;
    private File profilePhoto;


    //Constructors
    public User() {
    }
    public User(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String Language) {
        this.language = Language;
    }

    public int getDistanceToMatch() {
        return distanceToMatch;
    }

    public void setDistanceToMatch(int distanceToMatch) {
        this.distanceToMatch = distanceToMatch;
    }

    public File getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(File profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
