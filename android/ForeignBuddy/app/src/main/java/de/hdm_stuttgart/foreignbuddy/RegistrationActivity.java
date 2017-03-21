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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {


    private EditText txt_Email_Registration;
    private EditText txt_Password_Registration;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        txt_Email_Registration = (EditText) findViewById(R.id.txt_Email_Registration);
        txt_Password_Registration = (EditText) findViewById(R.id.txt_Password_Registration);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void btnRegistration_Click(View v) {
        final ProgressDialog progressDialog = ProgressDialog.show(RegistrationActivity.this, "Please wait...", "Processing...", true);
        (firebaseAuth.createUserWithEmailAndPassword(txt_Email_Registration.getText().toString(), txt_Password_Registration.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(i);
                } else {
                    Log.e("ERROR", task.getException().toString());
                    Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}
