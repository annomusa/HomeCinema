package com.musa.raffi.hboschedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: SplashActivity");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
