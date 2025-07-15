package com.example.loginandsignup;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FamilyMemberDetailActivity extends AppCompatActivity {
    EditText etName, etAge, etRelation;
    Button btnUpdate;
    int memberId;
    FamilyDBHelper dbHelper;
    FamilyMemberModel member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_member_detail);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etRelation = findViewById(R.id.etRelation);
        btnUpdate = findViewById(R.id.btnUpdate);

        memberId = getIntent().getIntExtra("id", -1);
        dbHelper = new FamilyDBHelper(this);
        member = dbHelper.getMemberById(memberId);

        if (member != null) {
            etName.setText(member.getName());
            etAge.setText(member.getAge());
            etRelation.setText(member.getRelation());
        }

        btnUpdate.setOnClickListener(v -> {
            member.setName(etName.getText().toString());
            member.setAge(etAge.getText().toString());
            member.setRelation(etRelation.getText().toString());

            dbHelper.updateMember(member);
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
