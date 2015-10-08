package com.mbmc.fiinfo.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {

    public static final String TABLE_EVENT = "event";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME_ZONE = "timeZone";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SPEED = "speed";

    public static final String NAME = "events";

    private static final int VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_EVENT + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TYPE + " INTEGER NOT NULL, "
            + COLUMN_DATE + " INTEGER NOT NULL, "
            + COLUMN_TIME_ZONE + " TEXT NOT NULL, "
            + COLUMN_COUNTRY + " TEXT NOT NULL, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_SPEED + " TEXT"
            + ");";


    public Database(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

}