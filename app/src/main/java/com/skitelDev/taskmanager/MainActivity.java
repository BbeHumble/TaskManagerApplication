
package com.skitelDev.taskmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.skitelDev.taskmanager.API.API;
import com.skitelDev.taskmanager.entities.Task;
import com.skitelDev.taskmanager.recycleViewHolders.SimpleItemTouchHelperCallback;
import com.skitelDev.taskmanager.recycleViewHolders.TaskListAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RelativeLayout bottomsheet;
    Button addButton;
    BottomSheetBehavior bottomSheetBehavior;
    static SQLiteDatabase db;
    private ItemTouchHelper mItemTouchHelper;
    ArrayList<Task> dataset;
    TaskListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        bottomsheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);
        bottomSheet();
        db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        API.createDatabase(db);
        dataset = API.getTaskFromList(db, 1);
        TaskListLoader(dataset);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            hideBottom();
        } else {
            super.onBackPressed();
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void bottomSheet() {
        recyclerView = findViewById(R.id.recycler_view);
        bottomsheet = findViewById(R.id.bottom_sheet);
        Button save = findViewById(R.id.savebutton);

        addButton = findViewById(R.id.addbutton);
        final RelativeLayout relativeLayout = findViewById(R.id.frame);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.bringToFront();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                addButton.setVisibility(View.INVISIBLE);
                EditText editText = findViewById(R.id.newTaskTextField);
                editText.requestFocus();
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideBottom();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
                EditText editText = findViewById(R.id.newTaskTextField);
                TaskListAdapter.mDataset.add(new Task(API.findLastTaskID(db), editText.getText().toString()));
                mAdapter.notifyItemInserted(TaskListAdapter.mDataset.size() - 1);
                hideBottom();
            }
        });


    }

    public void TaskListLoader(ArrayList<Task> list) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager1);
        mAdapter = new TaskListAdapter(getApplicationContext(), list);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
    }

    private void hideBottom() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        EditText editText = findViewById(R.id.newTaskTextField);
        editText.setText("");
        addButton.setVisibility(View.VISIBLE);
        recyclerView.bringToFront();
    }


    @Override
    protected void onStop() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        API.saveAll(db, 1, TaskListAdapter.mDataset);
        super.onStop();
    }
}
