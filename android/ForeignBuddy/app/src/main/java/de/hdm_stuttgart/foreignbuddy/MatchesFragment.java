package de.hdm_stuttgart.foreignbuddy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import static de.hdm_stuttgart.foreignbuddy.R.layout.matches;

public class MatchesFragment extends Fragment {

    List<User> matches;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_matches);

        matches = new ArrayList<>();
        matches.add(new User("Jan-Niklas Dittrich", "Backnang", "English"));
        matches.add(new User("Marlene Hasslinger", "Ludwigsburg", "English"));
        matches.add(new User("Marc-Julian Fleck", "Weinstadt", "English"));
        matches.add(new User("Max Mustermann", "Stuttgart", "English"));
        matches.add(new User("John Doe", "Stuttgart", "English"));
        matches.add(new User("Ansgar Gerlicher","Stuttgart", "English"));

        ArrayAdapter<User> matchesAdapter = new UserListAdapter();
        listView.setAdapter(matchesAdapter);

        return view;
    }

    private class UserListAdapter extends ArrayAdapter<User>{

        public UserListAdapter(){
            super(getActivity(), R.layout.matches, matches);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = convertView;
            if (convertView == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.matches, parent, false);

            }

            User currentUser = matches.get(position);

            TextView name = (TextView) view.findViewById(R.id.txt_name_matches);
            TextView location = (TextView) view.findViewById(R.id.txt_location_matches);
            TextView language = (TextView) view.findViewById(R.id.txt_language_matches);

            name.setText(currentUser.getName());
            location.setText(currentUser.getLocation());
            language.setText(currentUser.getLanguage());


            return view;
        }
    }

}
