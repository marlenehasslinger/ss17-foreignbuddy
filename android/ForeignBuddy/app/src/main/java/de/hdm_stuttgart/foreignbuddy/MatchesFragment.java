package de.hdm_stuttgart.foreignbuddy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import de.hdm_stuttgart.foreignbuddy.Users.User;


public class MatchesFragment extends Fragment {

    List<User> matches;
    Toolbar toolbar;
    private DatabaseReference mDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        matches = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        matches.add(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        ListView listView = (ListView) view.findViewById(R.id.list_matches);

        matches = new ArrayList<>();
        ArrayAdapter<User> matchesAdapter = new UserListAdapter();
        listView.setAdapter(matchesAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_matches);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Matches");


        /*User user = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                "Mayou", FirebaseAuth.getInstance().getCurrentUser().getEmail(), "English", "Stuttgart");

        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);*/


    }

    private class UserListAdapter extends ArrayAdapter<User>{

        public UserListAdapter(){
            super(getActivity(), R.layout.matches, matches);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = convertView;
            if (convertView == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.matches, parent, false);

            }

            User currentUser = matches.get(position);

            TextView name = (TextView) view.findViewById(R.id.txt_name_matches);
            TextView location = (TextView) view.findViewById(R.id.txt_location_matches);
            TextView language = (TextView) view.findViewById(R.id.txt_language_matches);

            name.setText(currentUser.username);
            location.setText(currentUser.location);
            language.setText(currentUser.nativeLanguage);


            return view;
        }
    }
}
