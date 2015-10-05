package com.mbmc.fiinfo.ui.component;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.event.RefreshListener;
import com.mbmc.fiinfo.event.ScrollListener;


public class RefreshLayout extends SwipeRefreshLayout
    implements ScrollListener {

    private RefreshListener refreshListener;


    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListener.onRefresh();
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ((RecyclerView) findViewById(R.id.main_recycler_view)).setScrollListener(this);
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
