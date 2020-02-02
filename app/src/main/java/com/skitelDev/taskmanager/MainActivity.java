
package com.skitelDev.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.skitelDev.taskmanager.API.API;
import com.skitelDev.taskmanager.entities.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RelativeLayout bottomsheet;
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

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }

        return super.dispatchTouchEvent(event);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bottomSheet() {
        bottomsheet = findViewById(R.id.bottom_sheet);
        Button save = findViewById(R.id.savebutton);
        bottomsheet.clearFocus();
        addButton = findViewById(R.id.addbutton);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                addButton.setVisibility(View.INVISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
                EditText editText = findViewById(R.id.newTaskTextField);
                API.insertIntoTaskList(db, editText.getText().toString(), 1);
                ArrayList<Task> list = API.getTaskFromList(db, 1);
                TaskListLoader(list);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                addButton.setVisibility(View.VISIBLE);
            }
        });


    }

    private void TaskListLoader(ArrayList<Task> list) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager1);
        TaskListAdapter mAdapter = new TaskListAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(mAdapter);
    }


}
