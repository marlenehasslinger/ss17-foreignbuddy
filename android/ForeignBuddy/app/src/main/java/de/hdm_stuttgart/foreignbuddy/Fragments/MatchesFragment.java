package de.hdm_stuttgart.foreignbuddy.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.hdm_stuttgart.foreignbuddy.Activities.ChatActivity;
import de.hdm_stuttgart.foreignbuddy.Activities.UserDetailsActivity;
import de.hdm_stuttgart.foreignbuddy.Database.DatabaseUser;
import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.User;
import de.hdm_stuttgart.foreignbuddy.UtilityClasses.GPS;
import de.hdm_stuttgart.foreignbuddy.UtilityClasses.SortEntfernung;
import de.hdm_stuttgart.foreignbuddy.UtilityClasses.SortInterests;


public class MatchesFragment extends Fragment {

    private ImageView img_user;
    private TextView name;
    private TextView location;
    private TextView language;
    private TextView interests;
    private Button btn_chat;
    private List<User> matches = new ArrayList<>();
    private ListView listView;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private User myUser;

    //For Location
    private Geocoder geocoder;
    private List<Address> addresses;


    //For profilphoto
    private StorageReference riversRef;
    private StorageReference storageReference;
    private String uploadName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        //Für Profilbilder
        storageReference = FirebaseStorage.getInstance().getReference();
        riversRef = storageReference.child("images/" + uploadName);
        listView = (ListView) view.findViewById(R.id.list_matches);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //get current User
        myUser = DatabaseUser.getInstance().getCurrentUser();

        //get Matches and set in TableView
        matches = DatabaseUser.getInstance().getCurrentUsersMatches();
        Collections.sort(matches, new SortInterests());
        ArrayAdapter<User> matchesAdapter = new UserListAdapter();
        listView.setAdapter(matchesAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Toolbar
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_conversations);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Matches");
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.toolbar_matches_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tb_filterDistance_matches:
                matches = DatabaseUser.getInstance().getCurrentUsersMatches();
                Collections.sort(matches, new SortEntfernung());
                ArrayAdapter<User> matchesAdapter = new UserListAdapter();
                listView.setAdapter(matchesAdapter);
                break;
            case R.id.tb_filterInterests_matches:
                matches = DatabaseUser.getInstance().getCurrentUsersMatches();
                Collections.sort(matches, new SortInterests());
                ArrayAdapter<User> matchesAdapter2 = new UserListAdapter();
                listView.setAdapter(matchesAdapter2);
        }
        return true;
    }

    private class UserListAdapter extends ArrayAdapter<User> {

        public UserListAdapter() {
            super(getActivity(), R.layout.matches, matches);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.matches, parent, false);
            } else {
                view = convertView;
            }

            final User currentMatch = matches.get(position);

            btn_chat = (Button) view.findViewById(R.id.btn_chat_matches);
            btn_chat.setTag(currentMatch);
            btn_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    User user = (User) v.getTag();
                    intent.putExtra("UserID", user.userID);
                    intent.putExtra("Username", user.username);
                    intent.putExtra("URLPhoto", user.urlProfilephoto);
                    startActivity(intent);
                }
            });

            //For Profilephoto
            uploadName = currentMatch.email + "_profilePhoto";

            //Widgets
            img_user = (ImageView) view.findViewById(R.id.img_user_matches);
            name = (TextView) view.findViewById(R.id.txt_name_matches);
            location = (TextView) view.findViewById(R.id.txt_location_matches);
            language = (TextView) view.findViewById(R.id.txt_language_matches);
            interests = (TextView)view.findViewById(R.id.txt_interests_matches);

            //Set Widget texts
            name.setText(currentMatch.username);
            language.setText(currentMatch.nativeLanguage);
            interests.setText(currentMatch.getNumberOfCommonInterest() + " common interests");
            if (currentMatch.latitude == null || currentMatch.longitude == null) {
                location.setText("- Km"); // If User has no location
            } else if (myUser.latitude == null || myUser.longitude == null) {
                location.setText("In " + currentMatch.lastKnownCity); //If I have no location
            } else {
                double entfernung = GPS.distanceInKm(myUser,currentMatch);
                location.setText(Double.toString(entfernung) + " Km"); //If both have location
            }

            try {
                img_user.setImageDrawable(Drawable.createFromPath(currentMatch.getProfilePhoto().getAbsolutePath()));
            } catch (Exception e) {
                img_user.setImageResource(R.drawable.user_male);
                Log.d("Download", "Current profil photo successfully downloaded and displayed");
            }

            return view;

        }
    }

}



