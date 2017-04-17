package de.hdm_stuttgart.foreignbuddy.Chat;

import com.google.firebase.auth.FirebaseAuth;

import de.hdm_stuttgart.foreignbuddy.Users.User;

/**
 * Created by Marc-JulianFleck on 12.04.17.
 */

public class Conversation {

    public String UserID;
    public String username;

    public Conversation() {

    }

    public  Conversation(String UserID, String username){
        this.UserID = UserID;
        this.username = username;
    }



}
