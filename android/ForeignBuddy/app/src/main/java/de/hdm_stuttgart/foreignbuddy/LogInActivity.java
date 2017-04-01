package de.hdm_stuttgart.foreignbuddy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {


    //Widgets
    private EditText txt_email_login;
    private EditText txt_password_login;
    private Button btn_login_login;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Widgets
        txt_email_login = (EditText) findViewById(R.id.txt_email_login);
        txt_password_login = (EditText) findViewById(R.id.txt_password_login);
        btn_login_login = (Button) findViewById(R.id.btn_login_login);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Auth", "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent i =  new Intent(LogInActivity.this, MyProfilActivity.class);
                    startActivity(i);
                } else {
                    // User is signed out
                    Log.d("Auth", "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void btn_login_login_clicked(View v) {
        mAuth.signInWithEmailAndPassword(txt_email_login.getText().toString(), txt_password_login.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Auth", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Auth", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LogInActivity.this, "Failed", //TODO Massage
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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