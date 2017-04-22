package de.hdm_stuttgart.foreignbuddy.Users;

import android.location.Location;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

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
    //private ArrayList<Conversation> conversations;


    //Constucture

    public User(String userID) {
        this.userID = userID;
    }

    public User (){}


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

    /*public ArrayList<Conversation> getConversations() {
        return conversations;
    }*/



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

    /*public void setConversations(ArrayList<Conversation> conversations) {}*/

}
