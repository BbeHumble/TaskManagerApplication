package com.skitelDev.taskmanager.API;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.skitelDev.taskmanager.entities.SubTask;
import com.skitelDev.taskmanager.entities.Task;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface SubTaskDao {
    @Insert
    long addSubTask(SubTask subTask);

    @Update
    void updateSubTask(SubTask subTask);

    @Query("delete from subtasks where subtask_id==:id")
    void deleteSubTask(long id);

    @Query("select * from subtasks where taskid ==:id")
    List<SubTask> getAllSubTasks(long id);

}
