package de.hdm_stuttgart.foreignbuddy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;


import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfilFragment extends Fragment implements View.OnClickListener {

    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 234;
    static final int CAM_REQUEST =1;
    private Button btn_choosePhoto, btn_uploadPhoto, btn_takePhoto;
    private ImageView imageView;
    private Uri filepath;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        imageView = (ImageView) view.findViewById(R.id.imageView);
        btn_choosePhoto = (Button) view.findViewById(R.id.btn_choosePhoto);
        btn_uploadPhoto = (Button) view.findViewById(R.id.btn_uploadPhoto);
        btn_takePhoto = (Button) view.findViewById(R.id.btn_takePhoto);

        btn_choosePhoto.setOnClickListener(this);
        btn_uploadPhoto.setOnClickListener(this);
        btn_takePhoto.setOnClickListener(this);


       storageReference = FirebaseStorage.getInstance().getReference();

        return view;
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

        } else if (requestCode ==CAM_REQUEST && resultCode == RESULT_OK){
            String path ="sdcard/ForeignBuddy/profilepic.jpg";
            imageView.setImageDrawable(Drawable.createFromPath(path));
            filepath = Uri.fromFile(new File(path));

        }



    }


    private void uploadFile(){

        if(filepath != null) {

          // final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext()); //?
            //progressDialog.setTitle("Uploading");
            //progressDialog.show();


            StorageReference riversRef = storageReference.child("images/profile.jpg");
            riversRef.putFile(filepath)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Log.d("photo", "sucess");

                            //     progressDialog.dismiss();
                          //  Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                    //       progressDialog.dismiss();

                            Log.d("Photo", "fail");


                            //  Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    })

                    /*
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            })
           */


            ;


        } else {
            //display error toast !!TODO
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);


    }

    public void takePhoto(){
        Intent camera_Intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getFile();
        camera_Intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(camera_Intent, CAM_REQUEST);

    }

    private File getFile() {

        File folder = new File("sdcard/ForeignBuddy");

        if(!folder.exists()){
            folder.mkdir();
        }

        File image_file = new File(folder, "profilepic.jpg");
        return image_file;
    }


    @Override
    public void onClick(View view) {
        if (view == btn_choosePhoto) {
            showFileChooser();
        } else if (view == btn_uploadPhoto) {
            uploadFile();
        } else if (view == btn_takePhoto){
            takePhoto();
        }
    }

    public void btnLogoutClick(View v){
       /* AuthUI.getInstance()
                .signOut(MyProfilActivity) //beendet aktuelle aktivität
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("AUTH", "User logged out");
                        finish();
                    }
                });*/
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getActivity(), LogInActivity.class);
        startActivity(i);
    }

}
