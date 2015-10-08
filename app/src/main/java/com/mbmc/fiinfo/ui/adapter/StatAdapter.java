package com.mbmc.fiinfo.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.helper.Database;


public class StatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final class ViewType {
        private static final int HEADER = 1;
        private static final int STAT = 2;
    }

    private CursorAdapter cursorAdapter;


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ViewType.HEADER;
        }
        return ViewType.STAT;
    }

    @Override
    public int getItemCount() {
        if (cursorAdapter == null) {
            return 0;
        }
        return cursorAdapter.getCount() + 1; // Header
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_stat_header, parent, false);
            return new HeaderViewHolder(view);
        }

        View view = cursorAdapter.newView(parent.getContext(), cursorAdapter.getCursor(), parent);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ViewType.HEADER) {
            return;
        }

        cursorAdapter.getCursor().moveToPosition(position - 1); // Header
        EventViewHolder viewHolder = (EventViewHolder) holder;
        Context context = viewHolder.itemView.getContext();
        Cursor cursor = cursorAdapter.getCursor();

        viewHolder.count.setText(cursor.getString(cursor.getColumnIndexOrThrow("count")));

        Event event = Event.get(cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_TYPE)));
        String type = context.getString(event.stringId);
        switch (event) {
            case MOBILE:
                type = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_SPEED));
                break;
        }
        viewHolder.type.setText(type);
        viewHolder.name.setText(cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_NAME)));
        cursorAdapter.bindView(holder.itemView, holder.itemView.getContext(), cursorAdapter.getCursor());
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
                return LayoutInflater.from(context).inflate(R.layout.view_stat, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

            }
        };
    }

    private static class EventViewHolder extends RecyclerView.ViewHolder{
        TextView count, type, name;

        public EventViewHolder(View itemView) {
            super(itemView);
            count = (TextView) itemView.findViewById(R.id.stat_count);
            type = (TextView) itemView.findViewById(R.id.stat_type);
            name = (TextView) itemView.findViewById(R.id.stat_name);
        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder{
        TextView count, type, name;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            count = (TextView) itemView.findViewById(R.id.stat_header_count);
            type = (TextView) itemView.findViewById(R.id.stat_header_type);
            name = (TextView) itemView.findViewById(R.id.stat_header_name);
        }
    }

}