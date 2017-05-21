package de.hdm_stuttgart.foreignbuddy.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdm_stuttgart.foreignbuddy.Database.Advertisement;
import de.hdm_stuttgart.foreignbuddy.R;

public class LogInActivity extends AppCompatActivity {


    //Widgets
    private EditText txt_email_login;
    private EditText txt_password_login;
    private Button btn_login_login;
    private ProgressDialog progressDialog;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Load one Ad
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/2247696110");
        Advertisement.getInstance().setAd(getApplicationContext());

        //Widgets
        txt_email_login = (EditText) findViewById(R.id.txt_email_login);
        txt_password_login = (EditText) findViewById(R.id.txt_password_login);
        btn_login_login = (Button) findViewById(R.id.btn_login_login);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Checks if user already signed in and set AuthSateListener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Auth", "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent i = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    // User is signed out
                    Log.d("Auth", "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void btn_login_login_clicked(View v) {
        //Try Login
        if (!txt_email_login.getText().toString().equals("") && !txt_password_login.getText().toString().equals("")) {
            progressDialog = ProgressDialog.show(this, "Loading...", "Please wait...", true);
            mAuth.signInWithEmailAndPassword(txt_email_login.getText().toString(), txt_password_login.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("Auth", "signInWithEmail:onComplete:" + task.isSuccessful());
                            progressDialog.dismiss();

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Log.w("Auth", "signInWithEmail:failed", task.getException());
                                Toast.makeText(LogInActivity.this, "Wrong mail and/or wrong password",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        } else {
            Toast.makeText(LogInActivity.this, "Enter your mail and password or register now",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void btn_register_clicked(View v) {
        Log.d("Auth", "onAuthStateChanged:register:");
        Intent i = new Intent(LogInActivity.this, RegistrationActivity.class);
        startActivity(i);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
