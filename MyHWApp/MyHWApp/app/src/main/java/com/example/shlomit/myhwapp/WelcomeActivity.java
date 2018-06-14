package com.example.shlomit.myhwapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.shlomit.hw.gifanimation.GifView;

public class WelcomeActivity extends Activity {

    GifView gifview;
    private static int SPLASH_TIME_OUT =2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent signInIntent = new Intent(WelcomeActivity.this, SignInActivity.class);
                startActivity(signInIntent);
                finish(); }},SPLASH_TIME_OUT);
    }

}
