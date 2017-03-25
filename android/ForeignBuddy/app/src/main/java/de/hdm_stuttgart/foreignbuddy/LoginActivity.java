package de.hdm_stuttgart.foreignbuddy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {



    private EditText txt_Email_Login;
    private EditText txt_Password_Login;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_Email_Login = (EditText) findViewById(R.id.txt_Email_Login);
        txt_Password_Login = (EditText) findViewById(R.id.txt_Password_Login);
        /* firebaseAuth = FirebaseAuth.getInstance();

        startActivityForResult(AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setProviders(
                AuthUI.FACEBOOK_PROVIDER,
                AuthUI.GOOGLE_PROVIDER,
                AuthUI.EMAIL_PROVIDER
        )
        .build(), 1); */





    }

    public void btnRegistration_Click(View v) {
        Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(i);
    }

    public void btnLogin_Click(View v) {
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "Processing...", true);

        (firebaseAuth.signInWithEmailAndPassword(txt_Email_Login.getText().toString(), txt_Password_Login.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                            Intent i =  new Intent(LoginActivity.this, MyProfilActivity.class);
                            i.putExtra("EMail", firebaseAuth.getCurrentUser().getEmail()); //Name statt E-Mail
                            startActivity(i);
                        } else {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

}
