package com.skitelDev.taskmanager.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.skitelDev.taskmanager.data.model.SubTask;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface SubTaskDao {
    @Insert(onConflict = REPLACE)
    long addSubTask(SubTask subTask);

    @Update(onConflict = REPLACE)
    void updateSubTask(SubTask subTask);

    @Query("delete from subtasks where subtask_id==:id")
    void deleteSubTask(long id);

    @Query("delete from subtasks where taskid==:id")
    void deleteSubTasksById(long id);

    @Query("select * from subtasks where taskid ==:id")
    List<SubTask> getAllSubTasks(long id);

    @Query("delete from subtasks")
    void deleleAllSubtasks();

    @Update
    int updateAllSubtasks(List<SubTask> subTasks);
}
