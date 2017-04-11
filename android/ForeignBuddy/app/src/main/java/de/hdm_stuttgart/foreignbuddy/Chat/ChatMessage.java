package de.hdm_stuttgart.foreignbuddy.Chat;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jan-niklas on 28.03.17.
 */

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private String messageTime;
    private String messageUserID;

    public ChatMessage(String messageText, String messageUser, String messageUserID) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageUserID = messageUserID;
        // Initialize to current time
        messageTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime()).toString();
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageUserID() {
        return messageUserID;
    }

    public void setMessageUserID(String messageUserID) {
        this.messageUserID = messageUserID;
    }


}
