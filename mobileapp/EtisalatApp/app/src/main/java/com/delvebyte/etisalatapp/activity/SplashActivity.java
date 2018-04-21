package com.delvebyte.etisalatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.delvebyte.etisalatapp.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startTimer();
    }

    /**
     * Starts the next activity with a little delay
     */
    private void startTimer() {
        Handler splashTimer = new Handler();
        splashTimer.postDelayed(new Runnable() {
            public void run() {
                Intent configIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(configIntent);
                finish();
            }
        }, SPLASH_DELAY);
    }
}
