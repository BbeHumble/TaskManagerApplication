


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
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomDialogFragment.ItemClickListener {
    public TaskManagerDatabase taskManagerDatabase;
    RecyclerView recyclerView;
    Button addButton;
    List<Task> dataset;
    TaskListAdapter mAdapter;
    long id;
    String taskname;
    int pos;
    String desc;
    TaskDao taskDao;
    SubTaskDao subTaskDao;

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
        if (getIntent().getExtras() == null)
            TaskListLoader(dataset);
        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getLong("id");
            if (id != -1) {
                taskname = getIntent().getExtras().getString("name");
                pos = getIntent().getExtras().getInt("pos");
                desc = getIntent().getExtras().getString("desc");
                ArrayList<String> subtasks = getIntent().getExtras().getStringArrayList("subtasks");
                long[] prev_subtasksIDs = getIntent().getExtras().getLongArray("prev_subtaks_ids");
                dataset.set(pos, new Task(id, taskname, desc));
                taskDao.updateTask(new Task(id, taskname, desc));
                if (prev_subtasksIDs.length == subtasks.size()) {
                    for (int i = 0; i < subtasks.size(); i++) {
                        subTaskDao.updateSubTask(new SubTask(prev_subtasksIDs[i], id, subtasks.get(i)));
                    }
                }
                if (prev_subtasksIDs.length < subtasks.size()) {
                    for (int i = 0; i < prev_subtasksIDs.length; i++) {
                        subTaskDao.updateSubTask(new SubTask(prev_subtasksIDs[i], id, subtasks.get(i)));
                    }
                    for (int i = prev_subtasksIDs.length; i < subtasks.size(); i++) {
                        subTaskDao.addSubTask(new SubTask(id, subtasks.get(i)));
                    }
                }
                if (prev_subtasksIDs.length > subtasks.size()) {
                    for (int i = 0; i < subtasks.size(); i++) {
                        subTaskDao.updateSubTask(new SubTask(prev_subtasksIDs[i], id, subtasks.get(i)));
                    }
                    for (int i = subtasks.size(); i < prev_subtasksIDs.length; i++) {
                        subTaskDao.deleteSubTask(prev_subtasksIDs[i]);
                    }
                }
                taskDao.updateTask(dataset.get(pos));
                TaskListLoader(dataset);
                mAdapter.notifyItemChanged(pos);
            } else {
                TaskListLoader(dataset);
                pos = getIntent().getExtras().getInt("position");
                dataset.remove(pos);
                mAdapter.notifyItemRemoved(pos);
                taskDao.deleteTask(dataset.get(pos));
                Toast toast = Toast.makeText(getApplicationContext(), "Task deleted", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, (view, position) -> {
                    Intent intent = new Intent(MainActivity.this, TaskDescriptionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("id", dataset.get(position).getId());
                    bundle.putString("name", dataset.get(position).getText());
                    bundle.putInt("pos", position);
                    bundle.putString("desc", dataset.get(position).getTaskDescription());
                    ArrayList<String> subtasksStrings = new ArrayList<>();
                    ArrayList<SubTask> subTasks = (ArrayList<SubTask>) subTaskDao.getAllSubTasks(dataset.get(position).getId());
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
            Task task = new Task(taskDao.getAllTasks().size() + 1, newTaskText, desc);
            TaskListAdapter.mDataset.add(task);
            mAdapter.notifyItemInserted(TaskListAdapter.mDataset.size());
            taskDao.addTask(task);
        }
        hideBottom();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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

//    @Override
//    protected void onStop() {
//        super.onStop();
//        taskDao.deleteAllTasks();
//        for (int i = 0; i < TaskListAdapter.mDataset.size() ; i++) {
//            taskDao.addTask(TaskListAdapter.mDataset.get(i));
//        }
//        ArrayList<String[]> taskswithSubtasks = new ArrayList<>();
//        for (int i = 0; i < TaskListAdapter.mDataset.size(); i++) {
//            List<SubTask> subTasks = subTaskDao.getAllSubTasks(TaskListAdapter.mDataset.get(i).getId());
//            String [] subtasksString = new String[subTasks.size()];
//            for (int j = 0; j < subTasks.size(); j++) {
//                subtasksString[j] = subTasks.get(j).subTaskText;
//            }
//            taskswithSubtasks.add(subtasksString);
//        }
//        subTaskDao.deleleAllSubtasks();
//        for (int i = 0; i < taskswithSubtasks.size() ; i++) {
//            for (int j = 0; j <taskswithSubtasks.get(i).length ; j++) {
//                subTaskDao.addSubTask(new SubTask(TaskListAdapter.mDataset.get(i).getId(),taskswithSubtasks.get(i)[j]));
//            }
//        }
//
//    }

}