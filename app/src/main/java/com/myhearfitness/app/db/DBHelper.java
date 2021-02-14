package com.myhearfitness.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.myhearfitness.app.db.Contract.UserDataEntry;
import com.myhearfitness.app.db.Contract.ProfilePicEntry;

public class DBHelper  extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "myheartfitness.db";
    private static final int VERSION_NUMBER = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserDataEntry.CREATE_TABLE);
        db.execSQL(ProfilePicEntry.CREATE_TABLE);

    }

    public SQLiteDatabase open() {
        final SQLiteDatabase db = getWritableDatabase();
        Log.d(TAG, "Database opened");
        return db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now, we'll just drop the current tables and create new ones
        db.execSQL("DROP TABLE IF EXISTS " + UserDataEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProfilePicEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public synchronized void close() {
        super.close();
        Log.d(TAG, "Database closed");
    }

}
