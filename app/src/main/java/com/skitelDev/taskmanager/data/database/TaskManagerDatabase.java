package com.skitelDev.taskmanager.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.skitelDev.taskmanager.data.model.SubTask;
import com.skitelDev.taskmanager.data.model.Task;

@Database(entities = {Task.class, SubTask.class}, version = 1, exportSchema = false)
public abstract class TaskManagerDatabase extends RoomDatabase {
    public abstract TaskDao getTaskDao();
    public abstract SubTaskDao getSubTaskDao();
}
