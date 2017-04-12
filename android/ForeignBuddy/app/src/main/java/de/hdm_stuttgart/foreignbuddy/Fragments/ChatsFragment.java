package de.hdm_stuttgart.foreignbuddy.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdm_stuttgart.foreignbuddy.Activities.ChatActivity;
import de.hdm_stuttgart.foreignbuddy.Chat.Conversation;
import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.User;
import de.hdm_stuttgart.foreignbuddy.Users.UserHelper;


public class ChatsFragment extends Fragment {

    private ListView chatOverview;
    private List<Conversation> conversations;
    private String conversationId;
    private ProgressDialog progressDialog;
    private TextView name;
    private TextView lastMessage;
    private Toolbar toolbar;



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        chatOverview = (ListView)view.findViewById(R.id.ChatsOverview);
        conversations = new ArrayList<>();

        progressDialog = ProgressDialog.show(getActivity(), "Loading Conversations...", "Please wait...", true);
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("conversations")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterable<DataSnapshot> allUsers = dataSnapshot.getChildren();
                for (DataSnapshot child : allUsers) {
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
        });

        //displayContactChat();

        chatOverview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent intent = new Intent(getActivity(),ChatActivity.class);
                Conversation conversation = conversations.get(position);
                startActivity(intent);
                intent.putExtra("UserID", conversation.UserID);
                intent.putExtra("Username", conversation.username);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_conversations);
        toolbar.setTitle("Chats");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    /*
    private void displayContactChat() {

        chatOverview = (ListView) getActivity().findViewById(R.id.ChatsOverview);

        adapter = new FirebaseListAdapter<ContactChat>(this, ContactChat.class, R.layout.contactchat,
                FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ContactChat model, int position) {

                TextView contactname = (TextView)v.findViewById(R.id.name);
                ImageView contactpicture = (ImageView)v.findViewById(R.id.contactPicture);


                contactname.setText(model.getContactname());
                //contactpicture.setImageResource(model.contactPicture);
            }
        };

        displayContactChat().setAdapter(adapter);
    }
*/

    private class ConversationListAdapter extends ArrayAdapter<Conversation> {

        public ConversationListAdapter() {
            super(getActivity(), R.layout.conversations, conversations);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = convertView;
            final Conversation currentConversation = conversations.get(position);

            if (convertView == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.conversations, parent, false);

            }

            //img_user = (ImageView) view.findViewById(R.id.img_user_conversations);
            name = (TextView) view.findViewById(R.id.txt_name_conversations);
            lastMessage = (TextView) view.findViewById(R.id.txt_lastMessage_conversation);


            name.setText(currentConversation.username);
            lastMessage.setText("Testsuidhfs");

            return view;

        }
    }
}
