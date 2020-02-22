package com.skitelDev.taskmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.skitelDev.taskmanager.R;
import com.skitelDev.taskmanager.entities.Task;

public class TaskDescriptionActivity extends AppCompatActivity {

    Task task;
    TextView taskField;
    Button exitbutton;
    EditText desc;
    Button deleteTask;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_description);
        final long id = getIntent().getExtras().getLong("id");
        final String taskname = getIntent().getExtras().getString("name");
        final int pos = getIntent().getExtras().getInt("pos");
        final String taskDescription = getIntent().getExtras().getString("desc");
        task = new Task(id,taskname,taskDescription);
        taskField = findViewById(R.id.taskField);
        taskField.setText(taskname);
        taskField.clearFocus();
        desc = findViewById(R.id.taskDescription);
        desc.setText(taskDescription);
        exitbutton = findViewById(R.id.exit);
        exitbutton.setOnClickListener(view -> {
            Intent intent = new Intent(TaskDescriptionActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("id", id);
            bundle.putString("name", taskField.getText().toString());
            bundle.putInt("pos", pos);
            bundle.putString("desc", desc.getText().toString());
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
    }

}
