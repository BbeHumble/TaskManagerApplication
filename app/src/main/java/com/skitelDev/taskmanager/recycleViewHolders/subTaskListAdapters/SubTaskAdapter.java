package com.skitelDev.taskmanager.recycleViewHolders.subTaskListAdapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skitelDev.taskmanager.R;

import java.util.ArrayList;

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskAdapter.MyViewHolder>{
    public static ArrayList<String> mDataset;
    private LayoutInflater mInflater;

    public SubTaskAdapter(Context context, ArrayList<String> subtasks) {
        mInflater = LayoutInflater.from(context);
        mDataset = subtasks;
    }
    @Override
    public SubTaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        View view = mInflater.inflate(R.layout.sub_task_recyclerview_item, parent, false);
        return new SubTaskAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String s = mDataset.get(position);
        holder.sub_task_text.setText(s);
        holder.delete_subtask.setOnClickListener(view -> {
            removeAt(position);
        });
        holder.sub_task_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mDataset.set(position,editable.toString());
            }
        });

    }
    private void removeAt(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataset.size());
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        EditText sub_task_text;
        Button delete_subtask;
        MyViewHolder(View v) {
            super(v);
            sub_task_text = itemView.findViewById(R.id.sub_task_text);
            delete_subtask = itemView.findViewById(R.id.delete_subtask);
        }


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}