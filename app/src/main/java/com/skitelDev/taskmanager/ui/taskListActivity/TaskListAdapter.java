package com.skitelDev.taskmanager.ui.taskListActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skitelDev.taskmanager.R;
import com.skitelDev.taskmanager.data.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    public static List<Task> mDataset;
    private LayoutInflater mInflater;

    public TaskListAdapter(Context context, List<Task> tasks) {
        mInflater = LayoutInflater.from(context);
        mDataset = tasks;
    }

    @Override
    public TaskListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        View view = mInflater.inflate(R.layout.tasklist_recyclerview_item, parent, false);
        return new TaskListAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.taskname.setText(mDataset.get(position).getText());
        holder.taskDescription.setText(mDataset.get(position).getTaskDescription());
        Random rnd = new Random();
        int paint = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        StateListDrawable shapeDrawable = (StateListDrawable) holder.colorBubble.getBackground();
        shapeDrawable.setColorFilter(paint, PorterDuff.Mode.MULTIPLY);
/*
        if(mDataset.get(position).getSubtasks()!=null) {
            if (mDataset.get(position).getSubtasks().size() != 0) {
                holder.imageView.setVisibility(View.VISIBLE);
            } else {
                holder.imageView.setVisibility(View.INVISIBLE);
            }
        }
        else {
            holder.imageView.setVisibility(View.INVISIBLE);
        }
*/
    }
    public void addData(List<Task> mDataset1) {
        mDataset.clear();
        mDataset.addAll(mDataset1);
        notifyDataSetChanged();
    }
    @Override
    public void onItemDismiss(int position) {//удаление
        MainActivity.presenter.deleteTask(TaskListAdapter.mDataset.get(position));
        notifyItemRemoved(position);
    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mDataset, i, i + 1);
                long id1 = mDataset.get(i).getId();
                long id2 = mDataset.get(i + 1).getId();
                mDataset.get(i+1).setId(id1);
                mDataset.get(i).setId(id2);

            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mDataset, i, i - 1);
                long id1 = mDataset.get(i).getId();
                long id2 = mDataset.get(i - 1).getId();
                mDataset.get(i-1).setId(id1);
                mDataset.get(i).setId(id2);
            }
        }
        SimpleItemTouchHelperCallback.from = fromPosition;
        SimpleItemTouchHelperCallback.to = toPosition;
        notifyItemMoved(fromPosition, toPosition);
        MainActivity.presenter.moveTask(fromPosition, toPosition);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        TextView taskname;
        TextView taskDescription;
        Button colorBubble;
        Button imageView;

        MyViewHolder(View v) {
            super(v);
            taskname = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            colorBubble = itemView.findViewById(R.id.color);
//            imageView = itemView.findViewById(R.id.hasSubTasks);
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

