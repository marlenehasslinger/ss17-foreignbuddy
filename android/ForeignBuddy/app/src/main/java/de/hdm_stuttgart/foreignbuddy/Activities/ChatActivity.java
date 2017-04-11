package de.hdm_stuttgart.foreignbuddy.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import de.hdm_stuttgart.foreignbuddy.R;


public class ChatActivity extends AppCompatActivity {


    private FirebaseListAdapter<ChatMessage> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FloatingActionButton SendButton =
                (FloatingActionButton)findViewById(R.id.SendButton);

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText EnterText = (EditText)findViewById(R.id.EnterText);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("chats")
                        .child(FirebaseAuth.getInstance()
                                .getCurrentUser().getUid() +"_"+ getIntent().getStringExtra("UserId")
                        )
                        .child(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime()))
                        .setValue(new ChatMessage(EnterText.getText().toString(), FirebaseAuth.getInstance()
                                .getCurrentUser().getUid()))
                        /*.setValue(new ChatMessage(EnterText.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        )*/;

                // Clear the input
                EnterText.setText("");
            }
        });

        displayChatMessages();
    }


    private void displayChatMessages() {

        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()
                .child("chats").
                child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid() +"_"+ getIntent().getStringExtra("UserId"))

        )






        {
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
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

}
