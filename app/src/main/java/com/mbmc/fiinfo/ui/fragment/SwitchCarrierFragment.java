package com.mbmc.fiinfo.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.data.Code;
import com.mbmc.fiinfo.helper.CodeManager;

import java.util.Arrays;
import java.util.List;


public class SwitchCarrierFragment extends DialogFragment {

    private static final List<Code> CODES = Arrays.asList(Code.AUTO, Code.REPAIR, Code.NEXT, Code.SPRINT, Code.T_MOBILE);
    private static final int SIZE = CODES.size();
    private static final CharSequence[] TITLES = new CharSequence[SIZE];


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        for (int i = 0; i < SIZE; ++i) {
            TITLES[i] = getString(CODES.get(i).labelId);
        }

        return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setItems(TITLES, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        CodeManager.send(getActivity(), CODES.get(which).code);
                    }
                })
                .setTitle(R.string.menu_carrier)
                .create();
    }

}
