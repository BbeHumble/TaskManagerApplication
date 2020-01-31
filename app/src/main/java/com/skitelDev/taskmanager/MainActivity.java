
package com.skitelDev.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.skitelDev.taskmanager.API.API;
import com.skitelDev.taskmanager.entities.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayout bottomsheet;
    Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomSheet();
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        API.createDatabase(db);
        ArrayList<Task> list = API.getTaskFromList(db, 1);
        TaskListLoader(list);
    }

    private void bottomSheet() {
        bottomsheet = findViewById(R.id.bottom_sheet);
        addButton = findViewById(R.id.button);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void TaskListLoader(ArrayList<Task> list) {
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager1);
        TaskListAdapter mAdapter = new TaskListAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(mAdapter);
    }




}
