package de.hdm_stuttgart.foreignbuddy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.*;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;


import static android.app.Activity.RESULT_OK;

public class ProfilFragment extends Fragment implements View.OnClickListener {

     /*
    String FIREBASE_URL;
    FirebaseStorage storage;
    StorageReference storageRef;
    */


    private static final int PICK_IMAGE_REQUEST = 234;
    private Button btn_choosePhoto, btn_uploadPhoto;
    private ImageView imageView;
    private Uri filepath;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        imageView = (ImageView) view.findViewById(R.id.imageView);
        btn_choosePhoto = (Button) view.findViewById(R.id.btn_choosePhoto);
        btn_uploadPhoto = (Button) view.findViewById(R.id.btn_uploadPhoto);
        btn_choosePhoto.setOnClickListener(this);
        btn_uploadPhoto.setOnClickListener(this);
        return view;

        /* FIREBASE_URL = "https://foreignbuddy-7b4d5.firebaseio.com/";
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        StorageReference imagesRef = storageRef.child("images");
        */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);


    }


    @Override
    public void onClick(View view) {
        if (view == btn_choosePhoto) {
            showFileChooser();
        } else if (view == btn_uploadPhoto) {

        }
    }

    /*public void btnLogoutClick(View v){
        AuthUI.getInstance()
                .signOut(MyProfilActivity) //beendet aktuelle aktivität
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("AUTH", "User logged out");
                        finish();
                    }
                });
    }*/

}
