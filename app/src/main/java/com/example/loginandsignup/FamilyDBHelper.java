package com.example.loginandsignup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class FamilyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "family.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FAMILY = "family";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_AGE = "age";
    private static final String COL_RELATION = "relation";

    public FamilyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_FAMILY + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT," +
                COL_AGE + " TEXT," +
                COL_RELATION + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAMILY);
        onCreate(db);
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
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FAMILY + " WHERE id=?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            return new FamilyMemberModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
        }
        return null;
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
        db.delete("family", "id=?", new String[]{String.valueOf(id)});
    }

    public void addDummyData() {
        if (getAllMembers().isEmpty()) {
            insertMember(new FamilyMemberModel(0, "John", "50", "Father"));
            insertMember(new FamilyMemberModel(0, "Anna", "45", "Mother"));
        }
    }

    public void insertMember(FamilyMemberModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, model.getName());
        values.put(COL_AGE, model.getAge());
        values.put(COL_RELATION, model.getRelation());
        db.insert(TABLE_FAMILY, null, values);
    }
}
