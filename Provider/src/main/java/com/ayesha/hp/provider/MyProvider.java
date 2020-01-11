package com.ayesha.hp.provider;
/**
 * Created by HP on 04/11/16.
 */
import java.util.HashMap;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class MyProvider extends ContentProvider
{
    private DatabaseHelper dbHelper;
    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        if (db != null)
            return true;
        return false;
    }
    private SQLiteDatabase db;
    static final String PROVIDER_NAME = "com.ayesha.hp.provider.MyProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/cte";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final int uriAll = 1;
    static final int uriID = 2;
    static final UriMatcher uriMatcher;
    private static HashMap<String, String> values;
    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "cte", uriAll);
        uriMatcher.addURI(PROVIDER_NAME, "cte/*", uriID);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case uriAll:
                count = db.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case uriID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.id +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri))
        {
            case uriAll:
                return "vnd.android.cursor.dir/cte";
            // return "vnd.android.cursor.dir/vnd.ayesha.hp.cte";
            case uriID:
                return "vnd.android.cursor.item/cte/*";
            //return "vnd.android.cursor.item/vnd.ayesha.hp.cte/*";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(DatabaseHelper.TABLE_NAME, "", values);
        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DatabaseHelper.TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case uriAll:
                qb.setProjectionMap(values);
                break;
            case uriID:
               // qb.appendWhere(DatabaseHelper.word + "=" + uri.getLastPathSegment());
                qb.appendWhere(DatabaseHelper.id + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == "")
            sortOrder = DatabaseHelper.word;
        Cursor c = qb.query(db, projection, selection, selectionArgs, null,null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case uriAll:
                count = db.update(DatabaseHelper.TABLE_NAME, values, selection, selectionArgs);
                break;
            case uriID:
                count = db.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.id + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}