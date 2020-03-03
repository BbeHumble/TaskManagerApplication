package com.skitelDev.taskmanager.API;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.RoomDatabase;

import com.skitelDev.taskmanager.entities.SubTask;
import com.skitelDev.taskmanager.entities.Task;

@Database(entities = {Task.class, SubTask.class}, version = 1, exportSchema = false)
public abstract class TaskManagerDatabase extends RoomDatabase {
    public abstract TaskDao getTaskDao();
    public abstract SubTaskDao getSubTaskDao();
}
