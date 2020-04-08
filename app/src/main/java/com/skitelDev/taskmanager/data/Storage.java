package com.skitelDev.taskmanager.data;

import com.skitelDev.taskmanager.data.database.SubTaskDao;
import com.skitelDev.taskmanager.data.database.TaskDao;
import com.skitelDev.taskmanager.data.model.SubTask;
import com.skitelDev.taskmanager.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class Storage {
    TaskDao taskDao;
    SubTaskDao subTaskDao;

    public Storage(TaskDao taskDao, SubTaskDao subTaskDao) {
        this.taskDao = taskDao;
        this.subTaskDao = subTaskDao;
    }

    public List<Task> getTasks() {
        List<Task> tasks = taskDao.getAllTasks();
        for (Task task : tasks) {
            task.setSubTaskList(subTaskDao.getAllSubTasks(task.getId()));
        }
        return tasks;
    }

    public void insertTask(Task task) {
//        if (task.getSubTaskList().size()==0) {
//            task.setSubTaskList(new ArrayList<>());
//        }
        taskDao.addTask(task);
    }

    public void updateTask(Task task) {
        taskDao.updateTask(task);
    }
    public void deleteTask(Task task){
        taskDao.deleteTask(task);
    }
    public void updateSubtask(SubTask subTask){
        subTaskDao.updateSubTask(subTask);
    }
    public List<SubTask> getSubtasks(long id) {
        return subTaskDao.getAllSubTasks(id);
    }

    public void deleteSubTasksById(long id) {
        subTaskDao.deleteSubTasksById(id);
    }

    public void addSubTask(SubTask subTask) {
        subTaskDao.addSubTask(subTask);
    }


    public interface StorageOwner {
        Storage obtainStorage();
    }
}
