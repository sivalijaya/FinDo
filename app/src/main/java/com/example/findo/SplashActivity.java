package com.example.findo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //todo change intent to home screen
                Intent intent = new Intent(SplashActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}