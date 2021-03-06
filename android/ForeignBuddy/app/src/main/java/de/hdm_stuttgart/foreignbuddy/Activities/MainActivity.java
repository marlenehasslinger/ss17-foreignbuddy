package de.hdm_stuttgart.foreignbuddy.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import de.hdm_stuttgart.foreignbuddy.Database.DatabaseUser;
import de.hdm_stuttgart.foreignbuddy.Fragments.ChatsFragment;
import de.hdm_stuttgart.foreignbuddy.Fragments.MatchesFragment;
import de.hdm_stuttgart.foreignbuddy.Fragments.ProfilFragment;
import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.UtilityClasses.GPS;

public class MainActivity extends AppCompatActivity {


    public static final int LOCATION_REQUEST_CODE = 609596;

    private boolean firstLoading = true;

    //Declare Fragments
    final private ChatsFragment chat = new ChatsFragment();
    final private ProfilFragment profil = new ProfilFragment();
    final private MatchesFragment matches = new MatchesFragment();

    //Progress Dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Load current User
        DatabaseUser.getInstance().setContext(getApplicationContext()); //Instance Database User and set current Context

        progressDialog = ProgressDialog.show(this, "Loading", "Please wait...", true);
        DatabaseUser.getInstance().setInitalLoading(true);

        LocalBroadcastManager.getInstance(this.getApplicationContext()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Set First Fragment
                if (firstLoading == true) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.fragment, profil);
                    transaction.commit();
                    firstLoading = false;
                }
                progressDialog.dismiss();
            }
        }, new IntentFilter(DatabaseUser.FINISHED_LOADING));



        //BottomNavigationBar; Fragments
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

