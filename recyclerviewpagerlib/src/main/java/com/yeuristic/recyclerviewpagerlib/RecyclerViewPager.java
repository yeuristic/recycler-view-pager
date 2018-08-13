package com.yeuristic.recyclerviewpagerlib;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewPager extends RecyclerView {

    private List<OnPageSelected> mOnPageSelectedList = new ArrayList<>();

    public RecyclerViewPager(Context context) {
        this(context, null);
    }

    public RecyclerViewPager(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewPager(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addMandatoryScrollListener();
    }

    @Override
    public void clearOnScrollListeners() {
        super.clearOnScrollListeners();
        addMandatoryScrollListener();
    }

    private void addMandatoryScrollListener() {
        SnapHelper snapHelper = new PagerSnapHelper() {
            @Override
            public int findTargetSnapPosition(LayoutManager layoutManager, int velocityX, int velocityY) {
                int targetSnapPosition = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
                int realTargetPosition;
                if (isLooping()) {
                    LoopRecyclerViewPagerAdapter adapter = (LoopRecyclerViewPagerAdapter) getAdapter();
                    realTargetPosition = adapter.getRealPosition(targetSnapPosition);
                } else {
                    realTargetPosition = targetSnapPosition;
                }
                if (mOnPageSelectedList != null) {
                    for (OnPageSelected onPageSelected : mOnPageSelectedList) {
                        onPageSelected.onPageSelected(realTargetPosition);
                    }
                }
                return targetSnapPosition;
            }
        };
        snapHelper.attachToRecyclerView(this);

        mOnPageSelectedList.add(new OnPageSelected() {
            @Override
            public void onPageSelected(int selectedPage) {
                if (isLooping()) {
                    LoopRecyclerViewPagerAdapter adapter = (LoopRecyclerViewPagerAdapter) getAdapter();
                    int currentPosition = getCurrentPosition();
                    if (currentPosition == 0 || currentPosition == adapter.getItemCount() - 1) {
                        int realCurrentPosition = adapter.getRealPosition(getCurrentPosition());
                        int positionTarget = adapter.getOffset() + realCurrentPosition;
                        scrollToPosition(positionTarget);
                    }
                }
            }
        });
    }

    public void addOnPageSelected(OnPageSelected onPageSelected) {
        mOnPageSelectedList.add(onPageSelected);
    }

    public boolean removeOnPageSelected(OnPageSelected onPageSelected) {
        return mOnPageSelectedList.remove(onPageSelected);
    }

    @Override
    public void smoothScrollToPosition(int position) {
        if (isLooping()) {
            LoopRecyclerViewPagerAdapter adapter = (LoopRecyclerViewPagerAdapter) getAdapter();
            if (getCurrentPosition() < 0) {
                super.smoothScrollToPosition(adapter.getOffset() + position);
            } else {
                int realCurrentPosition = adapter.getRealPosition(getCurrentPosition());
                int positionTarget = getCurrentPosition() + position - realCurrentPosition;
                super.smoothScrollToPosition(positionTarget);
            }
        } else {
            super.smoothScrollToPosition(position);
        }
    }

    public boolean isLooping() {
        return getAdapter() instanceof LoopRecyclerViewPagerAdapter;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof LoopRecyclerViewPagerAdapter) {
            LoopRecyclerViewPagerAdapter loopRecyclerViewPagerAdapter = (LoopRecyclerViewPagerAdapter) adapter;
            scrollToPosition(loopRecyclerViewPagerAdapter.getOffset());
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof LinearLayoutManager) {
            super.setLayoutManager(layout);
        } else {
            throw new IllegalArgumentException("only LinearLayoutManager supported");
        }
    }

    public int getCurrentPosition() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        return layoutManager.findFirstCompletelyVisibleItemPosition();
    }
}
