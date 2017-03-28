package de.hdm_stuttgart.foreignbuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MatchesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_matches);
        String[] matches = {"Jan-Niklas Dittrich", "Marlene Hasslinger", "Marc-Julian Fleck"};

        ArrayAdapter<String> matchesAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                matches
                );

        listView.setAdapter(matchesAdapter);

        return view;
    }



}
