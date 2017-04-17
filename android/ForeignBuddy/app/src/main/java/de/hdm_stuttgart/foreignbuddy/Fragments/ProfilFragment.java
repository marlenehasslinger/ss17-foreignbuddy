package de.hdm_stuttgart.foreignbuddy.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdm_stuttgart.foreignbuddy.Activities.LogInActivity;
import de.hdm_stuttgart.foreignbuddy.Activities.UserDetailsActivity;
import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.User;

import static android.app.Activity.RESULT_OK;

public class ProfilFragment extends Fragment implements View.OnClickListener, LocationListener {

    static final int CAM_REQUEST = 1;
    static final int CAMERA_REQUEST_CODE = 10;
    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int LOCATION_REQUEST_CODE = 22;
    private static final String APP_TAG = "CAMERA_TEST_APP";
    //Initialize size for thumbnails
    final int THUMBSIZE = 64;
    public String photoFileName;
    Geocoder geocoder;
    List<Address> addresses;
    User myUser;
    //Für Fileprovider
    File fileWritten;
    DatabaseReference mDatabase;
    private StorageReference storageReference;
    private Button btn_choosePhoto, btn_takePhoto;
    private TextView txt_userName;
    private TextView txt_location_profil;
    private TextView txt_nativeLanguage;
    private TextView txt_languages;
    private ImageView imageView;
    private Uri downloadUri;
    private Uri filepath;
    private String uploadName;
    private File localFile = null;
    private FirebaseAuth firebaseAuth;
    private StorageReference riversRef;
    private ProgressDialog progressDialog;
    //Toolbar
    private Toolbar toolbar;
    // GPS Start
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        //START WIDGETS
        imageView = (ImageView) view.findViewById(R.id.imageView);
        btn_choosePhoto = (Button) view.findViewById(R.id.btn_choosePhoto);
        btn_takePhoto = (Button) view.findViewById(R.id.btn_takePhoto);
        txt_userName = (TextView) view.findViewById(R.id.txt_userName);
        txt_location_profil = (TextView) view.findViewById(R.id.txt_location_user);
        txt_languages = (TextView) view.findViewById(R.id.txt_languages);
        txt_nativeLanguage = (TextView) view.findViewById(R.id.txt_nativeLanguage);
        //END WIDGETS

        //START BUTTON LISTENER
        btn_choosePhoto.setOnClickListener(this);
        btn_takePhoto.setOnClickListener(this);
        //END BUTTON LISTENER

        //START FIREBASE INSTANCES
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //END FIREBASE INSTANCES

        uploadName = firebaseAuth.getCurrentUser().getEmail() + "_profilePhoto";

        photoFileName = "photo.jpg";

        storageReference = FirebaseStorage.getInstance().getReference();
        riversRef = storageReference.child("images/" + uploadName);

