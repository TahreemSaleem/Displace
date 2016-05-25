package com.sixthsemester.project.displace;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Toast;

/**
 * Created by hira on 5/14/2016.
 */
public class OpenAd extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_details);
        Toast.makeText(getApplicationContext(), "In Open Ad" , Toast.LENGTH_SHORT).show();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * .7),(int)( height * .7));


    }

}