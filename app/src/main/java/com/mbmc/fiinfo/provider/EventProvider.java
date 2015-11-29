package com.mbmc.fiinfo.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.mbmc.fiinfo.BuildConfig;
import com.mbmc.fiinfo.helper.Database;

import java.util.Map;


public class EventProvider extends ContentProvider {

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";
    private static final String URI_PATH = "content://" + AUTHORITY + "/events";

    public static final Uri URI = Uri.parse(URI_PATH);
    public static final Uri URI_COUNT = Uri.parse(URI_PATH + "/count");
    public static final Uri URI_LAST = Uri.parse(URI_PATH + "/last");

    private static final int EVENTS = 1000;
    private static final int EVENT_ID = 10001;
    private static final int EVENT_COUNT = 10002;
    private static final int EVENT_LAST = 10003;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "events", EVENTS);
        uriMatcher.addURI(AUTHORITY, "events/#", EVENT_ID);
        uriMatcher.addURI(AUTHORITY, "events/count", EVENT_COUNT);
        uriMatcher.addURI(AUTHORITY, "events/last", EVENT_LAST);
    }

    private Database database;
    private Map<String, String> map;


    @Override
    public boolean onCreate() {
        database = new Database(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Database.TABLE_EVENT);

        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case EVENTS:
                queryBuilder.setProjectionMap(map);
                break;

            case EVENT_ID:
                queryBuilder.appendWhere(Database.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            case EVENT_COUNT:
                cursor = sqLiteDatabase.rawQuery("SELECT count(*) AS count, _id, type, name, mobile, speed" +
                        " FROM event GROUP BY type, name, mobile, speed" +
                        " ORDER BY count DESC", null);
                break;

            case EVENT_LAST:
                cursor = sqLiteDatabase.rawQuery("SELECT _id, type, date, name, mobile, speed FROM event ORDER BY date DESC LIMIT 1", null);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (cursor == null) {
            sortOrder = "date DESC";
            cursor = queryBuilder.query(sqLiteDatabase, projection, selection,
                    selectionArgs, null, null, sortOrder);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri result = null;
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case EVENTS:
                id = sqLiteDatabase.insert(Database.TABLE_EVENT, null, contentValues);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (id > 0) {
            result = ContentUris.withAppendedId(URI, id);
            getContext().getContentResolver().notifyChange(result, null);
        } else {
            throw new SQLException("Insert failed: " + uri);
        }
        return result;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        int result = sqLiteDatabase.delete(Database.TABLE_EVENT, null, null);
        if (result > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

}
