package de.hdm_stuttgart.foreignbuddy.Database;

import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdm_stuttgart.foreignbuddy.Chat.Conversation;
import de.hdm_stuttgart.foreignbuddy.Fragments.ChatsFragment;
import de.hdm_stuttgart.foreignbuddy.Users.User;

/**
 * Created by Marc-JulianFleck on 19.04.17.
 */

public class DatabaseUser {

    private static User currentUser;
    private static List<User> currentUsersMatches;
    private static List<Conversation> currentUsersConversations;

    private DatabaseUser() {
    }

    public static void InstanceCurrentUser() {
        deleteCurrentUser();
        loadCurrentUser();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static List<User> getCurrentUsersMatches() {
        return currentUsersMatches;
    }

    public static List<Conversation> getCurrentUsersConversations() {
        return currentUsersConversations;
    }

    public static boolean haveCurrentUser() {
        if (currentUser != null || currentUsersMatches != null || currentUsersConversations != null) {
            return true;
        }
        return false;
    }

    private static void loadCurrentUser() {
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentUser = dataSnapshot.getValue(User.class);
                        currentUsersMatches = new ArrayList<>();
                        loadCurrentUsersMatches();
                        currentUsersConversations = new ArrayList<>();
                        loadCurrentUsersConversations();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private static void loadCurrentUsersMatches() {
        FirebaseDatabase.getInstance().getReference()
                .child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> allUsers = dataSnapshot.getChildren();
                for (DataSnapshot child : allUsers) {
                    User user = child.getValue(User.class);
                    if (!user.getUserID().equals(currentUser.getUserID())) {
                        currentUsersMatches.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static boolean checkConstraintsMatches(User user) {
        if (user.getUserID().equals(currentUser.getUserID())) {
            return false;
        } else if (!user.getNativeLanguage().equals(currentUser.getLanguage())) {
            return false;
        } else if (currentUser.getNativeLanguage().equals(user.getLanguage())) {
            return false;
        }
        return true;
    }

    private static void loadCurrentUsersConversations() {
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
                            currentUsersConversations.add(conversation);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private static void deleteCurrentUser() {
        currentUser = null;
        currentUsersMatches = null;
        currentUsersConversations = null;
    }

}
