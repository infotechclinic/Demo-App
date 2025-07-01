package com.example.loginandsignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    ImageView star1, star2, star3;
    int currentStar=0;
    Handler handler = new Handler(Looper.getMainLooper());
    Runnable starAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);

        starAnimation = new Runnable() {
            @Override
            public void run(){
                updateStars(currentStar);
                currentStar = (currentStar + 1) % 3;
                handler.postDelayed(this, 500);
            }
        };
        handler.post(starAnimation);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                startActivity(new Intent(SplashActivity.this, HomeScreen.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MainScreenActivity.class));
            }
            finish();
        }, 2000);
    }
    private void updateStars(int currentStar){
        star1.setImageResource(currentStar==0 ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        star2.setImageResource(currentStar==1 ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        star3.setImageResource(currentStar==2 ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
    }
}
