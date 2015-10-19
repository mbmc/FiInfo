package com.mbmc.fiinfo.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;


public abstract class BaseCursorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private CursorAdapter cursorAdapter;


    public abstract RecyclerView.ViewHolder createCursorViewHolder(ViewGroup parent, int viewType);
    public abstract void bindCursorViewHolder(RecyclerView.ViewHolder holder,
                                              int position, Cursor cursor);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createCursorViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        cursorAdapter.getCursor().moveToPosition(position);
        bindCursorViewHolder(holder, position, cursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        if (cursorAdapter == null) {
            return 0;
        }
        return cursorAdapter.getCount();
    }

    public void swapCursor(Context context, Cursor cursor) {
        if (cursorAdapter == null) {
            createAdapter(context, cursor);
        }
        cursorAdapter.swapCursor(cursor);
        notifyDataSetChanged();
    }

    private void createAdapter(Context context, Cursor cursor) {
        cursorAdapter = new CursorAdapter(context, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return null;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

            }
        };
    }

}
