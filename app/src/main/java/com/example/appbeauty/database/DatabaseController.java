package com.example.appbeauty.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appbeauty.models.Professional;
import com.example.appbeauty.models.Schedule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DatabaseController {

    private SQLiteDatabase db;
    private DatabaseHelper helper;

    public DatabaseController(Context context){
        helper = new DatabaseHelper(context);
    }

    public Professional login(String username, String password) {
        Professional result = getProfessionalByUsername(username);

        if (result != null && result.getPassword().equals(password)) {
            return result;
        }

        return null;
    }

    public Professional getProfessionalById(long id) {
        Cursor cursor;
        String[] fields =  {"_id", "name", "username", "password"};
        db = helper.getReadableDatabase();
        cursor = db.query("professional", fields, "_id=" + id, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Professional result = new Professional();
            result.setId(cursor.getLong(0));
            result.setName(cursor.getString(1));
            result.setUsername(cursor.getString(2));
            result.setPassword(cursor.getString(3));

            db.close();
            return result;
        }

        db.close();
        return null;
    }

    public Professional[] getProfessional() {
        Cursor cursor;
        String[] fields =  {"_id", "name", "username", "password"};
        db = helper.getReadableDatabase();
        cursor = db.query("professional", fields, "username<>'admin'", null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            ArrayList<Professional> list = new ArrayList<>();
            do {
                Professional temp = new Professional();
                temp.setId(cursor.getLong(0));
                temp.setName(cursor.getString(1));
                temp.setUsername(cursor.getString(2));
                temp.setPassword(cursor.getString(3));
                list.add(temp);
            } while (cursor.moveToNext());

            db.close();
            return list.toArray(new Professional[0]);
        }

        db.close();
        return null;
    }

    public Professional getProfessionalByUsername(String username) {
        Cursor cursor;
        String[] fields =  {"_id", "name", "username", "password"};
        db = helper.getReadableDatabase();
        cursor = db.query("professional", fields, "username='" + username + "'", null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Professional result = new Professional();
            result.setId(cursor.getLong(0));
            result.setName(cursor.getString(1));
            result.setUsername(cursor.getString(2));
            result.setPassword(cursor.getString(3));

            db.close();
            return result;
        }

        db.close();
        return null;
    }

    public Professional createProfessional(Professional obj) {
        Professional temp = getProfessionalByUsername(obj.getUsername());
        if (temp != null && temp.getId() > 0) {
            return null;
        }

        ContentValues values;
        long result;

        db = helper.getWritableDatabase();
        values = new ContentValues();
        values.put("name", obj.getName());
        values.put("username", obj.getUsername());
        values.put("password", obj.getPassword());

        result = db.insert("professional", null, values);
        db.close();

        if (result == -1) {
            return null;
        }

        obj.setId(result);
        return obj;
    }

    public Professional editProfessional(Professional obj) {
        ContentValues values;
        long result;

        db = helper.getWritableDatabase();

        values = new ContentValues();
        values.put("name", obj.getName());
        values.put("username", obj.getUsername());
        values.put("password", obj.getPassword());

        result = db.update("professional", values, "_id=" + obj.getId(), null);
        db.close();

        if (result == -1) {
            return null;
        }

        obj.setId(result);
        return obj;
    }

    public void deleteProfessional(long id) {
        db = helper.getWritableDatabase();
        db.delete("professional","_id=" + id, null);
        db.close();
    }

    public Schedule[] getSchedule(long userId, Date date) {
        Cursor cursor;
        String[] fields =  {"_id", "clientName", "date", "length", "service", "value", "status", "professionalId"};

        String where = "professionalId=" + userId
                + " AND date>=" + getFirstHour(date).getTime()
                + " AND date<=" + getLastHour(date).getTime();
        db = helper.getReadableDatabase();
        cursor = db.query("professional", fields, where, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            ArrayList<Schedule> list = new ArrayList<>();
            do {
                Schedule temp = new Schedule();
                temp.setId(cursor.getLong(0));
                temp.setClientName(cursor.getString(1));
                temp.setDate(new Date(cursor.getLong(2)));
                temp.setLength(cursor.getInt(3));
                temp.setService(cursor.getString(4));
                temp.setValue(cursor.getDouble(5));
                temp.setStatus(cursor.getInt(6));
                temp.setProfessionalId(cursor.getLong(7));
                list.add(temp);
            } while (cursor.moveToNext());

            db.close();
            return (Schedule[])list.toArray();
        }

        db.close();
        return null;
    }

    public Schedule createSchedule(Schedule obj) {
        ContentValues values;
        long result;

        db = helper.getWritableDatabase();
        values = new ContentValues();
        values.put("clientName", obj.getClientName());
        values.put("date", obj.getDate().getTime());
        values.put("length", obj.getLength());
        values.put("service", obj.getService());
        values.put("value", obj.getValue());
        values.put("status", obj.getStatus());
        values.put("professionalId", obj.getProfessionalId());

        result = db.insert("schedule", null, values);
        db.close();

        if (result == -1) {
            return null;
        }

        obj.setId(result);
        return obj;
    }


    private Date getFirstHour(Date input) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(input);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date getLastHour(Date input) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(input);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

}
