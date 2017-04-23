package de.hdm_stuttgart.foreignbuddy.Activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import de.hdm_stuttgart.foreignbuddy.Chat.ChatMessage;
import de.hdm_stuttgart.foreignbuddy.Database.DatabaseChat;
import de.hdm_stuttgart.foreignbuddy.Database.DatabaseUser;
import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.User;


public class ChatActivity extends AppCompatActivity {


    private FirebaseListAdapter<ChatMessage> adapter;
    private String conversationID;
    private User myUser;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Widgets
        FloatingActionButton SendButton = (FloatingActionButton) findViewById(R.id.SendButton);

        myUser = DatabaseUser.getCurrentUser();

        //get conversationID and ,if necessary, creats new Chat in Database
        conversationID = DatabaseChat.newChat(this);

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Widget Text input
                EditText enterText = (EditText) findViewById(R.id.EnterText);
                //Write to Database
                DatabaseChat.writeNewMessage(enterText.getText().toString(), conversationID);
                // Clear the input
                enterText.setText("");
            }
        });

        displayChatMessages();
    }

    @Override
    protected void onStart() {
        super.onStart();
        toolbar = (Toolbar) this.findViewById(R.id.toolbar_conversation);
        toolbar.setTitle(getIntent().getStringExtra("Username"));
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }

    private void displayChatMessages() {

        ListView listOfMessages = (ListView) findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(conversationID)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(model.getMessageTime());
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
