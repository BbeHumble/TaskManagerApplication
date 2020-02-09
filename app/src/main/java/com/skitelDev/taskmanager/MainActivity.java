
package com.skitelDev.taskmanager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CpuUsageInfo;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.skitelDev.taskmanager.API.API;
import com.skitelDev.taskmanager.entities.Task;
import com.skitelDev.taskmanager.recycleViewHolders.RecyclerItemClickListener;
import com.skitelDev.taskmanager.recycleViewHolders.SimpleItemTouchHelperCallback;
import com.skitelDev.taskmanager.recycleViewHolders.TaskListAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RelativeLayout bottomsheet;
    Button addButton;
    BottomSheetBehavior bottomSheetBehavior;
    SQLiteDatabase db;
    private ItemTouchHelper mItemTouchHelper;
    ArrayList<Task> dataset;
    TaskListAdapter mAdapter;
    long id;
    String taskname;
    int pos;
    String desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        bottomsheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);
        bottomSheetBehavior.setHideable(false);
        bottomSheet();
        db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        API.createDatabase(db);
        if(getIntent().getExtras()!=null) {
            id = getIntent().getExtras().getLong("id");
            taskname = getIntent().getExtras().getString("name");
            desc = getIntent().getExtras().getString("desc");
        }

        dataset = API.getTaskFromList(db, 1);
        TaskListLoader(dataset);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, TaskDescriptionActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putLong("id", dataset.get(position).getId());
                        bundle.putString("name", dataset.get(position).getText());
                        bundle.putInt("pos", position);
                        bundle.putString("desc", dataset.get(position).getTaskDescription());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                })
        );

    }

    @Override
    protected void onResume() {
        if (getIntent().getExtras() != null) {
            db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
            id = getIntent().getExtras().getLong("id");
            taskname = getIntent().getExtras().getString("name");
            pos = getIntent().getExtras().getInt("pos");
            desc = getIntent().getExtras().getString("desc") ;
            TaskListAdapter.mDataset.set(pos, new Task(id, taskname, desc));
            mAdapter.notifyItemChanged(pos);
        }
        super.onResume();

    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof TextView) {
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
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
                EditText desc = findViewById(R.id.desc);
                if(!editText.getText().toString().trim().equals("")) {
                    TaskListAdapter.mDataset.add(new Task(API.findLastTaskID(db), editText.getText().toString(), desc.getText().toString()));
                    mAdapter.notifyItemInserted(TaskListAdapter.mDataset.size() - 1);
                    hideBottom();
                   desc.clearFocus();
                   editText.clearFocus();
                }
                else {
                    hideBottom();
                }
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
        TextView textView = findViewById(R.id.newTaskTextField);
        textView.setText("");
        addButton.setVisibility(View.VISIBLE);
        recyclerView.bringToFront();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        API.saveAll(db, 1, TaskListAdapter.mDataset);
    }
}
