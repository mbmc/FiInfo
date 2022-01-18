package com.mbmc.fiinfo.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.constant.Constants;
import com.mbmc.fiinfo.constant.Preferences;
import com.mbmc.fiinfo.data.Code;
import com.mbmc.fiinfo.helper.PreferencesManager;
import com.mbmc.fiinfo.helper.WidgetManager;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WidgetSettingsFragment extends DialogFragment {

    private static final List<Code> CODES = Arrays.asList(Code.AUTO, Code.REPAIR, Code.NEXT,
            Code.SPRINT, Code.T_MOBILE, Code.THREE_UK, Code.US_CELLULAR);
    private static final int SIZE = CODES.size();
    private static final String[] CHOICES = new String[SIZE];

    @BindView(R.id.widget_settings_1) Spinner spinner1;
    @BindView(R.id.widget_settings_2) Spinner spinner2;
    @BindView(R.id.widget_settings_3) Spinner spinner3;

    private Unbinder unbinder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_widget_settings, null);
        unbinder = ButterKnife.bind(this, view);

        for (int i = 0; i < SIZE; ++i) {
            CHOICES[i] = getString(CODES.get(i).labelId);
        }

        setSpinner(spinner1, Preferences.ACTION_1, Constants.CODE_1);
        setSpinner(spinner2, Preferences.ACTION_2, Constants.CODE_2);
        setSpinner(spinner3, Preferences.ACTION_3, Constants.CODE_3);

        return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setView(view)
                .setTitle(R.string.widget_settings)
                .setPositiveButton(R.string.ok, (DialogInterface dialogInterface, int which) -> {
                    save();
                    WidgetManager.update(getActivity());
                    dialogInterface.dismiss();
                })
                .setNegativeButton(R.string.cancel, (DialogInterface dialogInterface, int which)
                        -> dialogInterface.dismiss())
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setSpinner(Spinner spinner, String preference, Code code) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.view_spinner_title, CHOICES);
        adapter.setDropDownViewResource(R.layout.view_spinner_item);
        spinner.setAdapter(adapter);
        String choice = PreferencesManager.getInstance(getActivity()).getString(preference,
                code.name());
        spinner.setSelection(CODES.indexOf(Code.get(choice)));
    }

    private void save() {
        save(Preferences.ACTION_1, spinner1.getSelectedItemPosition());
        save(Preferences.ACTION_2, spinner2.getSelectedItemPosition());
        save(Preferences.ACTION_3, spinner3.getSelectedItemPosition());
    }

    private void save(String preference, int choice) {
        PreferencesManager.getInstance(getActivity()).setString(preference,
                CODES.get(choice).name());
    }
}
