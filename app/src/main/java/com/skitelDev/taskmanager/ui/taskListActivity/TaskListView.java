package com.skitelDev.taskmanager.ui.taskListActivity;

import com.skitelDev.taskmanager.data.model.Task;

import java.util.List;

public interface TaskListView {
    void showTasks(List<Task> projects);
    void showAddTask();
    void hideAddTask();
    void deleteTask(Task task);
    void moveTask(int from, int to);
    void showTaskDescription(int pos);

}
