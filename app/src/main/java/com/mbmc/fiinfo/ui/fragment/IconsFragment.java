package com.mbmc.fiinfo.ui.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.ui.component.IconLayout;

import java.util.Arrays;
import java.util.List;


public class IconsFragment extends DialogFragment {

    private static final List<Event> events = Arrays.asList(
            Event.AIRPLANE_OFF, Event.AIRPLANE_ON,
            Event.SHUTDOWN, Event.BOOT,
            Event.MOBILE_OFF, Event.MOBILE, Event.MOBILE, Event.MOBILE,
            Event.WIFI_OFF, Event.WIFI_ON, Event.WIFI, Event.WIFI_MOBILE, Event.WIFI_MOBILE, Event.WIFI_MOBILE
    );

    private static final int SPRINT = 6;
    private static final int T_MOBILE = 7;
    private static final int WIFI_SPRINT = 12;
    private static final int WIFI_T_MOBILE = 13;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.icons_legend);
        View view = inflater.inflate(R.layout.fragment_icons, container, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.icons_container);
        for (int i = 0; i < events.size(); ++i) {
            IconLayout iconLayout = (IconLayout) inflater.inflate(R.layout.layout_icon, linearLayout, false);
            switch (i) {
                case SPRINT:
                    iconLayout.setContent(R.drawable.ic_sprint, R.string.event_mobile_sprint);
                    break;
                case T_MOBILE:
                    iconLayout.setContent(R.drawable.ic_t_mobile, R.string.event_mobile_t_mobile);
                    break;
                case WIFI_SPRINT:
                    iconLayout.setContent(R.drawable.ic_wifi_sprint, R.string.event_wifi_sprint);
                    break;
                case WIFI_T_MOBILE:
                    iconLayout.setContent(R.drawable.ic_wifi_t_mobile, R.string.event_wifi_t_mobile);
                    break;
                default: iconLayout.setContent(events.get(i)); break;
            }
            linearLayout.addView(iconLayout);
        }
        return view;
    }

}
