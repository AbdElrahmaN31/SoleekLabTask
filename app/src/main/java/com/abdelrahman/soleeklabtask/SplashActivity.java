package com.abdelrahman.soleeklabtask;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.abdelrahman.soleeklabtask.Utils.IntentUtil;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IntentUtil.makeIntent(SplashActivity.this, HomeActivity.class);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
