package de.hdm_stuttgart.foreignbuddy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdm_stuttgart.foreignbuddy.Database.DatabaseUser;
import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.User;

public class UserDetailsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText userName;
    private RadioButton rB_English, rB_German, rB_French, rB_Spanish;
    private RadioButton rB_native_English, rB_native_German, rB_native_French, rB_native_Spanish;
    private CheckBox cB_Nature, cB_Culture, cB_Politics, cB_Sports, cB_Reading, cB_Music, cB_Technology;
    private DatabaseReference mDatabase;
    private User myUser;
    private TextView currentDistance;
    private SeekBar seekbar;
    private int seekbarprogess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        //WIDGETS
        userName = (EditText) findViewById(R.id.text_userName);

        rB_English = (RadioButton) findViewById(R.id.rB_English);
        rB_French = (RadioButton) findViewById(R.id.rB_French);
        rB_German = (RadioButton) findViewById(R.id.rB_German);
        rB_Spanish = (RadioButton) findViewById(R.id.rB_Spanish);

        rB_native_English = (RadioButton) findViewById(R.id.rB_native_English);
        rB_native_French = (RadioButton) findViewById(R.id.rB_native_French);
        rB_native_German = (RadioButton) findViewById(R.id.rB_native_German);
        rB_native_Spanish = (RadioButton) findViewById(R.id.rB_native_Spanish);

        cB_Nature = (CheckBox) findViewById(R.id.cB_Nature);
        cB_Culture = (CheckBox) findViewById(R.id.cB_Culture);
        cB_Politics = (CheckBox) findViewById(R.id.cB_Politics);
        cB_Sports = (CheckBox) findViewById(R.id.cB_Sports);
        cB_Reading = (CheckBox) findViewById(R.id.cB_Reading);
        cB_Music = (CheckBox) findViewById(R.id.cB_Music);
        cB_Technology = (CheckBox) findViewById(R.id.cB_Technology);

        currentDistance = (TextView) findViewById(R.id.tv_currentDistance);
        seekbar = (SeekBar) findViewById (R.id.seekBar);

        //Database und firebase authentication initialization
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        getCurrentUserData();

        //Set Seekbar for selecting value for maximum distance to matches that will be displayed
        seekbar.setMax(200);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekbarprogess = i;

                if(seekbarprogess<20){
                    seekbarprogess=20;
                }

                currentDistance.setText("" + seekbarprogess + " km");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




    }


    public void getCurrentUserData(){

        //Get current User
        //TODO because myUser should be get from DatabaseUserClass! Unnecessary network traffic
        mDatabase.child("users")
                .child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                myUser = dataSnapshot.getValue(User.class);

                //Get current username
                userName.setText(myUser.getUsername());


                //Get desired maximum distance to matches that will be displayed
                currentDistance.setText("" + myUser.getDistanceToMatch() + " km");
                seekbarprogess = myUser.getDistanceToMatch();
                seekbar.setProgress(seekbarprogess);


                //Get current native language in radiobutton group
                switch (myUser.getNativeLanguage()) {
                    case "English":
                        rB_native_English.setChecked(true);
                        break;
                    case "German":
                        rB_native_German.setChecked(true);
                        break;
                    case "French":
                        rB_native_French.setChecked(true);
                        break;
                    case "Spanish":
                        rB_native_Spanish.setChecked(true);
                        break;

                    default:
                        throw new IllegalArgumentException("Wrong Language");
                }



                //Get current language in radiobutton group
                switch (myUser.getLanguage()) {
                    case "English":
                        rB_English.setChecked(true);
                        break;
                    case "German":
                        rB_German.setChecked(true);
                        break;
                    case "French":
                        rB_French.setChecked(true);
                        break;
                    case "Spanish":
                        rB_Spanish.setChecked(true);
                        break;

                    default:
                        throw new IllegalArgumentException("Wrong Language");
                }

                //Get current interests
                if (myUser.interests != null) {


                if (myUser.interests.get("Nature")) {
                    cB_Nature.setChecked(true);
                }

                if (myUser.interests.get("Culture")) {
                    cB_Culture.setChecked(true);
                }

                if (myUser.interests.get("Politics")) {
                    cB_Politics.setChecked(true);
                }

                if (myUser.interests.get("Sports")) {
                    cB_Sports.setChecked(true);
                }

                if (myUser.interests.get("Music")) {
                    cB_Music.setChecked(true);
                }

                if (myUser.interests.get("Technology")) {
                    cB_Technology.setChecked(true);
                }

                if (myUser.interests.get("Reading")) {
                    cB_Reading.setChecked(true);
                }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    public void addUserDataToDatabase(){

        //Add username to database
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .child("username")
                .setValue(userName.getText().toString());

        //Add emailadress to database
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .child("email")
                .setValue(firebaseAuth.getInstance().getCurrentUser().getEmail());


        //Add distance to Matches to database
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .child("distanceToMatch")
                .setValue(seekbarprogess);


        //Add native language to database
        if(rB_native_English.isChecked()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("nativeLanguage")
                    .setValue("English");
        }
        else if(rB_native_German.isChecked()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("nativeLanguage")
                    .setValue("German");
        }
        if(rB_native_French.isChecked()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("nativeLanguage")
                    .setValue("French");
        }
        else if(rB_native_Spanish.isChecked()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("nativeLanguage")
                    .setValue("Spanish");
        }


    //Add language the user wants to improve
        if(rB_English.isChecked()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("language")
                    .setValue("English");
        }
        if(rB_German.isChecked()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("language")
                    .setValue("German");
        }
        if(rB_French.isChecked()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("language")
                    .setValue("French");
        }

        if(rB_Spanish.isChecked()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("language")
                    .setValue("Spanish");
        }

        //Add interests to database
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .child("interests")
                .child("Culture")
                .setValue(cB_Culture.isChecked());

        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .child("interests")
                .child("Nature")
                .setValue(cB_Nature.isChecked());

        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .child("interests")
                .child("Politics")
                .setValue(cB_Politics.isChecked());

        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .child("interests")
                .child("Sports")
                .setValue(cB_Sports.isChecked());

        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .child("interests")
                .child("Reading")
                .setValue(cB_Reading.isChecked());

        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .child("interests")
                .child("Music")
                .setValue(cB_Music.isChecked());

        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .child("interests")
                .child("Technology")
                .setValue(cB_Technology.isChecked());

    }




    public  void btn_next_clicked(View v) {
        addUserDataToDatabase();
        DatabaseUser.removeActualInstance();
        Intent i =  new Intent(UserDetailsActivity.this, MainActivity.class);
        startActivity(i);
    }
}
