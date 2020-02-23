package com.skitelDev.taskmanager.API;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import com.skitelDev.taskmanager.entities.Task;

import java.util.ArrayList;


public class API {
    public static SQLiteDatabase db;
    public static void createDatabase() {
        db.execSQL("CREATE TABLE IF NOT EXISTS task (id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT)"); // для каждой конкретной задачи
        db.execSQL("CREATE TABLE IF NOT EXISTS tasklist (id INTEGER, taskid INTEGER)");// таблица со списками таск листов(id - id листа - taskid - id таска, который содержится в листе
        db.execSQL("CREATE TABLE IF NOT EXISTS taskdescription (taskid INTEGER PRIMARY KEY AUTOINCREMENT, description Text )");
        db.execSQL("CREATE TABLE IF NOT EXISTS subtasks (taskid INTEGER, sub_task_text TEXT)");

    }
    private static void deleteSubTasks(long id){
        db.execSQL("DELETE FROM subtasks WHERE taskid =" + id + ";");
    }
    public static ArrayList<Task> getTasksWithSubTasks(ArrayList<Task> tasks){
        for (int i = 0; i <tasks.size() ; i++) {
            tasks.get(i).setSubtasks(getSubTasksByTaskId(tasks.get(i).getId()));
        }
        return tasks;
    }
    public static ArrayList<String> getSubTasksByTaskId(long taskId){
        ArrayList<String> subTasks = new ArrayList<>();
        Cursor query = db.rawQuery("SELECT subtasks.sub_task_text" +
                        " FROM subtasks" +
                        " JOIN task " +
                        "ON subtasks.taskid = " + taskId
                , null);
        if (query.moveToFirst()) {
            do {
                subTasks.add(query.getString(0));
            }
            while (query.moveToNext());
        }
        query.close();
        return subTasks;
    }
    private static void insertSubTaskToTask(long taskId, String subTaskText){
        db.execSQL("INSERT INTO subtasks(taskid, sub_task_text) VALUES (" + taskId + ",'" + subTaskText + "');");
    }
    //Получение списка задач по id списка
    public static ArrayList<Task> getTaskFromList(int taskListId) {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor query = db.rawQuery("SELECT task.id, " +
                        "task.text, " +
                        "taskdescription.description" +
                        " FROM task" +
                        " JOIN tasklist " +
                        "ON task.id = tasklist.taskid " +
                        " JOIN taskdescription" +
                        " ON task.id = taskdescription.taskid"+
                        " WHERE tasklist.id = " + taskListId
                , null);
        if (query.moveToFirst()) {
            do {
                int id = query.getInt(0);
                String text = query.getString(1);
                String desc = query.getString(2);
                tasks.add(new Task(id, text, desc));
            }
            while (query.moveToNext());
        }
        query.close();

        return tasks;
    }

    private static void insertIntoTaskList(String taskText, String taskDescription, int tasklistid) {
        db.execSQL("INSERT INTO task(text) VALUES ('" + taskText + "');");
        long index = findLastTaskID();
        db.execSQL("INSERT INTO tasklist(id, taskid) VALUES (" + tasklistid + "," + index + ");");
        db.execSQL("INSERT INTO taskdescription(taskid,description) VALUES (" + index+ ",'" + taskDescription + "')");

    }


    public static long findLastTaskID() {
        try {
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM task;", null);
            cursor.moveToLast();
            return cursor.getLong(0);
        }
        catch (CursorIndexOutOfBoundsException e){
            return 0;
        }
    }

    private static void deleteTaskById(long id) {
        db.execSQL("DELETE FROM task WHERE id =" + id + ";");
        db.execSQL("DELETE FROM taskdescription WHERE taskid ="+ id +";");
    }

    public static void deleteTaskByName(String text) {
        db.execSQL("DELETE FROM task WHERE text ='" + text + "';");
    }

    public static Task findTaskById(long id) {
        Cursor cursor = db.rawQuery("SELECT * FROM task " +
                "WHERE id =" + id + ";", null);
        cursor.moveToFirst();
        int resid = cursor.getInt(0);
        String restext = cursor.getString(1);
        return new Task(resid, restext, "");

    }
    public static Task findTaskByText(String text){
        Cursor cursor = db.rawQuery("SELECT * FROM task " +
                "WHERE text ='" + text + "';", null);
        cursor.moveToFirst();
        int resid = cursor.getInt(0);
        String restext = cursor.getString(1);

        return new Task(resid, restext, "");
    }
    public static boolean updateTaskbyId(long id, String text){
        db.execSQL("UPDATE task " +
                "SET text = '"+ text +"' " +
                "WHERE id = "+id+";");
        return true;
    }
    public static void updateTaskbyText(String prev, String next){
        db.execSQL("UPDATE task " +
                "SET text = '"+ next +"' " +
                "WHERE text = '"+prev+"';");
    }
    public static void saveAll(int idTaskList, ArrayList<Task> tasks){
        ArrayList<Task> tasks1 = getTaskFromList(idTaskList);
        for (Task task : tasks1){
            deleteTaskById(task.getId());
        }
        for (int i = 0; i <tasks.size() ; i++) {
            insertIntoTaskList(tasks.get(i).getText(), tasks.get(i).getTaskDescription(),idTaskList);
        }
        for (int i = 0; i <tasks.size() ; i++) {
            deleteSubTasks(tasks.get(i).getId());
        }
        for (int i = 0; i < tasks.size() ; i++) {
            if(tasks.get(i).getSubtasks()!=null) {
                for (int j = 0; j < tasks.get(i).getSubtasks().size(); j++) {
                    insertSubTaskToTask(tasks.get(i).getId(), tasks.get(i).getSubtasks().get(j));
                }
            }
        }
    }



}
