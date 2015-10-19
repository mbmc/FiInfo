package com.mbmc.fiinfo.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.data.Event;


public class IconLayout extends LinearLayout {

    public IconLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setContent(Event event) {
        setContent(event.iconId, event.labelId);
    }

    public void setContent(int iconId, int labelId) {
        ((ImageView) findViewById(R.id.icon_icon)).setImageResource(iconId);
        ((TextView) findViewById(R.id.icon_label)).setText(labelId);
    }

}
