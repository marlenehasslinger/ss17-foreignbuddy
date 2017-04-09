package de.hdm_stuttgart.foreignbuddy.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
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

import java.io.IOException;
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
    private ProgressDialog progressDialog;

    //Start Location Varaibles
    private Geocoder geocoder;
    private List<Address> addresses;
    //END Location Variables



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        matches = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.list_matches);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        mDatabase = FirebaseDatabase.getInstance().getReference();

        progressDialog = ProgressDialog.show(getActivity(), "Loading Matches...", "Please wait...", true);
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> allUsers = dataSnapshot.getChildren();
                for (DataSnapshot child : allUsers) {
                    User user = child.getValue(User.class);
                    matches.add(user);
                }
                ArrayAdapter<User> matchesAdapter = new UserListAdapter();
                listView.setAdapter(matchesAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
            /*try {
                addresses = geocoder.getFromLocation(currentUser.latitude, currentUser.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }*/



            ImageView img_user = (ImageView) view. findViewById(R.id.img_user);
            TextView name = (TextView) view.findViewById(R.id.txt_name_matches);
            TextView location = (TextView) view.findViewById(R.id.txt_location_matches);
            TextView language = (TextView) view.findViewById(R.id.txt_language_matches);

            name.setText(currentUser.username);
            //location.setText(addresses.get(0).getLocality());
            language.setText(currentUser.nativeLanguage);

            return view;
        }
    }
}
