package com.mbmc.fiinfo.ui.fragment;

import android.app.FragmentManager;
import android.content.DialogInterface;

public class DialogFragment extends android.app.DialogFragment {

    private boolean shown;

    @Override
    public void show(FragmentManager manager, String tag) {
        if (shown) {
            return;
        }
        shown = true;
        super.show(manager, tag);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        shown = false;
    }
}
