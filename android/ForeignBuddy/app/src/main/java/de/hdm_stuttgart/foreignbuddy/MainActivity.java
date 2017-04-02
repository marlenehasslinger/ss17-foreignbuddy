package de.hdm_stuttgart.foreignbuddy;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity{

    final ChatsFragment chat = new ChatsFragment();
    final MyProfileFragment profil = new MyProfileFragment();
    final MatchesFragment matches = new MatchesFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, chat);
        transaction.commit();

        //BottomNavigationFunction
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_chats:
                        //Toast.makeText(MainActivity.this, "Chats clicked",Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment, chat);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case R.id.action_profil:
                        //Toast.makeText(MainActivity.this, "Profil clicked", Toast.LENGTH_SHORT).show();

                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment, profil);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case R.id.action_matches:
                        //Toast.makeText(MainActivity.this, "Matches clicked", Toast.LENGTH_SHORT).show();

                        FragmentTransaction t3 = getSupportFragmentManager().beginTransaction();
                        t3.replace(R.id.fragment, matches);
                        t3.addToBackStack(null);
                        t3.commit();
                        break;
                }
                return true;
            }
        });

    }



}
