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

import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.User;

public class UserDetailRegistration extends AppCompatActivity {

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
    private String name;


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

        ///Set Default value for username
        name = getUserNameFromEmail(firebaseAuth.getInstance().getCurrentUser().getEmail());
        userName.setText(name);

        //Set default values for languages
        rB_English.setChecked(true);
        rB_native_German.setChecked(true);

        //Set Seekbar for selecting value for maximum distance to matches that will be displayed
        seekbar.setMax(200);
        seekbar.setProgress(100);
        currentDistance.setText("100 km");
        seekbarprogess = 100;
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


    public String getUserNameFromEmail(String email){
        name = email;
        name = name.substring(0, name.lastIndexOf("@"));
        name = name.substring(0,1).toUpperCase()+name.substring(1);
        return name;

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
        if(rB_native_Spanish.isChecked()){

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

        //Add Last known City
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .child("lastKnownCity")
                .setValue("No Location available");
    }



    public  void btn_next_clicked(View v) {

        addUserDataToDatabase();
        Intent i =  new Intent(UserDetailRegistration.this, MainActivity.class);
        startActivity(i);
    }
}

