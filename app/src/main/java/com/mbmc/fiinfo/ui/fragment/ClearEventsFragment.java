package com.mbmc.fiinfo.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.provider.EventProvider;


public class ClearEventsFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setTitle(R.string.clear)
                .setPositiveButton(R.string.ok, (DialogInterface dialogInterface, int which)
                        -> getActivity().getContentResolver().delete(EventProvider.URI, null, null))
                .setNegativeButton(R.string.cancel, (DialogInterface dialogInterface, int which)
                        -> dialogInterface.dismiss())
                .create();
    }

}
