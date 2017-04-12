package de.hdm_stuttgart.foreignbuddy.Activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import de.hdm_stuttgart.foreignbuddy.Chat.ChatMessage;
import de.hdm_stuttgart.foreignbuddy.Chat.Conversation;
import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.UserHelper;
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

        FloatingActionButton SendButton = (FloatingActionButton)findViewById(R.id.SendButton);

        myUser = UserHelper.getMyUser();

        int c = myUser.userID.compareTo(getIntent().getStringExtra("UserID"));
        if (c > 0) {
            conversationID = FirebaseAuth.getInstance()
                    .getCurrentUser().getUid() + "_" + getIntent().getStringExtra("UserID");
        } else {
            conversationID = getIntent().getStringExtra("UserID") + "_" + FirebaseAuth.getInstance()
                    .getCurrentUser().getUid();
        }

        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(myUser.userID)
                .child("conversations")
                .child(conversationID)
                .setValue(new Conversation(getIntent().getStringExtra("UserID"),getIntent().getStringExtra("Username")));

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText EnterText = (EditText)findViewById(R.id.EnterText);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database

                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("chats")
                        .child(conversationID)
                        .child(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime()))
                        .setValue(new ChatMessage(EnterText.getText().toString(), myUser.username
                                ,myUser.userID));

                // Clear the input
                EnterText.setText("");
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
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }

    private void displayChatMessages() {

        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()
                .child("chats").
                child(conversationID)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

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
