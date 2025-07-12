package com.example.loginandsignup;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MemberDetailActivity extends AppCompatActivity {

    private TextView nameView, ageView, relationView, tvReportsContent;
    private Button btnUploadReport, btnUploadPrescription;
    private RecyclerView recyclerReports;

    private static final int PICK_FILE_REQUEST_CODE = 101;
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 102;

    private Uri cameraImageUri;

    private int memberId;
    private FamilyDBHelper dbHelper;
    private ReportAdapter reportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);

        nameView = findViewById(R.id.tvName);
        ageView = findViewById(R.id.tvAge);
        relationView = findViewById(R.id.tvRelation);
        tvReportsContent = findViewById(R.id.tvReportsContent);
        btnUploadReport = findViewById(R.id.btnUploadReport);
        btnUploadPrescription = findViewById(R.id.btnUploadPrescription);
        recyclerReports = findViewById(R.id.recyclerReports);

        dbHelper = new FamilyDBHelper(this);

        memberId = getIntent().getIntExtra("member_id", -1);

        if (memberId != -1) {
            FamilyMemberModel member = dbHelper.getMemberById(memberId);
            if (member != null) {
                nameView.setText("Name: " + member.getName());
                ageView.setText("Age: " + member.getAge());
                relationView.setText("Relation: " + member.getRelation());
            }

            // Upload report listener
            btnUploadReport.setOnClickListener(v -> showUploadOptions());

            // Load Reports
            List<ReportModel> reportList = dbHelper.getReportsForMember(memberId);

            if (reportList.isEmpty()) {
                tvReportsContent.setVisibility(View.VISIBLE);
                btnUploadReport.setVisibility(View.VISIBLE);
            } else {
                tvReportsContent.setVisibility(View.GONE);
                btnUploadReport.setVisibility(View.GONE);
            }

            reportAdapter = new ReportAdapter(this, reportList);
            recyclerReports.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerReports.setAdapter(reportAdapter);

            // (Optional) Setup prescription list
            List<PrescriptionModel> prescriptionList = dbHelper.getPrescriptionsForMember(memberId);
        }
    }

    private void showUploadOptions() {
        String[] options = {"Pick from Files", "Take a Picture"};

        new AlertDialog.Builder(this)
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
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Report_" + System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DESCRIPTION, "Captured via camera");

        cameraImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
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

                List<ReportModel> updatedList = dbHelper.getReportsForMember(memberId);
                reportAdapter = new ReportAdapter(this, updatedList);
                recyclerReports.setAdapter(reportAdapter);
                tvReportsContent.setVisibility(View.GONE);
                btnUploadReport.setVisibility(View.GONE);
            }
        }
    }
}
