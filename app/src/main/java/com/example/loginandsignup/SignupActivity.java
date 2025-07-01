package com.example.loginandsignup;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText username, password, repassword;
    Button signup;
    DBHelper Database;
    TextView loginText,back;

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
        Database = new DBHelper(this);

        signup.setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();
            String repass = repassword.getText().toString();

            if (user.isEmpty() || pass.isEmpty() || repass.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else if (!pass.equals(repass)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else if (Database.checkUsername(user)) {
                Toast.makeText(SignupActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
            } else {
                boolean insert = Database.insertData(user, pass);
                if (insert) {
                    Toast.makeText(SignupActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getParentActivityIntent();
                if(intent!=null){
                    startActivity(intent);
                }else{
                    Intent back = new Intent(getApplicationContext(),MainScreenActivity.class);
                    startActivity(back);
                }
            }
        });
    }
}
