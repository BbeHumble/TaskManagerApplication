package com.skitelDev.taskmanager.API;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.skitelDev.taskmanager.entities.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    long addTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("select * from tasks")
    List<Task> getAllTasks();

    @Query("select * from tasks where task_id ==:id")
    Task getTask(long id);
}
