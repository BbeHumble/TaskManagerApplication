
package com.skitelDev.taskmanager.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skitelDev.taskmanager.API.API;
import com.skitelDev.taskmanager.R;
import com.skitelDev.taskmanager.bottomDialogFragment.BottomDialogFragment;
import com.skitelDev.taskmanager.entities.Task;
import com.skitelDev.taskmanager.recycleViewHolders.RecyclerItemClickListener;
import com.skitelDev.taskmanager.recycleViewHolders.SimpleItemTouchHelperCallback;
import com.skitelDev.taskmanager.recycleViewHolders.TaskListAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomDialogFragment.ItemClickListener {
    RecyclerView recyclerView;
    Button addButton;
    SQLiteDatabase db;
    ArrayList<Task> dataset;
    TaskListAdapter mAdapter;
    long id;
    String taskname;
    int pos;
    String desc;
    ArrayList<String> subTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        bottomSheet();
        db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        API.db = db;
        API.createDatabase();
        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getLong("id");
            taskname = getIntent().getExtras().getString("name");
            desc = getIntent().getExtras().getString("desc");

        }

        dataset = API.getTaskFromList(1);
        for (int i = 0; i < dataset.size(); i++) {
            dataset.get(i).setSubtasks(API.getSubTasksByTaskId(dataset.get(i).getId()));
        }
        TaskListLoader(dataset);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, (view, position) -> {
                    Intent intent = new Intent(MainActivity.this, TaskDescriptionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("id", dataset.get(position).getId());
                    bundle.putString("name", dataset.get(position).getText());
                    bundle.putInt("pos", position);
                    bundle.putString("desc", dataset.get(position).getTaskDescription());
                    bundle.putStringArrayList("subtasks", API.getSubTasksByTaskId(dataset.get(position).getId()));
//                    API.saveAll(1, TaskListAdapter.mDataset);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                })
        );

    }

    @Override
    protected void onResume() {
        if (getIntent().getExtras() != null) {
            db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
            id = getIntent().getExtras().getLong("id");
            if (id != -1) {
                taskname = getIntent().getExtras().getString("name");
                pos = getIntent().getExtras().getInt("pos");
                desc = getIntent().getExtras().getString("desc");
                subTasks = getIntent().getExtras().getStringArrayList("subtasks");
                if (TaskListAdapter.mDataset.size() != 0) {
                    TaskListAdapter.mDataset.set(pos, new Task(id, taskname, desc));
                } else {
                    TaskListAdapter.mDataset.add(new Task(id, taskname, desc));
                }
                TaskListAdapter.mDataset.get(pos).setSubtasks(subTasks);
                API.saveAll(1, TaskListAdapter.mDataset);
                mAdapter.notifyItemChanged(pos);
            } else {
                pos = getIntent().getExtras().getInt("position");
                TaskListAdapter.mDataset.remove(pos);
                mAdapter.notifyItemRemoved(pos);
                mAdapter.notifyItemRangeChanged(pos, TaskListAdapter.mDataset.size());
                Toast toast = Toast.makeText(getApplicationContext(), "Task deleted", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        super.onResume();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void bottomSheet() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        recyclerView = findViewById(R.id.recycler_view);
        addButton = findViewById(R.id.addbutton);
        final RelativeLayout relativeLayout = findViewById(R.id.frame);

        addButton.setOnClickListener(view -> {
            relativeLayout.bringToFront();
            BottomDialogFragment addPhotoBottomDialogFragment =
                    BottomDialogFragment.newInstance();
            addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                    "dialog_fragment");
        });


        relativeLayout.setOnClickListener(view -> hideBottom());


    }

    public void TaskListLoader(ArrayList<Task> list) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager1);
        mAdapter = new TaskListAdapter(getApplicationContext(), list);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
    }

    private void hideBottom() {
        addButton.setVisibility(View.VISIBLE);
        recyclerView.bringToFront();
    }


    @Override
    protected void onPause() {
        API.saveAll(1, TaskListAdapter.mDataset);
        super.onPause();
    }

    @Override
    public void onItemClick(String newTaskText, String desc) {
        if (!newTaskText.trim().equals("")) {
            TaskListAdapter.mDataset.add(new Task(API.findLastTaskID(), newTaskText, desc));
            mAdapter.notifyItemInserted(TaskListAdapter.mDataset.size() - 1);
        }
        hideBottom();
    }

}
