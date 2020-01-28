
package com.skitelDev.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDatabase();

    }
    public void createDatabase() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tasklist (id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT)");
//        db.execSQL("INSERT INTO tasklist(text) VALUES ('поесть');");
        Cursor query = db.rawQuery("SELECT * FROM tasklist;", null);
        if(query.moveToFirst()){
            do{
                int id = query.getInt(0);
                String text = query.getString(1);
                Log.println(Log.WARN,"MESSAGE",id+" "+ text);
            }
            while(query.moveToNext());
        }
        query.close();
        db.close();
    }
}
