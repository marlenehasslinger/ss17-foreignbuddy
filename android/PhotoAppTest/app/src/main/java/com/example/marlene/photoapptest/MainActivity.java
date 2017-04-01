package com.example.marlene.photoapptest;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button button;
    ImageView imageView;
    static final int CAM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageView);


button.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View v){

        Intent camera_Intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getFile();
        camera_Intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(camera_Intent, CAM_REQUEST);
    }

});





    }


    private File getFile(){

        File folder = new File ("sdcard/camera_app");

        if(!folder.exists()){
            folder.mkdir();
        }

        File image_file = new File(folder, "image.jpg");
        return image_file;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = "sdcard/camera_app/image.jpg";
        imageView.setImageDrawable(Drawable.createFromPath(path));


    }
}
