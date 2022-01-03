package com.example.cookwhat.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_USER_ENTRIES =
            "CREATE TABLE " + UserTableContract.UserTable.TABLE_NAME + " (" +
                    UserTableContract.UserTable._ID + " INTEGER PRIMARY KEY," +
                    UserTableContract.UserTable.COLUMN_NAME_DISPLAY_NAME + " TEXT," +
                    UserTableContract.UserTable.COLUMN_NAME_FOLLOWER + " INTEGER," +
                    UserTableContract.UserTable.COLUMN_NAME_FOLLOWING + " INTEGER)";

    private static final String SQL_DELETE_USER_ENTRIES =
            "DROP TABLE IF EXISTS " + UserTableContract.UserTable.TABLE_NAME;


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "cookwhat.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USER_ENTRIES);
        onCreate(db);
    }
}
