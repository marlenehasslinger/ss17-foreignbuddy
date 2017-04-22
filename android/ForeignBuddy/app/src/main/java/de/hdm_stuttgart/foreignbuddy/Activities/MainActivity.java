package de.hdm_stuttgart.foreignbuddy.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import de.hdm_stuttgart.foreignbuddy.Database.DatabaseUser;
import de.hdm_stuttgart.foreignbuddy.Fragments.ChatsFragment;
import de.hdm_stuttgart.foreignbuddy.Fragments.MatchesFragment;
import de.hdm_stuttgart.foreignbuddy.Fragments.ProfilFragment;
import de.hdm_stuttgart.foreignbuddy.R;

public class MainActivity extends AppCompatActivity{

    //Declare Fragments
    final private ChatsFragment chat = new ChatsFragment();
    final private ProfilFragment profil = new ProfilFragment();
    final private MatchesFragment matches = new MatchesFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load current User
        DatabaseUser.InstanceCurrentUser();

        //Set First Fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, chat);
        transaction.commit();

        //BottomNavigationBar set Fragment
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_chats:
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment, chat);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case R.id.action_profil:
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment, profil);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case R.id.action_matches:
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment, matches);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                }
                return true;
            }
        });

    }

}
