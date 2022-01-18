package com.mbmc.fiinfo.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.data.Code;
import com.mbmc.fiinfo.helper.CodeManager;

import java.util.Arrays;
import java.util.List;

public class SwitchCarrierFragment extends DialogFragment {

    private static final List<Code> CODES = Arrays.asList(Code.AUTO, Code.REPAIR,
            Code.NEXT, Code.SPRINT, Code.T_MOBILE, Code.THREE_UK, Code.US_CELLULAR);
    private static final int SIZE = CODES.size();
    private static final CharSequence[] TITLES = new CharSequence[SIZE];

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        for (int i = 0; i < SIZE; ++i) {
            TITLES[i] = getString(CODES.get(i).labelId);
        }

        return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setItems(TITLES, (DialogInterface dialogInterface, int which) ->
                        CodeManager.send(getActivity(), CODES.get(which).code))
                .setTitle(R.string.menu_carrier)
                .create();
    }
}
