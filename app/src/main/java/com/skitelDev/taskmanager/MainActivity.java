
package com.skitelDev.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.skitelDev.taskmanager.entities.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDatabase();
        ArrayList<Task> list;
        insertIntoTaskList("новый таск", 1);
        list = getTaskFromList(1);
        for (Task task : list) {
            Log.println(Log.ERROR, "MESSAGE", task.getId() + " " + task.getText());
        }
    }

    public void createDatabase() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS task (id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT)"); // для каждой конкретной задачи
        db.execSQL("CREATE TABLE IF NOT EXISTS tasklist (id INTEGER, taskid INTEGER)");// таблица со списками таск листов(id - id листа - taskid - id таска, который содержится в листе
        db.close();
    }

    //Получение списка задач по id списка
    public ArrayList<Task> getTaskFromList(int taskListId) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor query = db.rawQuery("SELECT * FROM task " +
                "JOIN tasklist " +
                "ON tasklist.taskid == task.id " +
                "WHERE tasklist.id == " + taskListId, null);
        if (query.moveToFirst()) {
            do {
                int id = query.getInt(0);
                String text = query.getString(1);
                tasks.add(new Task(id, text));
            }
            while (query.moveToNext());
        }
        query.close();
        db.close();
        return tasks;
    }

    public void insertIntoTaskList(String taskText, int tasklistid) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("INSERT INTO task(text) VALUES ('" + taskText + "');");
        Long index = findLastTaskID();
        db.execSQL("INSERT INTO tasklist(id, taskid) VALUES (" + tasklistid + "," + index + ");");
        db.close();
    }

    public long findLastTaskID() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM task;", null);
        cursor.moveToLast();
        db.close();
        return cursor.getLong(0);
    }

}
