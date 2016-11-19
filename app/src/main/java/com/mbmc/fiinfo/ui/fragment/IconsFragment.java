package com.mbmc.fiinfo.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.ui.component.IconLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class IconsFragment extends DialogFragment {

    private static final List<Event> EVENTS = Arrays.asList(
            Event.AIRPLANE_OFF, Event.AIRPLANE_ON,
            Event.SHUTDOWN, Event.BOOT,
            Event.MOBILE_OFF, Event.MOBILE, Event.MOBILE, Event.MOBILE, Event.MOBILE,
            Event.WIFI_OFF, Event.WIFI_ON, Event.WIFI, Event.WIFI_MOBILE, Event.WIFI_MOBILE,
            Event.WIFI_MOBILE, Event.WIFI_MOBILE
    );

    private static final int SPRINT = 6;
    private static final int T_MOBILE = 7;
    private static final int US_CELLULAR = 8;
    private static final int WIFI_SPRINT = 13;
    private static final int WIFI_T_MOBILE = 14;
    private static final int WIFI_US_CELLULAR = 15;

    @Bind(R.id.icons_container) LinearLayout container;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_icons, null);
        ButterKnife.bind(this, view);

        for (int i = 0; i < EVENTS.size(); ++i) {
            IconLayout iconLayout = (IconLayout) View.inflate(getActivity(), R.layout.layout_icon, null);
            switch (i) {
                case SPRINT:
                    iconLayout.setContent(R.drawable.ic_sprint, R.string.event_mobile_sprint);
                    break;
                case T_MOBILE:
                    iconLayout.setContent(R.drawable.ic_t_mobile, R.string.event_mobile_t_mobile);
                    break;
                case US_CELLULAR:
                    iconLayout.setContent(R.drawable.ic_us_cellular, R.string.event_mobile_us_cellular);
                    break;
                case WIFI_SPRINT:
                    iconLayout.setContent(R.drawable.ic_wifi_sprint, R.string.event_wifi_sprint);
                    break;
                case WIFI_T_MOBILE:
                    iconLayout.setContent(R.drawable.ic_wifi_t_mobile, R.string.event_wifi_t_mobile);
                    break;
                case WIFI_US_CELLULAR:
                    iconLayout.setContent(R.drawable.ic_wifi_us_cellular, R.string.event_wifi_us_cellular);
                    break;
                default: iconLayout.setContent(EVENTS.get(i)); break;
            }
            container.addView(iconLayout);
        }

        return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setView(view)
                .setTitle(R.string.icons_legend)
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
