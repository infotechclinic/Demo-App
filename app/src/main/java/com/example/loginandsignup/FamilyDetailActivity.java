package com.example.loginandsignup;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FamilyDetailActivity extends AppCompatActivity {

    private TextView name, age, relation, tvReportsContent;
    private static final int PICK_FILE_REQUEST_CODE = 101;
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 102;

    private Uri cameraImageUri;


    private Button btnUploadReport, btnUploadPrescription;
    private RecyclerView recyclerReports;

    private int memberId;
    private FamilyDBHelper dbHelper;
    private ReportAdapter reportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_detail);

        name = findViewById(R.id.tvName);
        age = findViewById(R.id.tvAge);
        relation = findViewById(R.id.tvRelation);
        tvReportsContent = findViewById(R.id.tvReportsContent);
        btnUploadPrescription = findViewById(R.id.btnUploadPrescription);
        recyclerReports = findViewById(R.id.recyclerReports);

        dbHelper = new FamilyDBHelper(this);


        btnUploadReport = findViewById(R.id.btnUploadReport);
        if (btnUploadReport == null) {
            Toast.makeText(this, "btnUploadReport is null", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "btnUploadReport is connected", Toast.LENGTH_SHORT).show();
        }

        btnUploadReport.setOnClickListener(v -> showUploadOptions());

        // Get memberId from Intent
        memberId = getIntent().getIntExtra("id", -1);

        // Fetch member info
        if (memberId != -1) {
            FamilyMemberModel member = dbHelper.getMemberById(memberId);
            if (member != null) {
                name.setText("Name: " + member.getName());
                age.setText("Age: " + member.getAge());
                relation.setText("Relation: " + member.getRelation());
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
            }


            // Load Reports
            List<ReportModel> reportList = dbHelper.getReportsForMember(memberId);

            btnUploadReport.setVisibility(View.VISIBLE);

            if (reportList.isEmpty()) {
                tvReportsContent.setVisibility(View.VISIBLE); // Show "No reports uploaded" message
            } else {
                tvReportsContent.setVisibility(View.GONE);    // Hide the message
            }

            reportAdapter = new ReportAdapter(this, reportList);
            recyclerReports.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerReports.setAdapter(reportAdapter);


            // You can add similar logic for prescriptions below
            List<PrescriptionModel> prescriptionList = dbHelper.getPrescriptionsForMember(memberId);
            // TODO: Set up prescription list (optional)
        }
    }
    private void showUploadOptions() {
        Toast.makeText(this, "Upload clicked", Toast.LENGTH_SHORT).show();
        String[] options = {"Pick from Files", "Take a Picture"};

        new AlertDialog.Builder(FamilyDetailActivity.this)
                .setTitle("Upload Report")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        openFileChooser();
                    } else if (which == 1) {
                        openCamera();
                    }
                })
                .show();
    }

    private void openCamera() {
        try {
            File photoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "Report_" + System.currentTimeMillis() + ".jpg");

            cameraImageUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    photoFile
            );

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Camera error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }




    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*"); // accept all types
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "application/pdf"});
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Report File"), PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri fileUri = null;

            if (requestCode == PICK_FILE_REQUEST_CODE && data != null) {
                fileUri = data.getData();
            } else if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
                fileUri = cameraImageUri;
            }

            if (fileUri != null) {
                getContentResolver().takePersistableUriPermission(fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                ReportModel model = new ReportModel(
                        0,
                        memberId,
                        "Report " + System.currentTimeMillis(),
                        String.valueOf(System.currentTimeMillis()),
                        fileUri.toString()
                );

                dbHelper.insertReport(model);

                // Refresh UI
                List<ReportModel> updatedList = dbHelper.getReportsForMember(memberId);
                reportAdapter = new ReportAdapter(this, updatedList);
                recyclerReports.setAdapter(reportAdapter);
                tvReportsContent.setVisibility(View.GONE);
                btnUploadReport.setVisibility(View.GONE);
            }
        }
    }

}
