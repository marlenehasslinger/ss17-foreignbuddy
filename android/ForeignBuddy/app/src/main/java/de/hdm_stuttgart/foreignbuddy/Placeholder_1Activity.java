package de.hdm_stuttgart.foreignbuddy;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Placeholder_1Activity extends AppCompatActivity {


    private TextView lbl_Welcome;
    Button btn_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeholder_1);
        lbl_Welcome = (TextView) findViewById(R.id.lbl_Welcome);
        lbl_Welcome.setText(getIntent().getExtras().getString("EMail"));

    }


    public void btnLogoutClick(View v){
        AuthUI.getInstance()
                .signOut(this) //beendet aktuelle aktivität
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               Log.d("AUTH", "User logged out");
                                               finish();
                                           }
                });

    }
}

