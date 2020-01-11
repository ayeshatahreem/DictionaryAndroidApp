package com.ayesha.hp.provider;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by HP on 05/11/16.
 */
public class Add extends AppCompatActivity {
    private DatabaseHelper ayesha_db;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        ayesha_db=new DatabaseHelper(this);
    }

    public void AddWord(View view) {
        SQLiteDatabase db = ayesha_db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.word, ((EditText) findViewById(R.id.txtWord)).getText().toString());
        values.put(DatabaseHelper.meaning, ((EditText) findViewById(R.id.txtMeaning)).getText().toString());
        db.insertWithOnConflict("words",null,values,SQLiteDatabase.CONFLICT_REPLACE);
        Toast.makeText(getBaseContext(), "New word added!", Toast.LENGTH_LONG).show();
    }
    public void goBack(View v) {
        finish();
    }
}
