package de.hdm_stuttgart.foreignbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Placeholder_1Activity extends AppCompatActivity {


    private TextView lbl_Welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeholder_1);
        lbl_Welcome = (TextView) findViewById(R.id.lbl_Welcome);
        lbl_Welcome.setText(getIntent().getExtras().getString("EMail"));
    }
}
