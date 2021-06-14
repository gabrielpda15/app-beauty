package com.example.appbeauty.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "appbeauty.db";
    private static final int VERSION = 6;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createScheduleTable(db);
        createProfessionalTable(db);

        createInitialProfessional(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS schedule");
        db.execSQL("DROP TABLE IF EXISTS professional");
        onCreate(db);
    }

    private void createInitialProfessional(SQLiteDatabase db) {
        ContentValues values;
        long result;

        values = new ContentValues();
        values.put("name", "Administrador");
        values.put("username", "admin");
        values.put("password", "Passw0rd!");

        result = db.insert("professional", null, values);
    }

    private void createScheduleTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE schedule("
                + "_id integer primary key autoincrement,"
                + "clientName text,"
                + "date numeric,"
                + "length integer,"
                + "service text,"
                + "value real,"
                + "status integer,"
                + "professionalId integer"
                + ")";
        db.execSQL(sql);
    }

    private void createProfessionalTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE professional("
                + "_id integer primary key autoincrement,"
                + "name text,"
                + "username text,"
                + "password text"
                + ")";
        db.execSQL(sql);
    }

}
