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


public class StatAdapter extends BaseCursorAdapter {

    @Override
    public RecyclerView.ViewHolder createCursorViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_stat, parent, false);
        return new StatViewHolder(view);
    }

    @Override
    public void bindCursorViewHolder(RecyclerView.ViewHolder holder, int position, Cursor cursor) {
        StatViewHolder viewHolder = (StatViewHolder) holder;

        viewHolder.count.setText(cursor.getString(cursor.getColumnIndexOrThrow("count")));

        Event event = Event.get(cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_TYPE)));
        EventInfo eventInfo = EventInfo.get(viewHolder.itemView.getContext(), event, cursor);
        viewHolder.type.setImageResource(eventInfo.iconId);
        viewHolder.info.setText(eventInfo.info);
    }


    private static class StatViewHolder extends RecyclerView.ViewHolder{
        TextView count, info;
        ImageView type;

        public StatViewHolder(View itemView) {
            super(itemView);
            count = (TextView) itemView.findViewById(R.id.stat_count);
            type = (ImageView) itemView.findViewById(R.id.stat_type);
            info = (TextView) itemView.findViewById(R.id.stat_info);
        }
    }

}
