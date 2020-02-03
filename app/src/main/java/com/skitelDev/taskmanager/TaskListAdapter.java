package com.skitelDev.taskmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skitelDev.taskmanager.API.API;
import com.skitelDev.taskmanager.entities.Task;

import java.util.ArrayList;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder>  {
    public static ArrayList<Task> mDataset;
    private LayoutInflater mInflater;
    public TaskListAdapter(Context context, ArrayList<Task> tasks) {
        mInflater = LayoutInflater.from(context);
        mDataset = tasks;
    }

    @Override
    public TaskListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = mInflater.inflate(R.layout.tasklist_recyclerview_item, parent, false);
        return new TaskListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(mDataset.get(position).getText());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public EditText textView;

        public MyViewHolder(View v) {
            super(v);
            textView = itemView.findViewById(R.id.taskTitle);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}

