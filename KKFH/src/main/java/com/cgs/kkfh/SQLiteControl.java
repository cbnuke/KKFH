package com.cgs.kkfh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Amnart on 22/11/2556.
 */
public class SQLiteControl extends SQLiteOpenHelper {

    private static final String DB_NAME = "DB_KKFH";
    private static final int DB_VERSION = 1;

    public SQLiteControl(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE member (id INTEGER PRIMARY KEY,"
                + "name TEXT(100),phone TEXT(100),"
                + " disease TEXT(255));");
        Log.d("KKFHD", "Create table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public boolean insertData(String name, String phone, String disease) {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase();

            ContentValues val = new ContentValues();
            val.put("name", name);
            val.put("phone", phone);
            val.put("disease", disease);

            long row = db.insert("member", null, val);
            Log.d("KKFHD", "Insert data");
            db.close();
            return true;
        } catch (Exception e) {
            Log.d("KKFHD", "Insert data fail");
            return false;
        }
    }
    public boolean updateData(String name, String phone, String disease) {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase();

            ContentValues val = new ContentValues();
            val.put("name", name);
            val.put("phone", phone);
            val.put("disease", disease);

            long row = db.update("member", val,"id=1",null);
            Log.d("KKFHD", "Update data");
            db.close();
            return true;
        } catch (Exception e) {
            Log.d("KKFHD", "Update data fail");
            return false;
        }
    }

    public String[] selectMember() {
        try {
            String data[] = new String[1];

            SQLiteDatabase db;
            db = this.getReadableDatabase();

            String strSQL = "SELECT * FROM member";
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null && cursor.moveToFirst()) {
                Log.d("KKFHD", "Select data");
                data[0] = cursor.getString(0);
                data[0] = cursor.getString(1);
                data[0] = cursor.getString(2);
                data[0] = cursor.getString(3);
            } else {
                data[0] = "0";
            }
            cursor.close();
            db.close();
            return data;
        } catch (Exception e) {
            return null;
        }
    }
}
