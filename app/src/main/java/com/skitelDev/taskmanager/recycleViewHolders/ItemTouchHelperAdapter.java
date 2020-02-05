package com.skitelDev.taskmanager.recycleViewHolders;

interface ItemTouchHelperAdapter {
    void onItemDismiss(int position);
    boolean onItemMove(int fromPosition, int toPosition);
}
