package de.hdm_stuttgart.foreignbuddy;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyProfilActivity extends AppCompatActivity{

    final ChatsFragment chat = new ChatsFragment();
    final ProfilFragment profil = new ProfilFragment();
    final MatchesFragment matches = new MatchesFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profil);

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
                        //Toast.makeText(MyProfilActivity.this, "Chats clicked",Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment, chat);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case R.id.action_profil:
                        //Toast.makeText(MyProfilActivity.this, "Profil clicked", Toast.LENGTH_SHORT).show();

                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment, profil);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case R.id.action_matches:
                        //Toast.makeText(MyProfilActivity.this, "Matches clicked", Toast.LENGTH_SHORT).show();

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
