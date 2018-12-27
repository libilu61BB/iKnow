

package com.example.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySql extends SQLiteOpenHelper {
    private final static String DatebaseName = "LocalData.db";
    private final static int Version = 1;
    private final static String TableName = "MyActivity";

    public MySql(Context context){
        super(context, DatebaseName, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        String sql = "create table " + TableName + "(Id Integer primary key, year Integer, month Integer, day Integer, " +
                "startHour Integer, startMinute Integer, endHour Integer, endMinute Integer, introduction varchar(200), " +
                "mainLabel varchar(5), subLabel varchar(5), activityLabel varchar(5), place varchar(15), host varchar(10), " +
                "url varchar(30), name varchar(5))";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public Cursor Query(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TableName, null, null, null, null ,null,null);
        return cursor;
    }

    public Cursor Query(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TableName, null, "Id="+String.valueOf(id),null, null ,null,null);
        return cursor;
    }

    public Cursor Query(int year, int month, int day, int startHour){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TableName, null, "year="+String.valueOf(year)+" AND month="+String.valueOf(month)+" AND day="+String.valueOf(day)+
                " AND startHour between "+String.valueOf(startHour)+" and "+String.valueOf(startHour+1),null, null ,null,"startHour");
        return cursor;
    }

    public void Insert(int Id, int year, int month, int day, int startHour, int startMinute, int endHour, int endMinute, String introduction,
                       String mainLabel, String subLabel, String activityLabel, String place, String host, String url, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Id", Id);
        cv.put("year", year);
        cv.put("month", month);
        cv.put("day", day);
        cv.put("startHour", startHour);
        cv.put("startMinute", startMinute);
        cv.put("endHour", endHour);
        cv.put("endMinute", endMinute);
        cv.put("introduction", introduction);
        cv.put("mainLabel", mainLabel);
        cv.put("subLabel", subLabel);
        cv.put("activityLabel", activityLabel);
        cv.put("place", place);
        cv.put("host", host);
        cv.put("url", url);
        cv.put("name", name);
        db.insert(TableName, null, cv);

        db.close();

    }

    public void Delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TableName, "Id = ?", new String[] {String.valueOf(id)});
        db.close();
    }

    public void Delete(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TableName, null, null);
        db.close();
    }
}

