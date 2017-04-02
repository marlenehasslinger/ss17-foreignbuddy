package de.hdm_stuttgart.foreignbuddy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MyProfileFragment extends Fragment {


    final ProfilFragment profil = new ProfilFragment();
    final LanguageFragment languages = new LanguageFragment();
    final LocationFragment location = new LocationFragment();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, profil);
        transaction.commit();

        //BottomNavigationFunction
        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.top_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_profile:
                        //Toast.makeText(MainActivity.this, "Chats clicked",Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment, profil);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case R.id.action_languages:
                        //Toast.makeText(MainActivity.this, "Profil clicked", Toast.LENGTH_SHORT).show();

                        transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment, languages);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case R.id.action_location:
                        //Toast.makeText(MainActivity.this, "Matches clicked", Toast.LENGTH_SHORT).show();

                        FragmentTransaction t3 = getActivity().getSupportFragmentManager().beginTransaction();
                        t3.replace(R.id.fragment, location);
                        t3.addToBackStack(null);
                        t3.commit();
                        break;
                }
                return true;
            }
        });



        return view;
    }
}
