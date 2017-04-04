package de.hdm_stuttgart.foreignbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.auth.FirebaseAuth;

import static android.R.id.list;


public class ChatsFragment extends Fragment {

    ListView chatOverview;
    private FirebaseListAdapter<ContactChat> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        chatOverview = (ListView)view.findViewById(R.id.ChatsOverview);

        chatOverview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent intent = new Intent(getActivity(),ChatActivity.class);
                getActivity().startActivity(intent);


            }
        });

        String[] namen = {"Jens", "Wilhelm", "Harald"};
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, namen);
        chatOverview.setAdapter(ad);

        //displayContactChat();

        return view;
    }


/*
    private void displayContactChat() {

        chatOverview = (ListView) getActivity().findViewById(R.id.ChatsOverview);

        adapter = new FirebaseListAdapter<ContactChat>(this, ContactChat.class, R.layout.contactchat,
                FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ContactChat model, int position) {

                TextView contactname = (TextView)v.findViewById(R.id.name);
                ImageView contactpicture = (ImageView)v.findViewById(R.id.contactPicture);


                contactname.setText(model.getContactname());
                //contactpicture.setImageResource(model.contactPicture);
            }
        };

        displayContactChat().setAdapter(adapter);
    }

*/
}
