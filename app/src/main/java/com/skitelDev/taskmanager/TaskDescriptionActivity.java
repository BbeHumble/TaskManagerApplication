package com.skitelDev.taskmanager;

import android.content.Intent;
import android.os.Bundle;

import com.skitelDev.taskmanager.entities.Task;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TaskDescriptionActivity extends AppCompatActivity {

    Task task;
    static TextView taskField;
    Button exitbutton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_description);
        final long id = getIntent().getExtras().getLong("id");
        final String taskname = getIntent().getExtras().getString("name");
        final int pos = getIntent().getExtras().getInt("pos");
        task = new Task(id,taskname);
        taskField = findViewById(R.id.textView);
        taskField.setText(taskname);
        exitbutton = findViewById(R.id.exit);
        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskDescriptionActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", id);
                bundle.putString("name", taskField.getText().toString());
                bundle.putInt("pos", pos);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

}
