package de.hdm_stuttgart.foreignbuddy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class MyProfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profil);

        BottomNavigationView bottomNavigationView =  (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_chats:
                        Toast.makeText(MyProfilActivity.this, "Chats",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_profil:
                        Toast.makeText(MyProfilActivity.this, "Profil",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_matches:
                        Toast.makeText(MyProfilActivity.this, "Matches",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

    }
}
