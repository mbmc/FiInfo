package com.mbmc.fiinfo.ui.component;

import android.content.Context;
import android.util.AttributeSet;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.event.RefreshListener;
import com.mbmc.fiinfo.event.ScrollListener;

public class RefreshLayout extends SwipeRefreshLayout
        implements ScrollListener {

    private RefreshListener refreshListener;

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnRefreshListener(() -> refreshListener.onRefresh());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ((RecyclerView) findViewById(R.id.recycler_view)).setScrollListener(this);
    }

    @Override
    public void scrollToTop(boolean top) {
        setEnabled(top);
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void stopAnimation() {
        setRefreshing(false);
        clearAnimation();
    }
}
