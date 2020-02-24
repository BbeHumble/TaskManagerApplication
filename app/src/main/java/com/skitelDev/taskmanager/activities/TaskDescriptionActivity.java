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

import com.skitelDev.taskmanager.R;
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_description);
        final long id = getIntent().getExtras().getLong("id");
        final String taskname = getIntent().getExtras().getString("name");
        final int pos = getIntent().getExtras().getInt("pos");
        final String taskDescription = getIntent().getExtras().getString("desc");
//        dataset = API.getSubTasksByTaskId(id);
        dataset = getIntent().getExtras().getStringArrayList("subtasks");
        subtasks = findViewById(R.id.subTask);
        TaskListLoader(dataset);
        addSubTask = findViewById(R.id.addSubTask);
        task = new Task(id,taskname,taskDescription);
        taskField = findViewById(R.id.taskField);
        taskField.setText(taskname);
        taskField.clearFocus();
        desc = findViewById(R.id.taskDescription);
        desc.setText(taskDescription);
        exitbutton = findViewById(R.id.exit);
        if (SubTaskAdapter.mDataset.size()>0) {
            subtasks.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.subTask);
            params.setMargins(70, 0, 0, 0);
            addSubTask.setLayoutParams(params);
        }
        exitbutton.setOnClickListener(view -> {
            Intent intent = new Intent(TaskDescriptionActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("id", id);
            bundle.putString("name", taskField.getText().toString());
            bundle.putInt("pos", pos);
            bundle.putString("desc", desc.getText().toString());
            bundle.putStringArrayList("subtasks", SubTaskAdapter.mDataset);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        });
        deleteTask = findViewById(R.id.deleteTask);
        deleteTask.setOnClickListener(view -> {
            Intent intent = new Intent(TaskDescriptionActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("id", -1);
            bundle.putInt("position",pos);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        });
        addSubTask.setOnClickListener(view -> {
            if (subtasks.getVisibility()== View.GONE){
                subtasks.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, R.id.subTask);
                params.setMargins(70,0,0,0);
                addSubTask.setLayoutParams(params);
                SubTaskAdapter.mDataset.add("");
            }
            else{
                SubTaskAdapter.mDataset.add("");
                subTaskAdapter.notifyItemInserted(SubTaskAdapter.mDataset.size());
            }
        });
    }
    public void TaskListLoader(ArrayList<String> list) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        subtasks.setLayoutManager(layoutManager1);
        subTaskAdapter = new SubTaskAdapter(getApplicationContext(), list);
        subtasks.setAdapter(subTaskAdapter);
    }

}
