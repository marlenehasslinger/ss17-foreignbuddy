package de.hdm_stuttgart.foreignbuddy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.User;





public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        firebaseAuth = FirebaseAuth.getInstance();


        /*

        //Wird schon in LogIn Activity geprüft

        if(firebaseAuth.getCurrentUser() != null) {

            Log.d("AUTH", firebaseAuth.getCurrentUser().getEmail());

            Intent i =  new Intent(RegistrationActivity.this, MainActivity.class);
            i.putExtra("EMail", firebaseAuth.getCurrentUser().getEmail()); //Name statt E-Mail
            startActivity(i);



        } else {

            */

            //startActivityForResult ist ein Intent, der dann als Result gibt ob User authenticated ist
            // oder nicht. Das Result wird dann unten wiederverwendet um zu checken ob user authenticated ist

            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setProviders(
                            AuthUI.FACEBOOK_PROVIDER,
                            AuthUI.GOOGLE_PROVIDER,
                            AuthUI.EMAIL_PROVIDER
                    )
                    .build(), RC_SIGN_IN);
        }

   // }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                //user logged in
                Log.d("AUTH", firebaseAuth.getCurrentUser().getEmail());

                Intent i =  new Intent(RegistrationActivity.this, MainActivity.class);
                i.putExtra("EMail", firebaseAuth.getCurrentUser().getEmail()); //Name statt E-Mail
                startActivity(i);

                addUserToDatabase();


            } else {
                //User not authenticated
                Log.d("Auth", "Not Authenticated");

            }
        }

    }

    public void addUserToDatabase(){

        User user = new User(firebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user);


    }







}
