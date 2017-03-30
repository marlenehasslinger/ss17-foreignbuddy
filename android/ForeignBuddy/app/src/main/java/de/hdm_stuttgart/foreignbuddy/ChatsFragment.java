package de.hdm_stuttgart.foreignbuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import static android.R.id.list;


public class ChatsFragment extends Fragment {

    ListView chatOverview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        chatOverview = (ListView)view.findViewById(R.id.ChatsOverview);

        /*chatOverview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemSelected(AdapterView parentView, View childView,
                                       int position, long id)
            {

            }

            public void onNothingSelected(AdapterView parentView) {

            }
        });

*/
        return view;
    }



}
