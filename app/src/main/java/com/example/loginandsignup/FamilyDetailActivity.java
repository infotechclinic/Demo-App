package com.example.loginandsignup;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FamilyDetailActivity extends AppCompatActivity {

    TextView name, age, relation;
    FamilyDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_detail);

        name = findViewById(R.id.tvName);
        age = findViewById(R.id.tvAge);
        relation = findViewById(R.id.tvRelation);
        dbHelper = new FamilyDBHelper(this);

        int id = getIntent().getIntExtra("id", -1);
        if (id != -1) {
            FamilyMemberModel member = dbHelper.getMemberById(id);
            if (member != null) {
                name.setText("Name: " + member.getName());
                age.setText("Age: " + member.getAge());
                relation.setText("Relation: " + member.getRelation());
            }
        }
    }
}
