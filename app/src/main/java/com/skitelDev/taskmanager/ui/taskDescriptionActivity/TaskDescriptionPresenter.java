package com.skitelDev.taskmanager.ui.taskDescriptionActivity;

import com.skitelDev.taskmanager.data.Storage;
import com.skitelDev.taskmanager.data.model.SubTask;
import com.skitelDev.taskmanager.ui.taskListActivity.TaskListView;

import java.util.ArrayList;

public class TaskDescriptionPresenter {
    private TaskDescriptionView mView;
    private Storage storage;

    public TaskDescriptionPresenter(TaskDescriptionView mView, Storage storage) {
        this.mView = mView;
        this.storage = storage;
    }

    public void updateSubTask(SubTask subTask) {
        storage.updateSubtask(subTask);
    }

    public void addSubTask(SubTask subTask) {
        storage.addSubTask(subTask);
    }

    public void deleteSubTask(long subtasksId) {
        storage.deleteSubTasksById(subtasksId);
    }

    void getAllSubTasks(ArrayList<String> dataset) {
        mView.showTaskInfo(dataset);
    }
}
