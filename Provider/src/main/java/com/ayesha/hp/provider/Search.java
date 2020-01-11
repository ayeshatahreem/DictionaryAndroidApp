package com.ayesha.hp.provider;

/**
 * Created by HP on 05/11/16.
 */
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Search extends  AppCompatActivity  {
    /** Called when the activity is first created. */
    String myword[];
    String mymeaning[];
    int index;
    private DatabaseHelper ayesha_db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        myword=new String[500];
        mymeaning=new String[500];
        index=0;
        ayesha_db=new DatabaseHelper(this);
        SQLiteDatabase db = ayesha_db.getWritableDatabase();
        // TO LOAD IN ARRAY
        String query = "SELECT * FROM words";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext())
        {
            myword[index]=cursor.getString(cursor.getColumnIndex("word"));
            mymeaning[index]=cursor.getString(cursor.getColumnIndex("meaning"));
            index++;
        }
    }
    public void onClickRetrieve(View view) {

        EditText p= (EditText) findViewById(R.id.txtWord);
        String input=p.getText().toString();
        String output=new String();
        output="Word not found in db!";
        for (int i=0; i<index;i++)
        {
            if (myword[i].equals(input))
            {
                output= mymeaning[i];
                Toast.makeText(this, output, Toast.LENGTH_LONG).show();
            }
        }

    }
    public void goBack(View v) {
        finish();
    }
}








