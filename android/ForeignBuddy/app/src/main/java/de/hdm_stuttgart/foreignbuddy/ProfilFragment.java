package de.hdm_stuttgart.foreignbuddy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.*;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.Manifest;



import java.io.File;
import java.io.IOException;


import static android.app.Activity.RESULT_OK;

public class ProfilFragment extends Fragment implements View.OnClickListener {

    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 234;
    static final int CAM_REQUEST =1;
    static final int CAMERA_REQUEST_CODE = 10;
    static final int WRITE_EXTERNAL_REQUEST_CODE = 20;
    private Button btn_choosePhoto, btn_uploadPhoto, btn_takePhoto, btn_logOut;
    private TextView txt_userName;
    private ImageView imageView;
    private Uri filepath;
    private StorageReference riversRef;
    private Uri downloadUri;

    private FirebaseAuth firebaseAuth;

    private File localFile = null;

    ProgressDialog progressDialog;


    private String uploadName;

    private File folder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        imageView = (ImageView) view.findViewById(R.id.imageView);

        btn_choosePhoto = (Button) view.findViewById(R.id.btn_choosePhoto);
        btn_uploadPhoto = (Button) view.findViewById(R.id.btn_uploadPhoto);
        btn_takePhoto = (Button) view.findViewById(R.id.btn_takePhoto);
        btn_logOut = (Button) view.findViewById(R.id.btn_LogOut);
        txt_userName = (TextView) view.findViewById(R.id.txt_userName);



        btn_choosePhoto.setOnClickListener(this);
        btn_uploadPhoto.setOnClickListener(this);
        btn_takePhoto.setOnClickListener(this);
        btn_logOut.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        uploadName = firebaseAuth.getCurrentUser().getEmail() + "_profilePhoto";

        storageReference = FirebaseStorage.getInstance().getReference();
        riversRef = storageReference.child("images/" + uploadName);


        txt_userName.setText(usernameFromEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()));

        //Set current profile photo

        try {
            downloadProfilePhoto();
            } catch (Exception e){
            imageView.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
            Log.d("Download", "Current profil photo successfully downloaded and displayed");

        }


        return view;

    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void downloadProfilePhoto() {
        try {
            localFile = File.createTempFile("images", uploadName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {


                        imageView.setImageDrawable(Drawable.createFromPath(localFile.getPath()));


                        Log.d("Download", "Profil photo successfully downloaded");


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Log.d("Download", "Profil photo download failed");
            }
        });
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

        } else  if (requestCode ==CAM_REQUEST){
            String path ="sdcard/ForeignBuddyPhotos/profilepic.jpg";
            imageView.setImageDrawable(Drawable.createFromPath(path));
            filepath = Uri.fromFile(new File(path));

}

    }


    private void uploadFile(){

        if(filepath != null) {

           progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);

            //riversRef = storageReference.child("images/" + uploadName); --nach oben verschoben
            riversRef.putFile(filepath)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Log.d("Upload", "Upload successful");
                            Toast.makeText(getActivity(),"File Uploaded!",Toast.LENGTH_SHORT).show();

                                progressDialog.dismiss();

                            downloadUri = taskSnapshot.getDownloadUrl();
                            Picasso.with(getContext()).load(downloadUri).into(imageView);
                            Log.d("Picasso", "Set Photo with Picasso successful");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                           progressDialog.dismiss();

                            Log.d("Upload", "Upload failed");


                            Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    })


            ;


        } else {

            Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);
        Log.d("Files", "User chose File for upload");



    }

    public void takePhoto(){

        if( ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED) {

            Log.d("Permission", "Camera Permission granted");

            if( ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {

                Log.d("Permission", "Write External Permission Permission granted");

            invokeCamera();
            } else {

                Log.d("Permission", "Write External Permission denied");
                String [] permissionRequested = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissionRequested, WRITE_EXTERNAL_REQUEST_CODE);

            }

        } else {

            Log.d("Permission", "Camera Permission denied");

            String [] permissionRequested = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissionRequested, CAMERA_REQUEST_CODE);
            requestPermissions(permissionRequested, WRITE_EXTERNAL_REQUEST_CODE);

            invokeCamera();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==CAMERA_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                invokeCamera();
            } else {

                Log.d("Permissions", "Camera Permission denied still");


            }
        }

        if(requestCode==WRITE_EXTERNAL_REQUEST_CODE){
            if(grantResults[1] == PackageManager.PERMISSION_GRANTED){
                invokeCamera();
            } else {

                Log.d("Permissions", "Write External Permission denied still");


            }
        }

    }

    private void invokeCamera() {

        Intent camera_Intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("Camera", "Camera started");
        File file = getFile();
        camera_Intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(camera_Intent, CAM_REQUEST);
    }


    private File getFile() {

        folder = new File("sdcard/ForeignBuddyPhotos");

        if(folder == null){
            folder.mkdir();
            Log.d("Files", "'ForeignBuddyPhotos' folder was created");
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
        } else if (view == btn_logOut){
            logout();

        }
    }

    public void logout(){

        FirebaseAuth.getInstance().signOut();
        Log.d("Auth", FirebaseAuth.getInstance().getCurrentUser() + "is logged out");
        Intent i = new Intent(getActivity(), LogInActivity.class);
        startActivity(i);


    }

}
