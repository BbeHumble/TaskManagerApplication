
package com.skitelDev.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.skitelDev.taskmanager.entities.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDatabase();
        ArrayList<Task> list;
//        insertIntoTaskList("новый таск", 1);
//        insertIntoTaskList("123 таск", 1);
//        insertIntoTaskList("нов123ый таск", 1);
//        insertIntoTaskList("123 таск", 1);
//        Task task = findTaskByText("123 таск");
//        Log.println(Log.ERROR, "out", task.getId() + " " + task.getText());
//        deleteTaskByName("новый таск");
//            updateTaskbyId(4,"edited");
        updateTaskbyText("edited","onemoretime");
        list = getTaskFromList(1);
        TaskListLoader(list);
    }

    private void TaskListLoader(ArrayList<Task> list) {
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager1);
        TaskListAdapter mAdapter = new TaskListAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(mAdapter);
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
        long index = findLastTaskID();
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

    public void deleteTaskById(int id) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("DELETE FROM task WHERE id =" + id + ";");
    }

    public void deleteTaskByName(String text) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("DELETE FROM task WHERE text ='" + text + "';");
    }

    public Task findTaskById(int id) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM task " +
                "WHERE id =" + id + ";", null);
        cursor.moveToFirst();
        int resid = cursor.getInt(0);
        String restext = cursor.getString(1);
        db.close();
        return new Task(resid, restext);

    }
    public Task findTaskByText(String text){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM task " +
                "WHERE text ='" + text + "';", null);
        cursor.moveToFirst();
        int resid = cursor.getInt(0);
        String restext = cursor.getString(1);
        db.close();
        return new Task(resid, restext);
    }
    public void updateTaskbyId(int id, String text){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("UPDATE task " +
                "SET text = '"+ text +"' " +
                "WHERE id = "+id+";");
    }
    public void updateTaskbyText(String prev, String next){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("UPDATE task " +
                "SET text = '"+ next +"' " +
                "WHERE text = '"+prev+"';");
    }
    


}
