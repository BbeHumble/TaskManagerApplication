
package com.skitelDev.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
        Button save = findViewById(R.id.savebutton);
        bottomsheet.clearFocus();
        addButton = findViewById(R.id.button);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                addButton.setVisibility(View.GONE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
                EditText editText = findViewById(R.id.newTaskTextField);
                API.insertIntoTaskList(db,editText.getText().toString(),1);
                ArrayList<Task> list = API.getTaskFromList(db, 1);
                TaskListLoader(list);
//         TODO: ТУТ НАДО ЧТО-ТО СДЕЛАТЬ

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
