package de.hdm_stuttgart.foreignbuddy.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdm_stuttgart.foreignbuddy.Activities.LogInActivity;
import de.hdm_stuttgart.foreignbuddy.Activities.UserDetailsActivity;
import de.hdm_stuttgart.foreignbuddy.Database.Advertisement;
import de.hdm_stuttgart.foreignbuddy.Database.DatabaseUser;
import de.hdm_stuttgart.foreignbuddy.R;
import de.hdm_stuttgart.foreignbuddy.Users.User;
import de.hdm_stuttgart.foreignbuddy.UtilityClasses.GPS;
import de.hdm_stuttgart.foreignbuddy.UtilityClasses.RotateBitmap;

import static android.app.Activity.RESULT_OK;

public class ProfilFragment extends Fragment implements View.OnClickListener {

    static final int CAM_REQUEST = 1;
    static final int CAMERA_REQUEST_CODE = 10;
    static final int LOCATION_REQUEST_CODE = 22;
    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int FILE_CHOOSER_REQUEST = 224;
    private static final String APP_TAG = "CAMERA_TEST_APP";
    //Initialize size for thumbnails
    final int THUMBSIZE = 64;
    Geocoder geocoder;
    List<Address> addresses;
    User myUser;
    //Für Fileprovider
    File fileWritten;
    private String photoFileName;
    private StorageReference storageReference;
    private Button btn_choosePhoto, btn_takePhoto;
    private TextView txt_userName;
    private TextView txt_location_profil;
    private TextView txt_nativeLanguage;
    private TextView txt_languages;
    private ImageView imageView;
    // private Uri downloadUri;
    private Uri filepath;
    private String uploadName;
    private StorageReference riversRef;
    private ProgressDialog progressDialog;
    private String manufacturer;

    //Toolbar
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        //Widgets
        imageView = (ImageView) view.findViewById(R.id.imageView);
        btn_choosePhoto = (Button) view.findViewById(R.id.btn_choosePhoto);
        btn_takePhoto = (Button) view.findViewById(R.id.btn_takePhoto);
        txt_userName = (TextView) view.findViewById(R.id.txt_userName);
        txt_location_profil = (TextView) view.findViewById(R.id.txt_location_user);
        txt_languages = (TextView) view.findViewById(R.id.txt_languages);
        txt_nativeLanguage = (TextView) view.findViewById(R.id.txt_nativeLanguage);

        //Set buttons Listener
        btn_choosePhoto.setOnClickListener(this);
        btn_takePhoto.setOnClickListener(this);

        //Loading an Ad
        Advertisement.getInstance().setAd(getContext());


        //Set filename for firebase up- and downloads and local files
        uploadName = FirebaseAuth.getInstance().getCurrentUser().getEmail() + "_profilePhoto";
        photoFileName = "photo.jpg";

        //Firebase database
        storageReference = FirebaseStorage.getInstance().getReference();
        riversRef = storageReference.child("images/" + uploadName);

        //Set application context
        DatabaseUser.getInstance().setContext(getActivity().getApplicationContext());

        //Get current User
        myUser = DatabaseUser.getInstance().getCurrentUser();

        //Set current user values on widgets
        txt_userName.setText(myUser.getUsername());
        txt_nativeLanguage.setText(myUser.getNativeLanguage());
        txt_languages.setText(myUser.getLanguage());

        //Figure out the manufacturer of device
        manufacturer = android.os.Build.MANUFACTURER;

