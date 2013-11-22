package com.cgs.kkfh;

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
        Log.d("KKFH","Create table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public String[] selectMember(){
        try {
            String data[]=null;

            SQLiteDatabase db;
            db = this.getReadableDatabase();

            String strSQL = "SELECT * FROM member";
            Cursor cursor = db.rawQuery(strSQL,null);

            if(cursor!=null&&cursor.moveToFirst()){
                data[0] = cursor.getString(0);
                data[1] = cursor.getString(1);
                data[2] = cursor.getString(2);
                data[3] = cursor.getString(3);
            }
            cursor.close();
            db.close();
            return data;
        }catch (Exception e){
            return null;
        }

    }
}
