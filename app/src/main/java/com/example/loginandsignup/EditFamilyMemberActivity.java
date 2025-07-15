package com.example.loginandsignup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class EditFamilyMemberActivity extends AppCompatActivity {

    EditText etName, etAge, etRelation;
    Button btnUpdate, btnDelete;
    FamilyDBHelper dbHelper;
    int memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_family_member);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etRelation = findViewById(R.id.etRelation);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        LinearLayout rootLayout = findViewById(R.id.rootLayout);
        rootLayout.setFocusableInTouchMode(true);
        rootLayout.requestFocus();
        dbHelper = new FamilyDBHelper(this);

        memberId = getIntent().getIntExtra("id", -1);
        if (memberId == -1) {
            Toast.makeText(this, "Invalid member", Toast.LENGTH_SHORT).show();
            finish();
        }

        FamilyMemberModel model = dbHelper.getMemberById(memberId);
        if (model != null) {
            etName.setText(model.getName());
            etAge.setText(model.getAge());
            etRelation.setText(model.getRelation());
        }

        btnUpdate.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String age = etAge.getText().toString().trim();
            String relation = etRelation.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(age) || TextUtils.isEmpty(relation)) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            FamilyMemberModel updatedModel = new FamilyMemberModel(memberId, name, age, relation);
            dbHelper.updateMember(updatedModel);
            Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Member")
                    .setMessage("Are you sure you want to delete this family member?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        rootLayout.animate()
                                .alpha(0f)
                                .setDuration(400)
                                .withEndAction(() -> {
                                    dbHelper.deleteMember(memberId);
                                    Snackbar snackbar = Snackbar.make(findViewById(R.id.rootLayout), "Deleted successfully", Snackbar.LENGTH_SHORT);
                                    View snackbarView = snackbar.getView();
                                    snackbarView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                                    TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                                    textView.setTextColor(getResources().getColor(android.R.color.white));
                                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    snackbar.show();

                                    finish();
                                })
                                .start();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

    }
}
