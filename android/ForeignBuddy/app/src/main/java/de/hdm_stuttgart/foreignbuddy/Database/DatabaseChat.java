package de.hdm_stuttgart.foreignbuddy.Database;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdm_stuttgart.foreignbuddy.Chat.ChatMessage;
import de.hdm_stuttgart.foreignbuddy.Chat.Conversation;
import de.hdm_stuttgart.foreignbuddy.Users.User;

/**
 * Created by Marc-JulianFleck on 22.04.17.
 */

public class DatabaseChat {

    private static Activity activity;
    private static String conversationID;


    private DatabaseChat(){}

    public static String newChat(Activity ChatActivity){
        activity = ChatActivity;
        conversationID = getConversationID();
        User currentUser = DatabaseUser.getInstance().getCurrentUser();
        List<Conversation> conversations = DatabaseUser.getInstance().getCurrentUsersConversations();
        for (int i = 0; i < conversations.size(); i++) {
            if (conversations.get(i).conversationID.equals(conversationID)) {
                return conversationID;
            }
        }
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("conversations")
                .child(conversationID)
                .setValue(new Conversation(activity.getIntent().getStringExtra("UserID")
                        , activity.getIntent().getStringExtra("Username")
                        , activity.getIntent().getStringExtra("URLPhoto")
                        , conversationID));

        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(activity.getIntent().getStringExtra("UserID"))
                .child("conversations")
                .child(conversationID)
                .setValue(new Conversation(currentUser.getUserID()
                        , currentUser.getUsername()
                        , currentUser.urlProfilephoto
                        , conversationID));

        return conversationID;
    }

    public static void writeNewMessage(String message, String conversationID){
        // Read the input field and push a new instance
        // of ChatMessage to the Firebase database
        FirebaseDatabase.getInstance()
                .getReference()
                .child("chats")
                .child(conversationID)
                .child(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime()))
                .setValue(new ChatMessage(message, DatabaseUser.getInstance().getCurrentUser().getUsername()
                        ,DatabaseUser.getInstance().getCurrentUser().getUserID()));
    }


    private static String getConversationID(){
        int c = FirebaseAuth.
                getInstance()
                .getCurrentUser()
                .getUid()
                .compareTo(activity.getIntent().getStringExtra("UserID"));
        if (c > 0) {
            return FirebaseAuth.getInstance()
                    .getCurrentUser().getUid() + "_" + activity.getIntent().getStringExtra("UserID");
        } else {
            return activity.getIntent().getStringExtra("UserID") + "_" + FirebaseAuth.getInstance()
                    .getCurrentUser().getUid();
        }
    }

}
