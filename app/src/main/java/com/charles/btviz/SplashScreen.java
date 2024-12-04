package com.charles.btviz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    // Variables
    private static final int SPLASH_SCREEN_DURATION = 2000;

    // Animations
    private Animation fadeAnimation;

    // Libraries

    // Views
    private TextView appNameTxt;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Change status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue_shade));
        // Change navigation bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.blue_shade));
        setContentView(R.layout.activity_splash_screen);

        initialize();
        splashScreen();

    }

    private void initialize() {
        appNameTxt = findViewById(R.id.assAppName_TextView);

        fadeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_animation);

        appNameTxt.setAnimation(fadeAnimation);
    }

    private void splashScreen() {
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            SplashScreen.this.finish();
        }, SPLASH_SCREEN_DURATION);
    }

}