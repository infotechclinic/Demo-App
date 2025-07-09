package com.example.loginandsignup;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

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

        logout.setOnClickListener(v -> {
            SharedPreferences pref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            Intent intent = new Intent(HomeScreen.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Set image view with a drawable
        ImageView imageView = findViewById(R.id.imageView); // Initialize before use

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_background, null);
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        }


        // Convert VectorDrawable to Bitmap for notification icon
        Bitmap largeIcon = null;
        Drawable iconDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_background, null);
        if (iconDrawable != null) {
            largeIcon = Bitmap.createBitmap(iconDrawable.getIntrinsicWidth(), iconDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(largeIcon);
            iconDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            iconDrawable.draw(canvas);
        }

        // Create and show notification
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification welcomeNotification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    WELCOME_NOTIFICATION_CHANNEL_ID,
                    "Welcome Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            nm.createNotificationChannel(channel);

            welcomeNotification = new Notification.Builder(this, WELCOME_NOTIFICATION_CHANNEL_ID)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.ic_eye_opendd)
                    .setContentTitle("Login Successful")
                    .setContentText("You have successfully logged in")
                    .setSubText("New message from this app...")
                    .build();
        } else {
            welcomeNotification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.ic_eye_opendd)
                    .setContentTitle("Login Successful")
                    .setContentText("You have successfully logged in")
                    .setSubText("New message from this app...")
                    .build();
        }

        nm.notify(NOTIFICATION_ID, welcomeNotification);
    }
}
