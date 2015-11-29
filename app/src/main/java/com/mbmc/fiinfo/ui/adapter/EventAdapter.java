package com.mbmc.fiinfo.ui.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.data.EventInfo;
import com.mbmc.fiinfo.helper.Database;
import com.mbmc.fiinfo.util.DateUtil;


public class EventAdapter extends BaseCursorAdapter {

    @Override
    public RecyclerView.ViewHolder createCursorViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void bindCursorViewHolder(RecyclerView.ViewHolder holder, int position, Cursor cursor) {
        EventViewHolder viewHolder = (EventViewHolder) holder;

        String date = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_DATE));
        viewHolder.date.setText(DateUtil.getDate(Long.valueOf(date),
                cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_TIME_ZONE))));

        Event event = Event.get(cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_TYPE)));
        EventInfo eventInfo = EventInfo.get(event, cursor);
        viewHolder.type.setImageResource(eventInfo.iconId);
        viewHolder.info.setText(eventInfo.info);
    }


    private static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView date, info;
        ImageView type;

        public EventViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.event_date);
            type = (ImageView) itemView.findViewById(R.id.event_type);
            info = (TextView) itemView.findViewById(R.id.event_info);
        }
    }

}
