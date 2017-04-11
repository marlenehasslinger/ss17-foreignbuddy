package de.hdm_stuttgart.foreignbuddy.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdm_stuttgart.foreignbuddy.Chat.ChatMessage;
import de.hdm_stuttgart.foreignbuddy.Fragments.MatchesFragment;
import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.User;


public class ChatActivity extends AppCompatActivity {


    private FirebaseListAdapter<ChatMessage> adapter;
    private String conversationID = "7483982hfhhf982hhrjf_djhfu9849rjn";
    private DatabaseReference mDatabase;
    private User myUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FloatingActionButton SendButton =
                (FloatingActionButton)findViewById(R.id.SendButton);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users")
                .child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myUser = dataSnapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        int c = FirebaseAuth.getInstance().getCurrentUser().getUid().compareTo(getIntent().getStringExtra("UserID"));
        if (c > 0) {
            conversationID = FirebaseAuth.getInstance()
                    .getCurrentUser().getUid() + "_" + getIntent().getStringExtra("UserID");
        } else {
            conversationID = getIntent().getStringExtra("UserID") + "_" + FirebaseAuth.getInstance()
                    .getCurrentUser().getUid();
        }

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
                                ,FirebaseAuth.getInstance().getCurrentUser().getUid()));

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

}
