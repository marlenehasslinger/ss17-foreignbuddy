package de.hdm_stuttgart.foreignbuddy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.User;

public class UserDetailsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText userName;
    private RadioButton rB_English, rB_German, rB_French, rB_Spanish;
    private RadioButton rB_native_English, rB_native_German, rB_native_French, rB_native_Spanish;
    private DatabaseReference mDatabase;
    private User myUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        userName = (EditText) findViewById(R.id.text_userName);

        rB_English = (RadioButton) findViewById(R.id.rB_English);
        rB_French = (RadioButton) findViewById(R.id.rB_French);
        rB_German = (RadioButton) findViewById(R.id.rB_German);
        rB_Spanish = (RadioButton) findViewById(R.id.rB_Spanish);

        rB_native_English = (RadioButton) findViewById(R.id.rB_native_English);
        rB_native_French = (RadioButton) findViewById(R.id.rB_native_French);
        rB_native_German = (RadioButton) findViewById(R.id.rB_native_German);
        rB_native_Spanish = (RadioButton) findViewById(R.id.rB_native_Spanish);

        //Database und firebase authentication initialization
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        getCurrentUserData();


    }


    public void getCurrentUserData(){


        mDatabase.child("users")
                .child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                myUser = dataSnapshot.getValue(User.class);

                //Set current username in EditText
                userName.setText(myUser.getUsername());


                //Set current native language in radiobutton group
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



                //Set current language in radiobutton group
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
                        rB_French.setChecked(true);
                        break;

                    default:
                        throw new IllegalArgumentException("Wrong Language");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }








    public void addUserDataToDatabase(){

        //Add User to database
        User user = new User(firebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user);

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



        //Add native language to database
        if(rB_native_English.isChecked()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("mativeLanguage")
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



    }




    public  void btn_next_clicked(View v) {

        addUserDataToDatabase();
        Intent i =  new Intent(UserDetailsActivity.this, MainActivity.class);
        startActivity(i);
    }
}
