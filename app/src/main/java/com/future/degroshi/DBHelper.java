package com.future.degroshi;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public DBHelper(Context context) {
        super(context, "myDB", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table spents ("
                + "id integer primary key autoincrement," + "name text,"
                + "sum integer," + "date text" + ");");
        Log.d("---Log---", "DB Was Creatde");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected void addToDB(String name, int sum, String date){
        ContentValues cv = new ContentValues();
        db = getWritableDatabase();
        cv.put("name", name);
        cv.put("sum", sum);
        cv.put("date", date);
        long rowID = db.insert("spents", null, cv);
        Log.d("---Log---", "row inserted, ID = " + rowID);
        Log.d("---Log---", "Named " + name);
        Log.d("---Log---", "Sum " + sum);
        Log.d("---Log---", "Date " + date);

        db.close();
    }

    protected void delFromDB(ArrayList<String> namesAR){
        db = getWritableDatabase();
        for(String nameToDel : namesAR) {
            int delCount = db.delete("spents", "name = '" + nameToDel + "'", null);
            Log.d("---LogDB---", "DELETED FROM DB " + delCount);
        }
        db.close();
    }

    protected void eraseDB(){
        db = getWritableDatabase();
        db.delete("spents", null, null);
        db.close();
    }


}

