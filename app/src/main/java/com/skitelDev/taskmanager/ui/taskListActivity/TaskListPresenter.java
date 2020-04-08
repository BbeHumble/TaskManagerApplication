package com.skitelDev.taskmanager.ui.taskListActivity;

import com.skitelDev.taskmanager.data.Storage;
import com.skitelDev.taskmanager.data.model.SubTask;
import com.skitelDev.taskmanager.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskListPresenter {
    private TaskListView mView;
    private Storage storage;

    public TaskListPresenter(TaskListView mView, Storage storage) {
        this.mView = mView;
        this.storage = storage;
    }

    void getAllTasks() {
        mView.showTasks(storage.getTasks());

    }

    void showAddTask() {
        mView.showAddTask();
    }


    public void deleteTask(Task task) {
        storage.deleteTask(task);
        storage.deleteSubTasksById(task.getId());
        mView.deleteTask(task);
    }

    public void hideAddTask() {
        mView.hideAddTask();
    }

    void moveTask(int from, int to) {
        List<Task> dataset = storage.getTasks();
        Task task1 = dataset.get(from);
        Task task2 = dataset.get(to);
        storage.updateTask((new Task(task1.getId(), task2.getText(), task2.getTaskDescription())));
        storage.updateTask(new Task(task2.getId(), task1.getText(), task1.getTaskDescription()));
        List<SubTask> subTasks1 = storage.getSubtasks(dataset.get(from).getId());
        List<SubTask> subTasks2 = storage.getSubtasks(dataset.get(to).getId());
        if (subTasks1.size() != 0 && subTasks2.size() != 0) {
            for (int j = 0; j < subTasks1.size(); j++) {
                storage.addSubTask(new SubTask(subTasks1.get(j).id, dataset.get(to).getId(), subTasks1.get(j).getSubTaskText()));
            }
            for (int j = 0; j < subTasks2.size(); j++) {
                storage.addSubTask(new SubTask(subTasks2.get(j).id, dataset.get(from).getId(), subTasks2.get(j).getSubTaskText()));
            }
        } else if (subTasks2.size() == 0 && subTasks1.size() != 0) {
            for (int j = 0; j < subTasks1.size(); j++) {
                storage.addSubTask(new SubTask(subTasks1.get(j).id, TaskListAdapter.mDataset.get(to).getId(), subTasks1.get(j).getSubTaskText()));
            }
        } else {
            if (subTasks2.size() != 0) {
                for (int j = 0; j < subTasks2.size(); j++) {
                    storage.addSubTask(new SubTask(subTasks2.get(j).id, TaskListAdapter.mDataset.get(from).getId(), subTasks2.get(j).getSubTaskText()));
                }
            }
        }
        mView.moveTask(from, to);
    }

    List<SubTask> provideSubTasks(long id) {
        return storage.getSubtasks(id);
    }

    void updateTask(Task task) {
        storage.updateTask(task);
    }


    public void showTaskDescription(int position) {
        mView.showTaskDescription(position);
    }
}
