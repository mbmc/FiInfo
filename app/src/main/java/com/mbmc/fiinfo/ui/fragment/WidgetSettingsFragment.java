package com.mbmc.fiinfo.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.constant.Constants;
import com.mbmc.fiinfo.constant.Preferences;
import com.mbmc.fiinfo.data.Code;
import com.mbmc.fiinfo.helper.PreferencesManager;
import com.mbmc.fiinfo.helper.WidgetManager;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WidgetSettingsFragment extends DialogFragment {

    private static final List<Code> CODES = Arrays.asList(Code.AUTO, Code.REPAIR, Code.NEXT,
            Code.SPRINT, Code.T_MOBILE, Code.US_CELLULAR);
    private static final int SIZE = CODES.size();
    private static final String[] CHOICES = new String[SIZE];

    @Bind(R.id.widget_settings_1) Spinner spinner1;
    @Bind(R.id.widget_settings_2) Spinner spinner2;
    @Bind(R.id.widget_settings_3) Spinner spinner3;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_widget_settings, null);
        ButterKnife.bind(this, view);

        for (int i = 0; i < SIZE; ++i) {
            CHOICES[i] = getString(CODES.get(i).labelId);
        }

        setSpinner(spinner1, Preferences.ACTION_1, Constants.CODE_1);
        setSpinner(spinner2, Preferences.ACTION_2, Constants.CODE_2);
        setSpinner(spinner3, Preferences.ACTION_3, Constants.CODE_3);

        return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setView(view)
                .setTitle(R.string.widget_settings)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        save();
                        WidgetManager.update(getActivity());
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private void setSpinner(Spinner spinner, String preference, Code code) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.view_spinner_title, CHOICES);
        adapter.setDropDownViewResource(R.layout.view_spinner_item);
        spinner.setAdapter(adapter);
        String choice = PreferencesManager.getInstance(getActivity()).getString(preference, code.name());
        spinner.setSelection(CODES.indexOf(Code.get(choice)));
    }

    private void save() {
        save(Preferences.ACTION_1, spinner1.getSelectedItemPosition());
        save(Preferences.ACTION_2, spinner2.getSelectedItemPosition());
        save(Preferences.ACTION_3, spinner3.getSelectedItemPosition());
    }

    private void save(String preference, int choice) {
        PreferencesManager.getInstance(getActivity()).setString(preference, CODES.get(choice).name());
    }

}
