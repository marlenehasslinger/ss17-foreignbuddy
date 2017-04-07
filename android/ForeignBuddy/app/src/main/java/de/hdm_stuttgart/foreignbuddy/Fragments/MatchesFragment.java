package de.hdm_stuttgart.foreignbuddy.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import java.util.Locale;

import android.Manifest;

import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.User;


public class MatchesFragment extends Fragment {

    private List<User> matches;
    private ListView listView;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        matches = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.list_matches);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child("v2CWwLpvORPke4q6niplUx8QXrx2").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        for (int i = 0; i < 10; i++){
                            matches.add(user);
                        }
                        matches.add(user);
                        ArrayAdapter<User> matchesAdapter = new UserListAdapter();
                        listView.setAdapter(matchesAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_matches);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Matches");
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

            ImageView img_user = (ImageView) view. findViewById(R.id.img_user);
            TextView name = (TextView) view.findViewById(R.id.txt_name_matches);
            TextView location = (TextView) view.findViewById(R.id.txt_location_matches);
            TextView language = (TextView) view.findViewById(R.id.txt_language_matches);


            name.setText(currentUser.username);
            language.setText(currentUser.nativeLanguage);


            return view;
        }
    }
}