        mDatabase.child("users")
                .child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myUser = dataSnapshot.getValue(User.class);
                txt_userName.setText(myUser.username);
                txt_nativeLanguage.setText(myUser.getNativeLanguage());
                txt_languages.setText(myUser.getLanguage());
                toolbar.setTitle(myUser.username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Set current profile photo
        try {
            downloadProfilePhoto();
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
            Log.d("Download", "Current profil photo successfully downloaded and displayed");
        }

        //Start LOCATION
        getLocation();
        //END LOCATION

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        //START TOOLBAR
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_profil);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("");
        setHasOptionsMenu(true);
        //END TOOLBAR
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    //Toolbar functions Starts
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.toolbar_profil_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tb_logout_profil:
                askForLogout();
                break;
            case R.id.tb_settings_profile:
                Intent i = new Intent(getActivity(), UserDetailsActivity.class);
                startActivity(i);
        }
        return true;
    }
    //Toolbar functions END

    //GPS functions Start
    private void getLocation() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
                Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (l == null) {
                    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
                        l = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                    if (l != null) {
                        onLocationChanged(l);
                    } else {
                        //txt_location_profil.setText(myUser.lastKnownCity);
                        showSettingsAlertForGPS();
                    }
                } else {
                    onLocationChanged(l);
                }
            } else {
                //txt_location_profil.setText(myUser.lastKnownCity);
                showSettingsAlertForGPS();
            }

        } else {
            Log.d("Permission", "Camera External or Write External Permission Permission denied");
            String[] permissionRequested = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permissionRequested, LOCATION_REQUEST_CODE);
        }
    }

    private void showSettingsAlertForGPS() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("GPS is off");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                String city = addresses.get(0).getLocality();
                txt_location_profil.setText("in " + city);
                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("latitude").setValue(location.getLatitude());
                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("longitude").setValue(location.getLongitude());
                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("lastKnownCity").setValue(city);
            }

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

    }

    //GPS Functions END


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

        //User picks profile photo from gallery
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();

            try {

                //Load the photo into a preview
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                imageView.setImageBitmap(bitmap);

                //upload photo to Firebase
                uploadFile();

            } catch (IOException e) {
                e.printStackTrace();
            }


            //User takes profile photo with camera
        } else if (requestCode == CAM_REQUEST) {

            if (resultCode == RESULT_OK) {

                Bitmap takenImage = BitmapFactory.decodeFile(fileWritten.getAbsolutePath());

                // Load the taken image into a preview
                imageView.setImageBitmap(takenImage);
                filepath = Uri.fromFile(fileWritten);

                //upload photo to Firebase
                uploadFile();


            } else {

                // Result was a failure
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void uploadFile() {

        if (filepath != null) {

            progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);

            riversRef.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Photo is successfully uploaded
                    Log.d("Upload", "Upload successful");
                    Toast.makeText(getActivity(), "File Uploaded!", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                    downloadUri = taskSnapshot.getDownloadUrl();


                    //Link to profile photo file will be stored within the corresponding user in the database
                    mDatabase.child("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("urlProfilephoto")
                            .setValue(downloadUri.toString());

                    //Picasso.with(getContext()).load(downloadUri).into(imageView);
                    // Log.d("Picasso", "Set Photo with Picasso successful");


                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            //Photo wasn't successfully uploaded

                            Log.d("Upload", "Upload failed");


                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {

            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);
        Log.d("Files", "User chose File for upload");


    }

    public void takePhoto() {

        //Checks if required permissions are already given
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

            Log.d("Permission", "Camera + Write External Permission granted");

            //Starts camera
            invokeCamera();

        } else {

            //Missing permissions will be requested
            Log.d("Permission", "Camera External or Write External Permission Permission denied");
            String[] permissionRequested = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissionRequested, CAMERA_REQUEST_CODE);


        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Asks for permissions that are needed for camera usage
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
            } else {

                Log.d("Permissions", "Camera Permission denied still");
            }
        }

        //Asks for Location permission
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Log.d("Permissions", "Locations Permissions denied still");
            }
        }

    }


    private void invokeCamera() {

        //Starts Camera Intent
        Intent camera_Intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("Camera", "Camera started");
        camera_Intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName));
        startActivityForResult(camera_Intent, CAM_REQUEST);
    }

    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
            // getExternalFilesDir() + "/Pictures" should match the declaration in fileprovider.xml paths
            File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            //store file to load later
            fileWritten = file;
            // wrap File object into a content provider. NOTE: authority here should match authority in manifest declaration
            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(APP_TAG, "failed to create directory");
            }
            // Return the file target for the photo based on filename
            //File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
            // wrap File object into a content provider
            // required for API >= 24
            Uri fileuri = FileProvider.getUriForFile(getActivity(), "com.codepath.fileprovider", file);
            return fileuri;
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    //Logout function Start
    private void askForLogout() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to Sign Out?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert) // Set own Icon!
                .show();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Log.d("Auth", FirebaseAuth.getInstance().getCurrentUser() + "is logged out");
        Intent i = new Intent(getActivity(), LogInActivity.class);
        startActivity(i);


    }
    //Logout function END


    //OnClickListener
    @Override
    public void onClick(View view) {
        if (view == btn_choosePhoto) {
            showFileChooser();
        } else if (view == btn_takePhoto) {
            takePhoto();
        }
    }


}
