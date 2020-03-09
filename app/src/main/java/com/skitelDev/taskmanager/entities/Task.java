package com.skitelDev.taskmanager.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private long id;
    @ColumnInfo(name = "task_title")
    private String text;
    @ColumnInfo(name = "task_description")
    private String taskDescription;

    public Task(long id, String text, String taskDescription) {
        this.id = id;
        this.text = text;
        this.taskDescription = taskDescription;
    }

    @Ignore
    public Task() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
}
