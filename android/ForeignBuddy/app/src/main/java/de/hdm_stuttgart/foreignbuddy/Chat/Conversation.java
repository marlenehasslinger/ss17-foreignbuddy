package de.hdm_stuttgart.foreignbuddy.Chat;

import com.google.firebase.auth.FirebaseAuth;

import de.hdm_stuttgart.foreignbuddy.Users.User;

/**
 * Created by Marc-JulianFleck on 12.04.17.
 */

public class Conversation {

    public String UserID;
    public String username;
    public String urlProfilephoto;
    public String lastMessage;
    public String conversationID;

    public Conversation() {

    }

    public  Conversation(String UserID, String username, String urlProfilephoto, String conversationID){
        this.UserID = UserID;
        this.username = username;
        this.urlProfilephoto = urlProfilephoto;
        this.conversationID = conversationID;
    }



}
