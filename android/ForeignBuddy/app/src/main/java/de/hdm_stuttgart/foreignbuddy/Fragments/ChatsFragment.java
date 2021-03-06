package de.hdm_stuttgart.foreignbuddy.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdm_stuttgart.foreignbuddy.Activities.ChatActivity;
import de.hdm_stuttgart.foreignbuddy.Chat.Conversation;
import de.hdm_stuttgart.foreignbuddy.Database.DatabaseUser;
import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.User;


public class ChatsFragment extends Fragment {

    //Widgets
    private ListView chatOverview;
    private TextView name;
    private TextView lastMessage;
    private Toolbar toolbar;
    private ImageView img_user;

    private List<Conversation> conversations = new ArrayList<>();


    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        chatOverview = (ListView)view.findViewById(R.id.ChatsOverview);

        /*progressDialog = ProgressDialog.show(getActivity(), "Loading Conversations...", "Please wait...", true);

        //Set listener to firebase database so messages can be retrieved
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("conversations")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> allConversations = dataSnapshot.getChildren();
                for (DataSnapshot child : allConversations) {
                    Conversation conversation = child.getValue(Conversation.class);
                    conversations.add(conversation);
                }
                ArrayAdapter<Conversation> conversationAdapter = new ConversationListAdapter();
                chatOverview.setAdapter(conversationAdapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        conversations = DatabaseUser.getInstance().getCurrentUsersConversations();
        ArrayAdapter<Conversation> conversationAdapter = new ConversationListAdapter();
        chatOverview.setAdapter(conversationAdapter);

        //Set onItemClickListener on list elements to open chats
        chatOverview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(),ChatActivity.class);
                    Conversation conversation = conversations.get(position);
                    intent.putExtra("UserID", conversation.UserID);
                    intent.putExtra("Username", conversation.username);
                    intent.putExtra("URLPhoto", conversation.urlProfilephoto);
                    startActivity(intent);
                }
        });

        return view;
    }

    @Override
    public void onStart() {
        //Set toolbar
        super.onStart();
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_conversations);
        toolbar.setTitle("Chats");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    }

    private class ConversationListAdapter extends ArrayAdapter<Conversation> {

        public ConversationListAdapter() {
            super(getActivity(), R.layout.conversations, conversations);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final View view = getActivity().getLayoutInflater().inflate(R.layout.conversations, parent, false);

            final Conversation currentConversation = conversations.get(position);

            //Widgets
            name = (TextView) view.findViewById(R.id.txt_name_conversation);
            img_user = (ImageView) view.findViewById(R.id.img_user_conversation);
            lastMessage = (TextView) view.findViewById(R.id.txt_lastMassage_conversation);

            //Load profile photo of conversation partner
            Picasso.with(getActivity()).
                    load(currentConversation.urlProfilephoto)
                    .placeholder(R.drawable.user_male)
                    .error(R.drawable.user_male)
                    .into(img_user);

            //Set usename of conversation partner
            name.setText(currentConversation.username);

            //Set preview of last message in chat list overview elements
            if(currentConversation.lastMessage != null) {

                if (currentConversation.lastMessage.length() >= 33) {
                    lastMessage.setText(currentConversation.lastMessage.substring(0, 30) + "...");
                } else {
                    lastMessage.setText(currentConversation.lastMessage);
                }
            }else{

                lastMessage.setText("");
            }

            return view;

        }

    }
}
