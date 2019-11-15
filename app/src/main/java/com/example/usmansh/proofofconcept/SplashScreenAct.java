package com.example.usmansh.proofofconcept;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        EasySplashScreen splashScreen = new EasySplashScreen(SplashScreenAct.this)
                .withFullScreen()
                .withTargetActivity(LoginAct.class)
                .withBackgroundResource(R.drawable.splashscreenimg)
                .withSplashTimeOut(5000);

        View view = splashScreen.create();
        setContentView(view);
    }
}
