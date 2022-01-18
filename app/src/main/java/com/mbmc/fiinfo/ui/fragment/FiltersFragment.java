package com.mbmc.fiinfo.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.data.Filter;
import com.mbmc.fiinfo.ui.activity.MainActivity;

public class FiltersFragment extends DialogFragment {

    private static final int SIZE = Filter.values().length;
    private static final CharSequence[] TITLES = new CharSequence[SIZE];

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        for (int i = 0 ; i < SIZE; ++i) {
            TITLES[i] = getString(Filter.values()[i].stringId);
        }

        return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setTitle(R.string.filter)
                .setItems(TITLES, (DialogInterface dialogInterface, int which) ->
                        ((MainActivity) getActivity()).applyFilter(Filter.values()[which]))
                .create();
    }
}
