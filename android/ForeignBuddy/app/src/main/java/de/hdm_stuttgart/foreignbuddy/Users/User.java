package de.hdm_stuttgart.foreignbuddy.Users;

import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

/**
 * Created by Marc-JulianFleck on 29.03.17.
 */

public class User {

    public String userID;
    public String username;
    public String email;
    public String location;
    //private Location lastLocation = null;
    //private Boolean[] interests;


    //Constucture
    public User (String userID, String username, String email, String nativeLanguage, String location) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.nativeLanguage = nativeLanguage;
        this.location = location;
    }

    public User(String userID) {
        this.userID = userID;
    }


    public User (){

    }





    //Getters
    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public String getLocation() {
        return location;
    }


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

    public String nativeLanguage;

    public void setLocation(String location) {
        this.location = location;
    }



}
