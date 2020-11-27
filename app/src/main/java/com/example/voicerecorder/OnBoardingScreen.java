package com.example.voicerecorder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class OnBoardingScreen extends Activity {

    private static int SPLASH_SCREEN = 3500;

    //Variables
    Animation topAnim, bottomAnim;
    ImageView imageViewLogo;
    TextView textViewNameApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding_screen);

        //Animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        imageViewLogo = findViewById(R.id.logoApp);
        textViewNameApp = findViewById(R.id.nameApp);

        imageViewLogo.setAnimation(topAnim);
        textViewNameApp.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Boolean isStarted = preferences.getBoolean(getString(R.string.isStarted), false);
                if (!isStarted) {
                    Intent intent = new Intent(OnBoardingScreen.this, SplashScreen.class);
                    startActivity(intent);
                    finish();
                }
                else{

                    Intent intent = new Intent(OnBoardingScreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_SCREEN);
    }
}