package com.yeuristic.recyclerviewpagerlib;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class LoopRecyclerViewPagerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public abstract int getRealItemCount();

    public int getItemViewTypeLoop(int position) {
        return 0;
    }

    public int getItemIdLoop(int position) {
        return -1;
    }

    public abstract void onBindViewHolderLoop(@NonNull VH holder, int position);

    public void onBindViewHolderLoop(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        onBindViewHolderLoop(holder, position);
    }

    /**
     * Use {@link #getRealItemCount()} instead
     */
    @Deprecated
    @Override
    public final int getItemCount() {
        if (getRealItemCount() <= 0) {
            return 0;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        onBindViewHolderLoop(holder, getRealPosition(position));
    }

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        onBindViewHolderLoop(holder, getRealPosition(position), payloads);
    }

    @Override
    public final int getItemViewType(int position) {
        return getItemViewTypeLoop(getRealPosition(position));
    }

    @Override
    public final long getItemId(int position) {
        return getItemIdLoop(getRealPosition(position));
    }

    public int getRealPosition(int position) {
        int temp = (position - getOffset()) % getRealItemCount();
        return temp < 0 ? temp + getRealItemCount() : temp;
    }

    public int getOffset() {
        int mid = Integer.MAX_VALUE / 2;
        int itemCountMid = getRealItemCount() / 2;
        return mid - itemCountMid;
    }
}
