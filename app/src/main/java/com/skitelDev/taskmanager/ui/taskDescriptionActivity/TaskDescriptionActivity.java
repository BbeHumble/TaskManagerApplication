package com.skitelDev.taskmanager.ui.taskDescriptionActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.skitelDev.taskmanager.R;
import com.skitelDev.taskmanager.data.Storage;
import com.skitelDev.taskmanager.data.database.TaskManagerDatabase;
import com.skitelDev.taskmanager.data.model.SubTask;
import com.skitelDev.taskmanager.data.model.Task;
import com.skitelDev.taskmanager.recycleViewHolders.subTaskListAdapters.SubTaskAdapter;
import com.skitelDev.taskmanager.ui.taskListActivity.MainActivity;

import java.util.ArrayList;


public class TaskDescriptionActivity extends AppCompatActivity implements TaskDescriptionView {

    TextView taskField;
    Button exitbutton;
    EditText desc;
    Button deleteTask;
    RecyclerView subtasks;
    SubTaskAdapter subTaskAdapter;
    ArrayList<String> dataset;
    Button addSubTask;
    int pos;
    long[] subtasksIds;
    Storage storage;
    TaskDescriptionPresenter presenter;
    Task currentTask;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_description);
        storage = provideStorage();
        presenter = new TaskDescriptionPresenter(this, storage);
        currentTask = new Task();
        currentTask.setId(getIntent().getExtras().getLong("id"));
        currentTask.setText(getIntent().getExtras().getString("name"));
        pos = getIntent().getExtras().getInt("pos");
        currentTask.setTaskDescription(getIntent().getExtras().getString("desc"));
        dataset = getIntent().getExtras().getStringArrayList("subtasks");
        subtasksIds = getIntent().getExtras().getLongArray("subtasks_ids");
        initItems(currentTask);
        presenter.getAllSubTasks(dataset);


        if (SubTaskAdapter.mDataset != null) {
            subtasks.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.subTask);
            params.setMargins(70, 0, 0, 0);
            addSubTask.setLayoutParams(params);
        }
        exitbutton.setOnClickListener(view -> {
            if (!taskField.getText().toString().equals("")) {
                saveAndExit(currentTask.getId(), pos);
            } else {
                deleteAndExit(pos);
            }
        });

        deleteTask.setOnClickListener(view -> {
            deleteAndExit(pos);
        });
        addSubTask.setOnClickListener(view -> {
            if (subtasks.getVisibility() == View.GONE) {
                subtasks.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, R.id.subTask);
                params.setMargins(70, 0, 0, 0);
                addSubTask.setLayoutParams(params);
                if (SubTaskAdapter.mDataset == null) {
                    SubTaskAdapter.mDataset = new ArrayList<>();
                    SubTaskAdapter.mDataset.add("");
                } else {
                    SubTaskAdapter.mDataset.add("");
                }
                subTaskAdapter.notifyItemInserted(SubTaskAdapter.mDataset.size());
            } else {
                if (SubTaskAdapter.mDataset == null) {
                    SubTaskAdapter.mDataset = new ArrayList<>();
                    SubTaskAdapter.mDataset.add("");
                } else {
                    SubTaskAdapter.mDataset.add("");
                }
                subTaskAdapter.notifyItemInserted(SubTaskAdapter.mDataset.size());
            }
        });

    }

    private void initItems(Task currentTask) {
        subtasks = findViewById(R.id.subTask);
        deleteTask = findViewById(R.id.deleteTask);
        addSubTask = findViewById(R.id.addSubTask);
        taskField = findViewById(R.id.taskField);
        desc = findViewById(R.id.taskDescription);
        exitbutton = findViewById(R.id.exit);
        taskField.setText(currentTask.getText());
        taskField.clearFocus();
        desc.setText(currentTask.getTaskDescription());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (subtasksIds.length == SubTaskAdapter.mDataset.size()) {
            for (int i = 0; i < SubTaskAdapter.mDataset.size(); i++) {

                presenter.updateSubTask(new SubTask(subtasksIds[i], currentTask.getId(), SubTaskAdapter.mDataset.get(i)));
            }
        }
        if (subtasksIds.length < SubTaskAdapter.mDataset.size()) {
            for (int i = 0; i < subtasksIds.length; i++) {
                presenter.updateSubTask(new SubTask(subtasksIds[i], currentTask.getId(), SubTaskAdapter.mDataset.get(i)));
            }
            for (int i = subtasksIds.length; i < SubTaskAdapter.mDataset.size(); i++) {
                presenter.addSubTask(new SubTask(currentTask.getId(), SubTaskAdapter.mDataset.get(i)));
            }
        }
        if (subtasksIds.length > SubTaskAdapter.mDataset.size()) {
            for (int i = 0; i < SubTaskAdapter.mDataset.size(); i++) {
                presenter.updateSubTask(new SubTask(subtasksIds[i], currentTask.getId(), SubTaskAdapter.mDataset.get(i)));
            }
            for (int i = SubTaskAdapter.mDataset.size(); i < subtasksIds.length; i++) {
                presenter.deleteSubTask(subtasksIds[i]);
            }
        }
    }

    private void deleteAndExit(int pos) {
        Intent intent = new Intent(TaskDescriptionActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id", -1);
        bundle.putInt("position", pos);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void saveAndExit(long id, int pos) {
        Intent intent = new Intent(TaskDescriptionActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        bundle.putString("name", taskField.getText().toString());
        bundle.putInt("pos", pos);
        bundle.putString("desc", desc.getText().toString());
        if (SubTaskAdapter.mDataset != null) {
            SubTaskAdapter.mDataset.remove("");
        }
        bundle.putLongArray("prev_subtaks_ids", subtasksIds);
        bundle.putStringArrayList("subtasks", SubTaskAdapter.mDataset);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (taskField.getText().toString().trim().equals("")) {
            deleteAndExit(pos);
        } else {
            saveAndExit(currentTask.getId(), pos);
        }
    }

    Storage provideStorage() {
        final TaskManagerDatabase database = Room.databaseBuilder(this, TaskManagerDatabase.class, "TaskManagerDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        return new Storage(database.getTaskDao(), database.getSubTaskDao());
    }

    @Override
    public void showTaskInfo(ArrayList<String> list) {
        dataset = list;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        subtasks.setLayoutManager(layoutManager1);
        subTaskAdapter = new SubTaskAdapter(getApplicationContext(), list);
        subtasks.setAdapter(subTaskAdapter);
    }
}
