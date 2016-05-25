package com.sixthsemester.project.displace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Signup_selection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_selection);
    }


    public void customer(View view){
        Intent intent = new Intent(Signup_selection.this, Signup.class);
        startActivity(intent);

    }

    public void Provider(View view){
        Intent intent = new Intent(Signup_selection.this, provider_signup.class);
       // finishActivity();
       //finishAffinity();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
