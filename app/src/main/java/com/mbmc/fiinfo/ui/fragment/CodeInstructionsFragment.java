package com.mbmc.fiinfo.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.constant.Preferences;
import com.mbmc.fiinfo.helper.CodeManager;
import com.mbmc.fiinfo.helper.PreferencesManager;


public class CodeInstructionsFragment extends DialogFragment {

    private int code;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.view_code_instructions, null);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.code_instructions_check_box);

        return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        if (checkBox.isChecked()) {
                            PreferencesManager.getInstance(getActivity())
                                    .setBoolean(Preferences.HIDE_CODE_INSTRUCTIONS, true);
                        }
                        CodeManager.openDialer(getActivity(), code);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
    }

    public void setCode(int code) {
        this.code = code;
    }

}
