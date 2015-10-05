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
import com.mbmc.fiinfo.util.DateUtil;


public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private CursorAdapter cursorAdapter;


    @Override
    public int getItemCount() {
        if (cursorAdapter == null) {
            return 0;
        }
        return cursorAdapter.getCount();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        cursorAdapter.getCursor().moveToPosition(position);
        EventViewHolder viewHolder = (EventViewHolder) holder;
        Context context = viewHolder.itemView.getContext();
        Cursor cursor = cursorAdapter.getCursor();

        viewHolder.date.setText(DateUtil.getDate(cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_TIME_ZONE))));

        Event event = Event.get(cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_TYPE)));
        String type = context.getString(event.stringId);
        switch (event) {
            case MOBILE:
                type = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_SPEED));
                break;
        }
        viewHolder.name.setText(cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_NAME)));
        viewHolder.type.setText(type);
        cursorAdapter.bindView(holder.itemView, holder.itemView.getContext(), cursorAdapter.getCursor());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = cursorAdapter.newView(parent.getContext(), cursorAdapter.getCursor(), parent);
        return new EventViewHolder(view);
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
                return LayoutInflater.from(context).inflate(R.layout.layout_event, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

            }
        };
    }

    private static class EventViewHolder extends RecyclerView.ViewHolder{
        TextView date, name, speed, type;

        public EventViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.event_date);
            name = (TextView) itemView.findViewById(R.id.event_name);
            type = (TextView) itemView.findViewById(R.id.event_type);
        }
    }

}
