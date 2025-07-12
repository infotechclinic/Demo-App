package com.example.loginandsignup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.ArrayList;
import java.util.List;

public class FamilyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "family.db";
    private static final int DATABASE_VERSION = 2; // <-- bumped version for new tables

    // Family Table
    private static final String TABLE_FAMILY = "family";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_AGE = "age";
    private static final String COL_RELATION = "relation";

    // Reports Table
    private static final String TABLE_REPORTS = "reports";
    private static final String COL_REPORT_ID = "id";
    private static final String COL_REPORT_MEMBER_ID = "member_id";
    private static final String COL_REPORT_TITLE = "title";
    private static final String COL_REPORT_DATE = "date";
    private static final String COL_REPORT_FILE_PATH = "file_path";

    // Prescriptions Table
    private static final String TABLE_PRESCRIPTIONS = "prescriptions";
    private static final String COL_PRES_ID = "id";
    private static final String COL_PRES_MEMBER_ID = "member_id";
    private static final String COL_PRES_NAME = "medicine_name";
    private static final String COL_PRES_DOSAGE = "dosage";
    private static final String COL_PRES_DATE = "date";

    public FamilyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Family table
        db.execSQL("CREATE TABLE " + TABLE_FAMILY + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT," +
                COL_AGE + " TEXT," +
                COL_RELATION + " TEXT)");

        // Reports table
        db.execSQL("CREATE TABLE " + TABLE_REPORTS + "(" +
                COL_REPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_REPORT_MEMBER_ID + " INTEGER," +
                COL_REPORT_TITLE + " TEXT," +
                COL_REPORT_DATE + " TEXT," +
                COL_REPORT_FILE_PATH + " TEXT)");

        // Prescriptions table
        db.execSQL("CREATE TABLE " + TABLE_PRESCRIPTIONS + "(" +
                COL_PRES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_PRES_MEMBER_ID + " INTEGER," +
                COL_PRES_NAME + " TEXT," +
                COL_PRES_DOSAGE + " TEXT," +
                COL_PRES_DATE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAMILY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESCRIPTIONS);
        onCreate(db);
    }

    public void insertMember(FamilyMemberModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, model.getName());
        values.put(COL_AGE, model.getAge());
        values.put(COL_RELATION, model.getRelation());
        db.insert(TABLE_FAMILY, null, values);
    }

    public List<FamilyMemberModel> getAllMembers() {
        List<FamilyMemberModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FAMILY, null);
        if (cursor.moveToFirst()) {
            do {
                FamilyMemberModel model = new FamilyMemberModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                list.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public FamilyMemberModel getMemberById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAMILY, null, "id=?", new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            FamilyMemberModel member = new FamilyMemberModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("age"))),
                    cursor.getString(cursor.getColumnIndexOrThrow("relation"))
            );
            cursor.close();
            return member;
        }
        return null;
    }

    public void deleteReport(int reportId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("reports", "id = ?", new String[]{String.valueOf(reportId)});
    }


    public void updateReportTitle(int reportId, String newTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", newTitle);
        db.update("reports", values, "id=?", new String[]{String.valueOf(reportId)});
    }

    public void updateMember(FamilyMemberModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, model.getName());
        values.put(COL_AGE, model.getAge());
        values.put(COL_RELATION, model.getRelation());
        db.update(TABLE_FAMILY, values, COL_ID + "=?", new String[]{String.valueOf(model.getId())});
    }

    public void deleteMember(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAMILY, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void addDummyData() {
        if (getAllMembers().isEmpty()) {
            insertMember(new FamilyMemberModel(0, "John", "50", "Father"));
            insertMember(new FamilyMemberModel(0, "Anna", "45", "Mother"));
        }
    }

    // Insert a report
    public void insertReport(ReportModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_REPORT_MEMBER_ID, model.getMemberId());
        values.put(COL_REPORT_TITLE, model.getTitle());
        values.put(COL_REPORT_DATE, model.getDate());
        values.put(COL_REPORT_FILE_PATH, model.getFilePath());
        db.insert(TABLE_REPORTS, null, values);
    }

    // Get all reports for a given member
    public List<ReportModel> getReportsForMember(int memberId) {
        List<ReportModel> reports = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REPORTS + " WHERE " + COL_REPORT_MEMBER_ID + " = ?", new String[]{String.valueOf(memberId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_REPORT_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_REPORT_TITLE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_REPORT_DATE));
                String filePath = cursor.getString(cursor.getColumnIndexOrThrow(COL_REPORT_FILE_PATH));

                reports.add(new ReportModel(id, memberId, title, date, filePath));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return reports;
    }

    // Insert a prescription
    public void insertPrescription(PrescriptionModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRES_MEMBER_ID, model.getMemberId());
        values.put(COL_PRES_NAME, model.getMedicineName());
        values.put(COL_PRES_DOSAGE, model.getDosage());
        values.put(COL_PRES_DATE, model.getDate());
        db.insert(TABLE_PRESCRIPTIONS, null, values);
    }

    // Get prescriptions for a member
    public List<PrescriptionModel> getPrescriptionsForMember(int memberId) {
        List<PrescriptionModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRESCRIPTIONS + " WHERE " + COL_PRES_MEMBER_ID + " = ?", new String[]{String.valueOf(memberId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRES_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRES_NAME));
                String dosage = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRES_DOSAGE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRES_DATE));

                list.add(new PrescriptionModel(id, memberId, name, dosage, date));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }
}
