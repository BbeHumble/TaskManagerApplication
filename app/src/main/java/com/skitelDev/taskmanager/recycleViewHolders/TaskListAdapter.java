package com.skitelDev.taskmanager.recycleViewHolders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skitelDev.taskmanager.R;
import com.skitelDev.taskmanager.entities.Task;

import java.util.ArrayList;
import java.util.Collections;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.taskname.setText(mDataset.get(position).getText());
        holder.taskDescription.setText(mDataset.get(position).getTaskDescription());
    }

    @Override
    public void onItemDismiss(int position) {//удаление
            mDataset.remove(position);
            notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mDataset, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mDataset, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder{
        public TextView taskname;
        public TextView taskDescription;
        public MyViewHolder(View v) {
            super(v);
            taskname = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(0);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

