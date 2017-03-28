package de.hdm_stuttgart.foreignbuddy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import android.view.View;


import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyProfilActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profil);

        //BottomNavigationFunction
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_chats:
                        //Toast.makeText(MyProfilActivity.this, "Chats clicked",Toast.LENGTH_SHORT).show();
                        ChatsFragment chatsFragment = new ChatsFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment, chatsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case R.id.action_profil:
                        //Toast.makeText(MyProfilActivity.this, "Profil clicked", Toast.LENGTH_SHORT).show();
                        ProfilFragment profilFragment = new ProfilFragment();
                        FragmentTransaction t2 = getSupportFragmentManager().beginTransaction();
                        t2.replace(R.id.fragment, profilFragment);
                        t2.addToBackStack(null);
                        t2.commit();
                        break;
                    case R.id.action_matches:
                        Toast.makeText(MyProfilActivity.this, "Matches clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

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
