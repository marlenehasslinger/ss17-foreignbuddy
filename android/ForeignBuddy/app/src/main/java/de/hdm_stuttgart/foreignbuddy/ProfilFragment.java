package de.hdm_stuttgart.foreignbuddy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
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
import java.util.List;
import java.util.Locale;


import static android.app.Activity.RESULT_OK;

public class ProfilFragment extends Fragment implements View.OnClickListener {

    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 234;
    static final int CAM_REQUEST =1;
    static final int CAMERA_REQUEST_CODE = 10;
    static final int WRITE_EXTERNAL_REQUEST_CODE = 20;
    private Button btn_choosePhoto, btn_uploadPhoto, btn_takePhoto, btn_logOut;
    private TextView txt_userName;
    private TextView txt_location_profil;
    private ImageView imageView;
    private Uri filepath;
    private StorageReference riversRef;
    private Uri downloadUri;
    private FirebaseAuth firebaseAuth;
    private File localFile = null;
    ProgressDialog progressDialog;
    private String uploadName;
    private File folder;

    //Toolbar
    Toolbar toolbar;

    // GPS Start
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int LOCATION_REQUEST_CODE = 22;
    Geocoder geocoder;
    List<Address> addresses;
    //GPS End

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
        txt_location_profil = (TextView) view.findViewById(R.id.txt_location_user);



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

        //Start GPS
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    //String state = addresses.get(0).getAdminArea();
                    //String country = addresses.get(0).getCountryName();
                    //String postalCode = addresses.get(0).getPostalCode();
                    //String knownName = addresses.get(0).getFeatureName();
                    txt_location_profil.setText(address + ", " + city);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        getLocation();
        //GPS END

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_profil);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("MyProfil");
        toolbar.setTitleTextColor(Color.WHITE);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.toolbar_profil_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //GPS functions Start
    private void getLocation(){

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                ==PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

            locationManager.requestLocationUpdates("gps", 0, 0, locationListener);


        } else {

            Log.d("Permission", "Camera External or Write External Permission Permission denied");
            String [] permissionRequested = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permissionRequested, LOCATION_REQUEST_CODE);


        }
    }
    //GPS Functions END

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

        if( ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

            Log.d("Permission", "Camera + Write External Permission granted");
            invokeCamera();

        }    else {

            Log.d("Permission", "Camera External or Write External Permission Permission denied");
            String [] permissionRequested = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissionRequested, CAMERA_REQUEST_CODE);




        }

        /*

        else if (!(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)){

                Log.d("Permission", "Camera External Permission denied");
                String [] permissionRequested = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissionRequested, CAMERA_REQUEST_CODE);




        }


        else if (!(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){

            Log.d("Permission", "Write External Permission denied");

            String [] permissionRequested = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissionRequested, WRITE_EXTERNAL_REQUEST_CODE);

        }

        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==CAMERA_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
            } else {

                Log.d("Permissions", "Camera Permission denied still");
            }
        }

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Log.d("Permissions", "Location Permissions denied still");
            }
        }

        /*
        if(requestCode==WRITE_EXTERNAL_REQUEST_CODE){
            if(grantResults[1] == PackageManager.PERMISSION_GRANTED){
                invokeCamera();
            } else {

                Log.d("Permissions", "Write External Permission denied still");


            }
        }*/
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
