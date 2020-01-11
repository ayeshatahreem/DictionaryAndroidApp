package com.ayesha.hp.provider;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by HP on 04/11/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String id = "id";
    public static final String word = "word";
    public static final String meaning = "meaning";
    public static final String DATABASE_NAME = "mydictionary";
    public static final String TABLE_NAME = "words";
    public static final int DATABASE_VERSION = 1;
    private static final String CREATE_DB_TABLE = " CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " word TEXT NOT NULL, "+" meaning TEXT NOT NULL );";
    DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}