        //Set profile photo
        try {
            imageView.setImageDrawable(Drawable.createFromPath(DatabaseUser.getInstance().getCurrentUser().getProfilePhoto()
                    .getAbsolutePath()));
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.user_male);
            Log.d("Download", "Current profil photo successfully downloaded and displayed");
        }

        if (myUser.lastKnownCity != null) {
            txt_location_profil.setText(myUser.lastKnownCity);
        }

        //Get City
        GPS.getInstance().setContext(getActivity().getApplicationContext());
        GPS.getInstance().setActivity(getActivity());
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Set city
                String locationCity = intent.getStringExtra("city");
                txt_location_profil.setText(locationCity);
            }
        }, new IntentFilter(GPS.LOCATION_UPDATED));

        getLocation();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Set toolbar
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_profil);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Profile");
        setHasOptionsMenu(true);
    }

    //Toolbar methods
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

    //GPS functions Start
    private void getLocation() {

        //Check if permissions are already given
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

            //If permissions are granted already the location request starts
            GPS.getInstance().startLocationRequest();

        } else {

            //Missing permissions will be requested
            Log.d("Permission", "Location Permission denied");
            String[] permissionRequested = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permissionRequested, LOCATION_REQUEST_CODE);
        }
    }

    private void showFileChooser() {

        //Checks if required permissions are already given
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

            Log.d("Permission", "Read + Write External Permission granted");


            //If permissions are granteed File chooser will be started
            startFileChooser();

        } else {

            //Missing permissions will be requested
            Log.d("Permission", "Camera External or Write External Permission Permission denied");
            String[] permissionRequested = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissionRequested, FILE_CHOOSER_REQUEST);


        }


    }

    public void startFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);
        Log.d("Files", "User chose File for upload");

    }

    public void startCamera() {

        Intent camera_Intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("Camera", "Camera started");
        camera_Intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName));
        startActivityForResult(camera_Intent, CAM_REQUEST);
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
            startCamera();

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
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {

                Log.d("Permissions", "Camera Permission denied still");
            }
        }

        //Asks for permissions that are needed for file chooser usage
        if (requestCode == FILE_CHOOSER_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startFileChooser();

            } else {

                Log.d("Permissions", "Camera Permission denied still");
            }
        }

        //Asks for Location permission
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                GPS.getInstance().startLocationRequest();
            } else {
                Log.d("Permissions", "Locations Permissions still denied");
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //User picks profile photo from gallery
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();


            try {
                Bitmap takenImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);

                //Find ratio to scale
                final int maxSize = 800;
                int outWidth;
                int outHeight;
                int inWidth = takenImage.getWidth();
                int inHeight = takenImage.getHeight();
                if (inWidth > inHeight) {
                    outWidth = maxSize;
                    outHeight = (inHeight * maxSize) / inWidth;
                } else {
                    outHeight = maxSize;
                    outWidth = (inWidth * maxSize) / inHeight;
                }
                Log.e("Bild", "Orig. size: h:" + inHeight + ",w:" + inWidth + " New size: h:" + outHeight + ",w: " + outWidth);
                takenImage = Bitmap.createScaledBitmap(takenImage, outWidth, outHeight, false);

                //Create a new file so the original file won't be changed during compressing process
                File file = getPhotoFile(uploadName);

                //Compress ProfilePhoto
                FileOutputStream fOut = new FileOutputStream(file);
                takenImage.compress(Bitmap.CompressFormat.JPEG, 20, fOut);
                fOut.flush();
                fOut.close();


                // Load the taken image into a preview
                imageView.setImageBitmap(takenImage);
                //Set new Profile photo to current User
                DatabaseUser.getInstance().getCurrentUser().setProfilePhoto(file);
                filepath = Uri.fromFile(file);
                uploadProfilePhoto(filepath);

            } catch (IOException e) {
                e.printStackTrace();
            }


            //User takes profile photo with camera
        } else if (requestCode == CAM_REQUEST) {

            if (resultCode == RESULT_OK) {

                //Set bitmap with the file that was created when the photo was taken with camera
                Bitmap takenImage = BitmapFactory.decodeFile(fileWritten.getAbsolutePath());

                try {


                    //Find ratio to scale
                    final int maxSize = 800;
                    int outWidth;
                    int outHeight;
                    int inWidth = takenImage.getWidth();
                    int inHeight = takenImage.getHeight();

                    if (inWidth > inHeight) {
                        outWidth = maxSize;
                        outHeight = (inHeight * maxSize) / inWidth;
                    } else {
                        outHeight = maxSize;
                        outWidth = (inWidth * maxSize) / inHeight;
                    }


                    //To fix a samsung camera orientation bug, the images for samsung devies will be rotated
                    if ("samsung".equals(manufacturer)) {

                        ExifInterface exif = new ExifInterface(fileWritten.getAbsolutePath());
                        Log.e("orientation", "image needs to be rotated. Exif:" + exif.getAttribute(ExifInterface.TAG_ORIENTATION));

                        //For photos taken in portrait mode
                        if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equals("6")) {
                            takenImage = RotateBitmap.RotateBitmap(takenImage, 90);
                            Log.e("orientation", "image rotated");


                        }

                        //For photos taken in landscape mode
                        if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equals("3")) {
                            takenImage = RotateBitmap.RotateBitmap(takenImage, 180);
                            Log.e("orientation", "image rotated");


                        }

                        //For photos taken with front cam
                        if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equals("8")) {
                            takenImage = RotateBitmap.RotateBitmap(takenImage, 270);
                            Log.e("orientation", "image rotated");


                        }
                    }


                    //Compress ProfilePhoto
                    FileOutputStream fOut = new FileOutputStream(fileWritten);
                    takenImage.compress(Bitmap.CompressFormat.JPEG, 20, fOut);
                    fOut.flush();
                    fOut.close();


                    Log.e("Bild", "Orig. size: h:" + inHeight + ",w:" + inWidth + " New size: h:" + outHeight + ",w: " + outWidth);

                    takenImage = Bitmap.createScaledBitmap(takenImage, outWidth, outHeight, false);

                    Log.e("Bild", "Scale image size: h: " + takenImage.getHeight() + ", w: " + takenImage.getWidth());


                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Load the taken image into a preview
                imageView.setImageBitmap(takenImage);
                DatabaseUser.getInstance().getCurrentUser().setProfilePhoto(fileWritten);
                filepath = Uri.fromFile(fileWritten);


                //upload photo to Firebase
                uploadProfilePhoto(filepath);


            } else {

                // Result was a failure
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void uploadProfilePhoto(Uri filepath) {

        if (filepath != null) {

            String downloadName = FirebaseAuth.getInstance().getCurrentUser().getEmail() + "_profilePhoto";
            storageReference = FirebaseStorage.getInstance().getReference();
            riversRef = storageReference.child("images/" + downloadName);


            riversRef.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Photo is successfully uploaded

                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    Log.d("Upload", "Upload successful");
                    Toast.makeText(getActivity(), "File Uploaded!", Toast.LENGTH_SHORT).show();

                    //Downloadink to profile photo will be stored within the corresponding user in the database
                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("urlProfilephoto")
                            .setValue(downloadUri.toString());
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //Photo wasn't successfully uploaded
                            Log.d("Upload", "Upload failed");
                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {

            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getPhotoFileUri(String fileName) {
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
            // File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
            // wrap File object into a content provider
            // required for API >= 24
            Uri fileuri = FileProvider.getUriForFile(getActivity(), "com.codepath.fileprovider", file);
            return fileuri;
        }
        return null;
    }

    private File getPhotoFile(String fileName) {
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
            // File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
            // wrap File object into a content provider
            // required for API >= 24
            // Uri fileuri = FileProvider.getUriForFile(getActivity(), "com.codepath.fileprovider", file);
            return file;
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
        DatabaseUser.removeActualInstance();
        FirebaseAuth.getInstance().signOut();
        Log.d("Auth", FirebaseAuth.getInstance().getCurrentUser() + "is logged out");
        Intent i = new Intent(getActivity(), LogInActivity.class);
        startActivity(i);


    }

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
