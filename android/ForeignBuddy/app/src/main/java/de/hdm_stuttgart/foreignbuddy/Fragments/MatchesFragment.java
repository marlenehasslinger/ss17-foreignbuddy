package de.hdm_stuttgart.foreignbuddy.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdm_stuttgart.foreignbuddy.Activities.ChatActivity;
import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.User;


public class MatchesFragment extends Fragment  {

    private List<User> matches;
    private ListView listView;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;


    String UserId;
    private User myUser;


    //Start Location Varaibles
    private Geocoder geocoder;
    private List<Address> addresses;
    //END Location Variables


    //Für Profilbilddownload
    private StorageReference riversRef;
    private StorageReference storageReference;
    private String uploadName;


    //Für Listenelement
    ImageView img_user;
    TextView name;
    TextView location;
    TextView language;
    Button btn_chat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        //Für Profilbilder
        storageReference = FirebaseStorage.getInstance().getReference();
        riversRef = storageReference.child("images/" + uploadName);
        matches = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.list_matches);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //START GET CURRENT USER
        mDatabase.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        myUser = dataSnapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        //END GET CURRENT USER

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
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_conversations);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Matches");
    }

    private class UserListAdapter extends ArrayAdapter<User> {

        public UserListAdapter() {
            super(getActivity(), R.layout.matches, matches);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = convertView;
            final User currentUser = matches.get(position);
            UserId = currentUser.getUserID();


            if (convertView == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.matches, parent, false);
                btn_chat = (Button) view.findViewById(R.id.btn_chat_matches);
                btn_chat.setTag(currentUser);
                btn_chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        User user = (User) v.getTag();
                        intent.putExtra("UserID", user.userID);
                        intent.putExtra("Username", user.username);
                        startActivity(intent);
                    }
                });
            }

            /*try {
                addresses = geocoder.getFromLocation(currentUser.latitude, currentUser.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            //For Profilephoto
            uploadName = currentUser.email + "_profilePhoto";

            img_user = (ImageView) view.findViewById(R.id.img_user_matches);
            name = (TextView) view.findViewById(R.id.txt_name_matches);
            location = (TextView) view.findViewById(R.id.txt_location_matches);
            language = (TextView) view.findViewById(R.id.txt_language_matches);


            name.setText(currentUser.username);

            if (currentUser.latitude == null || currentUser.longitude == null) {
                location.setText("- Km");
            } else if (myUser.latitude == null || myUser.longitude == null) {
                location.setText("In " + currentUser.lastKnownCity);
            } else {
                double entfernung = distanceInKm(myUser.latitude, myUser.longitude
                        , currentUser.latitude, currentUser.longitude);
                location.setText(Double.toString(entfernung) + " Km");
            }
            language.setText(currentUser.nativeLanguage);



            Picasso.with(getActivity()).
                    load(currentUser.urlProfilephoto)
                    .placeholder(R.drawable.user_male)
                    .error(R.drawable.user_male)
                    .into(img_user);

            return view;

        }
    }

    private static double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        final int radius = 6371;

        double lat = Math.toRadians(lat2 - lat1);
        double lon = Math.toRadians(lon2- lon1);

        double a = Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lon / 2) * Math.sin(lon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = radius * c;

        double result =  Math.abs(d);
        result = Math.round(100.0 * result) / 100.0;

        return result;
    }

}



