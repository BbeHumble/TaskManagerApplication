


package com.skitelDev.taskmanager.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.skitelDev.taskmanager.API.SubTaskDao;
import com.skitelDev.taskmanager.API.TaskDao;
import com.skitelDev.taskmanager.API.TaskManagerDatabase;
import com.skitelDev.taskmanager.R;
import com.skitelDev.taskmanager.bottomDialogFragment.BottomDialogFragment;
import com.skitelDev.taskmanager.entities.SubTask;
import com.skitelDev.taskmanager.entities.Task;
import com.skitelDev.taskmanager.recycleViewHolders.RecyclerItemClickListener;
import com.skitelDev.taskmanager.recycleViewHolders.SimpleItemTouchHelperCallback;
import com.skitelDev.taskmanager.recycleViewHolders.TaskListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.internal.observers.SubscriberCompletableObserver;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements BottomDialogFragment.ItemClickListener {
    public TaskManagerDatabase taskManagerDatabase;
    RecyclerView recyclerView;
    Button addButton;
    List<Task> dataset;
    static TaskListAdapter mAdapter;
    long id;
    String taskname;
    int pos;
    String desc;
    static TaskDao taskDao;
    static SubTaskDao subTaskDao;
//https://stackoverflow.com/questions/55949538/update-onmove-changes-in-recycler-view-data-to-room-database

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskManagerDatabase = Room.databaseBuilder(getApplicationContext(), TaskManagerDatabase.class, "TaskManagerDB")
                .allowMainThreadQueries()
                .build();
        recyclerView = findViewById(R.id.recycler_view);
        bottomSheet();
        taskDao = taskManagerDatabase.getTaskDao();
        subTaskDao = taskManagerDatabase.getSubTaskDao();
        dataset = taskManagerDatabase.getTaskDao().getAllTasks();
        for (int i = 0; i <dataset.size() ; i++) {
            dataset.get(i).setSubTaskList(subTaskDao.getAllSubTasks(dataset.get(i).getId()));
        }
        if (getIntent().getExtras() == null)
            TaskListLoader(dataset);
        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getLong("id");
            if (id != -1) {
                taskname = getIntent().getExtras().getString("name");
                pos = getIntent().getExtras().getInt("pos");
                desc = getIntent().getExtras().getString("desc");
                dataset.set(pos, new Task(id, taskname, desc));
                dataset.get(pos).setSubTaskList(subTaskDao.getAllSubTasks(dataset.get(pos).getId()));
                taskDao.updateTask(dataset.get(pos));
                TaskListLoader(dataset);
                mAdapter.notifyItemChanged(pos);
            } else {
                TaskListLoader(dataset);
                pos = getIntent().getExtras().getInt("position");
                deleteItem(pos);
                Toast toast = Toast.makeText(getApplicationContext(), "Task deleted", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, (view, position) -> {
                    Intent intent = new Intent(MainActivity.this, TaskDescriptionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("id", TaskListAdapter.mDataset.get(position).getId());
                    bundle.putString("name", TaskListAdapter.mDataset.get(position).getText());
                    bundle.putInt("pos", position);
                    bundle.putString("desc", TaskListAdapter.mDataset.get(position).getTaskDescription());
                    ArrayList<String> subtasksStrings = new ArrayList<>();
                    ArrayList<SubTask> subTasks = (ArrayList<SubTask>) TaskListAdapter.mDataset.get(position).getSubTaskList();

                    long[] subtasksIds = new long[subTasks.size()];
                    for (int i = 0; i < subTasks.size(); i++) {
                        subtasksStrings.add(subTasks.get(i).getSubTaskText());
                        subtasksIds[i] = subTasks.get(i).id;
                    }
                    bundle.putLongArray("subtasks_ids", subtasksIds);
                    bundle.putStringArrayList("subtasks", subtasksStrings);

                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                })
        );

    }

    @Override
    public void onItemClick(String newTaskText, String desc) {
        if (!newTaskText.trim().equals("")) {
            int index = taskDao.getAllTasks().size() + 1;
            Task task = new Task(index, newTaskText, desc);
            task.setSubTaskList(new ArrayList<>());
            TaskListAdapter.mDataset.add(task);
            mAdapter.notifyItemInserted(TaskListAdapter.mDataset.size());
            taskDao.addTask(task);
        }
        hideBottom();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static void deleteItem(int position) {
        taskDao.deleteTask(new Task(TaskListAdapter.mDataset.get(position).getId(), TaskListAdapter.mDataset.get(position).getText(), TaskListAdapter.mDataset.get(position).getTaskDescription()));
        subTaskDao.deleteSubTasksById(TaskListAdapter.mDataset.get(position).getId());
        TaskListAdapter.mDataset.remove(position);
        mAdapter.notifyItemRemoved(position);

    }


    @SuppressLint("ClickableViewAccessibility")
    private void bottomSheet() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        recyclerView = findViewById(R.id.recycler_view);
        addButton = findViewById(R.id.addbutton);
        final RelativeLayout relativeLayout = findViewById(R.id.frame);

        addButton.setOnClickListener(view -> {
            relativeLayout.bringToFront();
            BottomDialogFragment addPhotoBottomDialogFragment =
                    BottomDialogFragment.newInstance();
            addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                    "dialog_fragment");
        });


        relativeLayout.setOnClickListener(view -> hideBottom());
    }

    private void hideBottom() {
        addButton.setVisibility(View.VISIBLE);
        recyclerView.bringToFront();
    }

    public static void onMove(ArrayList<Task> tasks, int from, int to) {
        for (int i = 0; i <tasks.size() ; i++) {
            taskDao.addTask(tasks.get(i));
        }
////        Task task1 = TaskListAdapter.mDataset.get(from);
////        Task task2 = TaskListAdapter.mDataset.get(to);
////        taskDao.addTask(new Task(task1.getId(), task2.getText(), task2.getTaskDescription()));
////        taskDao.addTask(new Task(task2.getId(), task1.getText(), task1.getTaskDescription()));
        List<SubTask> subTasks1 = subTaskDao.getAllSubTasks(tasks.get(from).getId());
        List<SubTask> subTasks2 = subTaskDao.getAllSubTasks(tasks.get(to).getId());
        if (subTasks1.size() != 0 && subTasks2.size() != 0) {
            for (int j = 0; j < subTasks1.size(); j++) {
                subTaskDao.addSubTask(new SubTask(subTasks1.get(j).id, tasks.get(to).getId(), subTasks1.get(j).getSubTaskText()));

            }
            for (int j = 0; j < subTasks2.size(); j++) {
                subTaskDao.addSubTask(new SubTask(subTasks2.get(j).id, tasks.get(from).getId(), subTasks2.get(j).getSubTaskText()));
            }
        }
         else if (subTasks2.size() == 0 && subTasks1.size() != 0) {
            for (int j = 0; j < subTasks1.size(); j++) {
                subTaskDao.addSubTask(new SubTask(subTasks1.get(j).id, TaskListAdapter.mDataset.get(to).getId(), subTasks1.get(j).getSubTaskText()));
            }
        }
        else if (subTasks1.size()==0 && subTasks2.size()!=0){
            for (int j = 0; j < subTasks2.size(); j++) {
                subTaskDao.addSubTask(new SubTask(subTasks2.get(j).id, TaskListAdapter.mDataset.get(from).getId(), subTasks2.get(j).getSubTaskText()));
            }
        }


    }


    public void TaskListLoader(List<Task> list) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager1);
        mAdapter = new TaskListAdapter(getApplicationContext(), list);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
    }

}

