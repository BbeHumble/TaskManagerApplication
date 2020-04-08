


package com.skitelDev.taskmanager.ui.taskListActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import com.skitelDev.taskmanager.R;
import com.skitelDev.taskmanager.data.Storage;
import com.skitelDev.taskmanager.data.database.SubTaskDao;
import com.skitelDev.taskmanager.data.database.TaskDao;
import com.skitelDev.taskmanager.data.database.TaskManagerDatabase;
import com.skitelDev.taskmanager.data.model.SubTask;
import com.skitelDev.taskmanager.data.model.Task;
import com.skitelDev.taskmanager.ui.taskDescriptionActivity.TaskDescriptionActivity;
import com.skitelDev.taskmanager.bottomDialogFragment.BottomDialogFragment;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomDialogFragment.ItemClickListener, TaskListView {
    RecyclerView recyclerView;
    Button addButton;
    List<Task> dataset;
    static TaskListAdapter mAdapter;
    int pos;
    BottomDialogFragment bottomDialogFragment;
    static TaskListPresenter presenter;
    private Storage storage;
    //https://stackoverflow.com/questions/55949538/update-onmove-changes-in-recycler-view-data-to-room-database

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        addButton = findViewById(R.id.addbutton);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        bottomDialogFragment = initBottomFragment();
        storage = provideStorage();


        presenter = new TaskListPresenter(this, storage);
        presenter.getAllTasks();
        if (getIntent().getExtras() != null) {
            Task editedTask = new Task();
            editedTask.setId(getIntent().getExtras().getLong("id"));
            if (editedTask.getId() != -1) {
                editedTask.setText(getIntent().getExtras().getString("name"));
                pos = getIntent().getExtras().getInt("pos");
                editedTask.setTaskDescription(getIntent().getExtras().getString("desc"));
                overwriteTask(editedTask);
            } else {
                pos = getIntent().getExtras().getInt("position");
                presenter.deleteTask(dataset.get(pos));
                mAdapter.notifyItemRemoved(pos);
                Toast toast = Toast.makeText(getApplicationContext(), "Task deleted", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, (view, position) -> {
                    presenter.showTaskDescription(position);
                })
        );


        addButton.setOnClickListener(view -> {
            presenter.showAddTask();
        });
    }

    private void overwriteTask(Task editedTask) {
        dataset.set(pos, editedTask);
        dataset.get(pos).setSubTaskList(presenter.provideSubTasks(dataset.get(pos).getId()));
        presenter.updateTask(dataset.get(pos));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(String newTaskText, String desc) {
        if (!newTaskText.trim().equals("")) {
            int index = storage.getTasks().size() + 1;
            Task task = new Task(index, newTaskText, desc);
            task.setSubTaskList(new ArrayList<>());
            TaskListAdapter.mDataset.add(task);
            mAdapter.notifyItemInserted(TaskListAdapter.mDataset.size());
            storage.insertTask(task);
        }
        presenter.hideAddTask();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void showTasks(List<Task> projects) {
        dataset = projects;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager1);
        mAdapter = new TaskListAdapter(getApplicationContext(), projects);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
    }

    BottomDialogFragment initBottomFragment() {
        return BottomDialogFragment.newInstance();
    }

    @Override
    public void showAddTask() {
        final RelativeLayout relativeLayout = findViewById(R.id.frame);
        relativeLayout.bringToFront();
        bottomDialogFragment.show(getSupportFragmentManager(),
                "dialog_fragment");

    }

    @Override
    public void hideAddTask() {
        final RelativeLayout relativeLayout = findViewById(R.id.frame);
        relativeLayout.setOnClickListener(view -> {
            addButton.setVisibility(View.VISIBLE);
            recyclerView.bringToFront();
        });
    }

    @Override
    public void deleteTask(Task task) {
        TaskListAdapter.mDataset.remove(task);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void moveTask(int from, int to) {

    }

    @Override
    public void showTaskDescription(int position) {
        Intent intent = new Intent(MainActivity.this, TaskDescriptionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id", TaskListAdapter.mDataset.get(position).getId());
        bundle.putString("name", TaskListAdapter.mDataset.get(position).getText());
        bundle.putInt("pos", position);
        bundle.putString("desc", TaskListAdapter.mDataset.get(position).getTaskDescription());
        ArrayList<String> subtasksStrings = new ArrayList<>();
        ArrayList<SubTask> subTasks = (ArrayList<SubTask>) TaskListAdapter.mDataset.get(position).getSubTaskList();

        long[] subtasksIds = new long[subTasks.size()];
        for (int i = 0; i < subTasks.size(); i++) {
            subtasksStrings.add(subTasks.get(i).getSubTaskText());
            subtasksIds[i] = subTasks.get(i).id;
        }
        bundle.putLongArray("subtasks_ids", subtasksIds);
        bundle.putStringArrayList("subtasks", subtasksStrings);

        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    Storage provideStorage() {
        final TaskManagerDatabase database = Room.databaseBuilder(this, TaskManagerDatabase.class, "TaskManagerDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        return new Storage(database.getTaskDao(), database.getSubTaskDao());
    }
}

