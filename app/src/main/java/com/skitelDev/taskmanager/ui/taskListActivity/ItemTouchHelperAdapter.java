package com.skitelDev.taskmanager.ui.taskListActivity;

interface ItemTouchHelperAdapter {
    void onItemDismiss(int position);
    void onItemMove(int fromPosition, int toPosition);
}
