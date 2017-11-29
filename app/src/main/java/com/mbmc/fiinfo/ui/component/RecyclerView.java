package com.mbmc.fiinfo.ui.component;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import com.mbmc.fiinfo.event.ScrollListener;


public class RecyclerView extends android.support.v7.widget.RecyclerView {

    private ScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;


    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayoutManager(linearLayoutManager = new LinearLayoutManager(context));

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(android.support.v7.widget.RecyclerView recyclerView, int dx, int dy) {
                scrollListener.scrollToTop(linearLayoutManager
                        .findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

}
