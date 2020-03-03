package com.skitelDev.taskmanager.activities;

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

import com.skitelDev.taskmanager.API.API;
import com.skitelDev.taskmanager.R;
import com.skitelDev.taskmanager.entities.SubTask;
import com.skitelDev.taskmanager.entities.Task;
import com.skitelDev.taskmanager.recycleViewHolders.subTaskListAdapters.SubTaskAdapter;

import java.util.ArrayList;

public class TaskDescriptionActivity extends AppCompatActivity {

    Task task;
    TextView taskField;
    Button exitbutton;
    EditText desc;
    Button deleteTask;
    RecyclerView subtasks;
    SubTaskAdapter subTaskAdapter;
    ArrayList<String> dataset;
    Button addSubTask;
    long id;
    int pos;
    String taskname;
    long[] subtasksIds;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_description);
        id = getIntent().getExtras().getLong("id");
        taskname = getIntent().getExtras().getString("name");
        pos = getIntent().getExtras().getInt("pos");
        final String taskDescription = getIntent().getExtras().getString("desc");
        dataset = getIntent().getExtras().getStringArrayList("subtasks");
        subtasksIds = getIntent().getExtras().getLongArray("subtasks_ids");
        subtasks = findViewById(R.id.subTask);
        TaskListLoader(dataset);
        addSubTask = findViewById(R.id.addSubTask);
        task = new Task(id, taskname, taskDescription);
        taskField = findViewById(R.id.taskField);
        taskField.setText(taskname);
        taskField.clearFocus();
        desc = findViewById(R.id.taskDescription);
        desc.setText(taskDescription);
        exitbutton = findViewById(R.id.exit);
        if (SubTaskAdapter.mDataset != null) {
            subtasks.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.subTask);
            params.setMargins(70, 0, 0, 0);
            addSubTask.setLayoutParams(params);
        }
        exitbutton.setOnClickListener(view -> {
            if (!taskField.getText().toString().equals("")) {
                saveAndExit(id, pos);
            } else {
                deleteAndExit(pos);
            }
        });
        deleteTask = findViewById(R.id.deleteTask);
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
        if(SubTaskAdapter.mDataset!=null) {
            SubTaskAdapter.mDataset.remove("");
        }
        bundle.putLongArray("prev_subtaks_ids", subtasksIds);
        bundle.putStringArrayList("subtasks", SubTaskAdapter.mDataset);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public void TaskListLoader(ArrayList<String> list) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        subtasks.setLayoutManager(layoutManager1);
        subTaskAdapter = new SubTaskAdapter(getApplicationContext(), list);
        subtasks.setAdapter(subTaskAdapter);
    }

    @Override
    public void onBackPressed() {
        if(taskField.getText().toString().trim().equals("")){
            deleteAndExit(pos);
        }else {
            saveAndExit(id,pos);
        }
    }

}
