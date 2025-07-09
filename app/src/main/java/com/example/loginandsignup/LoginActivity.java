package com.example.loginandsignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    ImageView eyeIcon;
    Button login;
    DBHelper DB;
    TextView signUpText, back, forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginbtn);
        signUpText = findViewById(R.id.signupRedirect);
        back = findViewById(R.id.back);
        forgotPassword = findViewById(R.id.forgotPassword); // FIXED
        DB = new DBHelper(this);
        eyeIcon = findViewById(R.id.eye_icon);
        final boolean[] isPassVisible = {false};

        forgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Forgot password functionality coming soon", Toast.LENGTH_SHORT).show();
        });

        eyeIcon.setOnClickListener(v -> {
            if (isPassVisible[0]) {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                eyeIcon.setImageResource(R.drawable.ic_eye_closedd);
            } else {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                eyeIcon.setImageResource(R.drawable.ic_eye_opendd);
            }
            password.setSelection(password.getText().length());
            isPassVisible[0] = !isPassVisible[0];
        });

        login.setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (DB.checkUsernamePassword(user, pass)) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    preferences.edit().putBoolean("isLoggedIn", true).apply();
                    startActivity(new Intent(LoginActivity.this, HomeScreen.class));
                    finish();
                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });

        back.setOnClickListener(v -> {
            Intent intent = getParentActivityIntent();
            if (intent != null) startActivity(intent);
            else startActivity(new Intent(this, MainScreenActivity.class));
            finish();
        });
    }
}