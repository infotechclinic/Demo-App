package com.example.loginandsignup;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeScreen extends AppCompatActivity {
private static final String WELCOME_NOTIFICATION_CHANNEL_ID = "welcome_id";
private static final int NOTIFICATION_ID = 100;
    Button logout;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                Intent intent = new Intent(HomeScreen.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_launcher_background,null);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeIcon = bitmapDrawable.getBitmap();

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification Welcomenotification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Welcomenotification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.ic_eye_opendd)
                    .setContentText("You have successfully logged in")
                    .setSubText("New Message from this app...")
                    .setChannelId(WELCOME_NOTIFICATION_CHANNEL_ID)
                    .build();
            nm.createNotificationChannel(new NotificationChannel(WELCOME_NOTIFICATION_CHANNEL_ID,"New Channel",NotificationManager.IMPORTANCE_HIGH));
        }else{
            Welcomenotification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.ic_eye_opendd)
                    .setContentText("You have successfully logged in")
                    .setSubText("New Message from this app...")
                    .build();
        }
        nm.notify(NOTIFICATION_ID,Welcomenotification);

    }
}