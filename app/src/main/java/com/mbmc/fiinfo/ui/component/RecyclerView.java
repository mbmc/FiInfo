package com.mbmc.fiinfo.ui.component;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mbmc.fiinfo.event.ScrollListener;

public class RecyclerView extends androidx.recyclerview.widget.RecyclerView {

    private ScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;

    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayoutManager(linearLayoutManager = new LinearLayoutManager(context));

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(androidx.recyclerview.widget.RecyclerView recyclerView, int dx, int dy) {
                scrollListener.scrollToTop(linearLayoutManager
                        .findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }
}
