package com.mbmc.fiinfo.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.data.MobileEvent;
import com.mbmc.fiinfo.data.WiFiMobileEvent;
import com.mbmc.fiinfo.ui.component.IconLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class IconsFragment extends DialogFragment {

    private static final List<Event> EVENTS = Arrays.asList(
            Event.AIRPLANE_OFF, Event.AIRPLANE_ON, Event.SHUTDOWN, Event.BOOT, Event.MOBILE_OFF,
            Event.MOBILE, Event.WIFI_OFF, Event.WIFI_ON, Event.WIFI, Event.WIFI_MOBILE
    );

    @BindView(R.id.icons_container) LinearLayout container;

    private Unbinder unbinder;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_icons, null);
        unbinder = ButterKnife.bind(this, view);

        for (Event event : EVENTS) {
            addItem(event.iconId, event.labelId);
            if (event == Event.MOBILE) {
                for (MobileEvent mobileEvent : MobileEvent.values()) {
                    addItem(mobileEvent.iconId, mobileEvent.labelId);
                }
            } else if (event == Event.WIFI_MOBILE) {
                for (WiFiMobileEvent wiFiMobileEvent : WiFiMobileEvent.values()) {
                    addItem(wiFiMobileEvent.iconId, wiFiMobileEvent.labelId);
                }
            }
        }

        return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setView(view)
                .setTitle(R.string.icons_legend)
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void addItem(int iconId, int labelId) {
        IconLayout iconLayout = (IconLayout) View.inflate(getActivity(),
                R.layout.layout_icon, null);
        iconLayout.setContent(iconId, labelId);
        container.addView(iconLayout);
    }

}
