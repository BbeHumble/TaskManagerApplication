package com.skitelDev.taskmanager.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.skitelDev.taskmanager.data.model.Task;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TaskDao {
    @Insert(onConflict = REPLACE)
    long addTask(Task task);

    @Update(onConflict = REPLACE)
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("select * from tasks")
    List<Task> getAllTasks();

    @Query("select * from tasks where task_id ==:id")
    Task getTask(long id);

    @Query("delete from tasks")
    void deleteAllTasks();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAll(List<Task> tasks);
}
