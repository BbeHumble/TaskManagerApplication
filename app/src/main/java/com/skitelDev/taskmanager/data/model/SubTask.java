package com.skitelDev.taskmanager.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "subtasks")
public
class SubTask {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "subtask_id")
    public long id;
    @ForeignKey(entity = Task.class, parentColumns = "task_id", childColumns = "taskid")
    @ColumnInfo(name = "taskid")
    private long taskid;
    @ColumnInfo(name = "subtask_text")
    public String subTaskText;

    public long getTaskid() {
        return taskid;
    }

    public void setTaskid(long taskid) {
        this.taskid = taskid;
    }

    public String getSubTaskText() {
        return subTaskText;
    }

    public void setSubTaskText(String subTaskText) {
        this.subTaskText = subTaskText;
    }

    @Ignore
    public SubTask(long taskid, String subTaskText) {
        this.taskid = taskid;
        this.subTaskText = subTaskText;
    }

    public SubTask(long id, long taskid, String subTaskText) {
        this.id = id;
        this.taskid = taskid;
        this.subTaskText = subTaskText;
    }
}
