package com.example.loginandsignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText username, password, repassword;
    ImageView eyeIcon;
    Button signup;
    DBHelper Database;
    TextView loginText, back;
    boolean isPassVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        signup = findViewById(R.id.signupbtn);
        loginText = findViewById(R.id.loginRedirect);
        back = findViewById(R.id.back);
        eyeIcon = findViewById(R.id.eye_icon);

        Database = new DBHelper(this);

        eyeIcon.setOnClickListener(v -> {
            if (isPassVisible) {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                repassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                eyeIcon.setImageResource(R.drawable.ic_eye_closedd); // your closed eye icon
            } else {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                repassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                eyeIcon.setImageResource(R.drawable.ic_eye_opendd); // your open eye icon
            }
            password.setSelection(password.getText().length());
            isPassVisible = !isPassVisible;
        });

        // Sign up button click
        signup.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString();
            String repass = repassword.getText().toString();

            if (user.isEmpty() || pass.isEmpty() || repass.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else if (!pass.equals(repass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else if (!pass.matches("^(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,}$")) {
                Toast.makeText(this, "Password must be 6+ chars, 1 uppercase, 1 special character", Toast.LENGTH_SHORT).show();
            } else if (Database.checkUsername(user)) {
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
            } else {
                boolean insert = Database.insertData(user, pass);
                if (insert) {
                    SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginText.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });

        back.setOnClickListener(v -> {
            Intent intent = getParentActivityIntent();
            if (intent != null) startActivity(intent);
            else startActivity(new Intent(this, MainScreenActivity.class));
        });
    }
}
