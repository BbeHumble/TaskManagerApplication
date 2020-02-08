package com.skitelDev.taskmanager.API;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import com.skitelDev.taskmanager.entities.Task;

import java.util.ArrayList;


public class API {
    public static void createDatabase(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS task (id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT)"); // для каждой конкретной задачи
        db.execSQL("CREATE TABLE IF NOT EXISTS tasklist (id INTEGER, taskid INTEGER)");// таблица со списками таск листов(id - id листа - taskid - id таска, который содержится в листе
    }

    //Получение списка задач по id списка
    public static ArrayList<Task> getTaskFromList(SQLiteDatabase db, int taskListId) {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor query = db.rawQuery("SELECT * FROM task " +
                "JOIN tasklist " +
                "ON tasklist.taskid == task.id " +
                "WHERE tasklist.id == " + taskListId +
                " ORDER BY id ASC", null);
        if (query.moveToFirst()) {
            do {
                int id = query.getInt(0);
                String text = query.getString(1);
                tasks.add(new Task(id, text));
            }
            while (query.moveToNext());
        }
        query.close();

        return tasks;
    }

    private static void insertIntoTaskList(SQLiteDatabase db, String taskText, int tasklistid) {
        db.execSQL("INSERT INTO task(text) VALUES ('" + taskText + "');");
        long index = findLastTaskID(db);
        db.execSQL("INSERT INTO tasklist(id, taskid) VALUES (" + tasklistid + "," + index + ");");

    }

    public static long findLastTaskID(SQLiteDatabase db) {
        try {
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM task;", null);
            cursor.moveToLast();
            return cursor.getLong(0);
        }
        catch (CursorIndexOutOfBoundsException e){
            return 0;
        }
    }

    private static void deleteTaskById(SQLiteDatabase db, long id) {
        db.execSQL("DELETE FROM task WHERE id =" + id + ";");
    }

    public static void deleteTaskByName(SQLiteDatabase db, String text) {
        db.execSQL("DELETE FROM task WHERE text ='" + text + "';");
    }

    public static Task findTaskById(SQLiteDatabase db, long id) {
        Cursor cursor = db.rawQuery("SELECT * FROM task " +
                "WHERE id =" + id + ";", null);
        cursor.moveToFirst();
        int resid = cursor.getInt(0);
        String restext = cursor.getString(1);
        return new Task(resid, restext);

    }
    public static Task findTaskByText(SQLiteDatabase db, String text){
        Cursor cursor = db.rawQuery("SELECT * FROM task " +
                "WHERE text ='" + text + "';", null);
        cursor.moveToFirst();
        int resid = cursor.getInt(0);
        String restext = cursor.getString(1);

        return new Task(resid, restext);
    }
    public static boolean updateTaskbyId(SQLiteDatabase db, long id, String text){
        db.execSQL("UPDATE task " +
                "SET text = '"+ text +"' " +
                "WHERE id = "+id+";");
        return true;
    }
    public static void updateTaskbyText(SQLiteDatabase db, String prev, String next){
        db.execSQL("UPDATE task " +
                "SET text = '"+ next +"' " +
                "WHERE text = '"+prev+"';");
    }
    public static void saveAll(SQLiteDatabase db, int idTaskList, ArrayList<Task> tasks){
        ArrayList<Task> tasks1 = getTaskFromList(db,idTaskList);
        for (Task task : tasks1){
            deleteTaskById(db, task.getId());
        }
        for (int i = 0; i <tasks.size() ; i++) {
            insertIntoTaskList(db,tasks.get(i).getText(),idTaskList);
        }
    }

}
