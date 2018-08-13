package com.yeuristic.recyclerviewpagertest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yeuristic.recyclerviewpagerlib.LoopRecyclerViewPagerAdapter;

public class TestAdapter extends LoopRecyclerViewPagerAdapter<TestAdapter.TestViewHolder> {

    private final String[] testColor = new String[] {
            "#000000",
            "#0000ff",
            "#00ff00",
            "#00ffff",
            "#ff0000",
            "#ff00ff",
            "#ffff00"
    };
    private final ItemClickListener mTopClickAction;
    private final ItemClickListener mBottomClickAction;

    public TestAdapter(ItemClickListener topClickAction, ItemClickListener bottomClickAction) {
        mTopClickAction = topClickAction;
        mBottomClickAction = bottomClickAction;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View layout = LayoutInflater.from(context).inflate(R.layout.item_test, parent, false);
        return new TestViewHolder(layout);
    }

    @Override
    public void onBindViewHolderLoop(@NonNull TestViewHolder holder, int position) {
        holder.bottomView.setImageDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        holder.topView.setImageDrawable(new ColorDrawable(Color.parseColor(testColor[position])));
        holder.topView.setOnClickListener(v -> {
            mTopClickAction.onClick(position);
        });
        holder.bottomView.setOnClickListener(v -> {
            mBottomClickAction.onClick(position);
        });
    }

    @Override
    public int getRealItemCount() {
        return testColor.length;
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {

        private final ImageView topView;
        private final ImageView bottomView;
        public TestViewHolder(View itemView) {
            super(itemView);
            topView = itemView.findViewById(R.id.image_view_top);
            bottomView = itemView.findViewById(R.id.image_view_bottom);
        }
    }
}
