package com.skitelDev.taskmanager.entities;

import java.util.ArrayList;

public class Task {
    private long id;
    private String text;
    private String taskDescription;
    private ArrayList<String> subtasks;

    public Task(long id, String text, String taskDescription) {
        this.id = id;
        this.text = text;
        this.taskDescription = taskDescription;
    }
    public Task(long id, String text, String taskDescription, ArrayList<String> subtasks) {
        this.id = id;
        this.text = text;
        this.taskDescription = taskDescription;
        this.subtasks = subtasks;
    }

    public Task() {
    }

    public ArrayList<String> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<String> subtasks) {
        this.subtasks = subtasks;
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
