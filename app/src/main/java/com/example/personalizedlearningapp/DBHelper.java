package com.example.personalizedlearningapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME    = "Users.db";
    private static final int    DB_VERSION = 1;
    private static final String TABLE      = "users";
    private static final String COL_USER   = "username";
    private static final String COL_PASS   = "password";

    public DBHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE +
                " (" + COL_USER + " TEXT PRIMARY KEY, " +
                COL_PASS + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    /** Insert a new user; returns false if username already exists */
    public boolean insertUser(String user, String pass) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER, user);
        cv.put(COL_PASS, pass);
        long id = db.insert(TABLE, null, cv);
        return id != -1;
    }

    /** Check if a username/password combo exists */
    public boolean checkUser(String user, String pass) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE,
                new String[]{COL_USER},
                COL_USER + "=? AND " + COL_PASS + "=?",
                new String[]{user, pass},
                null, null, null
        );
        boolean exists = c.getCount() > 0;
        c.close();
        return exists;
    }
}
