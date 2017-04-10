package de.hdm_stuttgart.foreignbuddy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.User;

public class UserDetailsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText userName;
    CheckBox cB_English, cB_German, cB_French, cB_Spanish;
    RadioButton rB_native_English, rB_native_German, rB_native_French, rB_native_Spanish;


    private ArrayList<String> languages = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);


        userName = (EditText) findViewById(R.id.text_userName);

        cB_English = (CheckBox) findViewById(R.id.cB_English);
        cB_French = (CheckBox) findViewById(R.id.cB_French);
        cB_German = (CheckBox) findViewById(R.id.cB_German);
        cB_Spanish = (CheckBox) findViewById(R.id.cB_Spanish);

        rB_native_English = (RadioButton) findViewById(R.id.rB_native_English);
        rB_native_French = (RadioButton) findViewById(R.id.rB_native_French);
        rB_native_German = (RadioButton) findViewById(R.id.rB_native_German);
        rB_native_Spanish = (RadioButton) findViewById(R.id.rB_native_Spanish);



    }







    public void addUserDataToDatabase(){

        User user = new User(firebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user);

        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .child("username")
                .setValue(userName.getText().toString());


        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .child("email")
                .setValue(firebaseAuth.getInstance().getCurrentUser().getEmail());



        //Für native language
        if(rB_native_English.isEnabled()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("NativeLanguage")
                    .setValue("English");
        }
        else if(rB_native_German.isEnabled()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("NativeLanguage")
                    .setValue("German");
        }
        if(rB_native_French.isEnabled()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("NativeLanguage")
                    .setValue("French");
        }
        else if(rB_native_Spanish.isEnabled()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("NativeLanguage")
                    .setValue("Spanish");
        }


    //Für languages
        if(cB_English.isChecked()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Languages")
                    .child("Language1")
                    .setValue("English");
        }
        if(cB_German.isChecked()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Languages")
                    .child("Language2")

                    .setValue("German");
        }
        if(cB_French.isChecked()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Languages")
                    .child("Language3")

                    .setValue("French");
        }

        if(cB_Spanish.isChecked()){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Languages")
                    .child("Language4")
                    .setValue("Spanish");
        }



    }




    public  void btn_next_clicked(View v) {

        addUserDataToDatabase();
        Intent i =  new Intent(UserDetailsActivity.this, MainActivity.class);
        startActivity(i);
    }
}
