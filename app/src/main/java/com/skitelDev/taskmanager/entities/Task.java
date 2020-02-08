package com.skitelDev.taskmanager.entities;

public class Task {
    private long id;
    private String text;
    private String taskDescription;

    public Task(long id, String text, String taskDescription) {
        this.id = id;
        this.text = text;
        this.taskDescription = taskDescription;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
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
}
