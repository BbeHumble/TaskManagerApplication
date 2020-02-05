package com.skitelDev.taskmanager.recycleViewHolders;

import android.util.Log;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.skitelDev.taskmanager.recycleViewHolders.ItemTouchHelperAdapter;
import com.skitelDev.taskmanager.recycleViewHolders.ItemTouchHelperViewHolder;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemSelected();
        }

        super.onSelectedChanged(viewHolder, actionState);
    }
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            ItemTouchHelperViewHolder itemViewHolder =
                    (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }

    }
}