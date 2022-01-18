package com.mbmc.fiinfo.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.constant.Preferences;
import com.mbmc.fiinfo.helper.PreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.Unbinder;

public class NotificationSettingsFragment extends DialogFragment {

    @BindView(R.id.notification_settings_enable) Switch enable;
    @BindView(R.id.notification_settings_sound) Switch sound;
    @BindView(R.id.notification_settings_vibration) Switch vibration;

    private Unbinder unbinder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_notification_settings, null);
        unbinder = ButterKnife.bind(this, view);

        enable.setChecked(PreferencesManager.getInstance(getActivity())
                .getBoolean(Preferences.NOTIFICATION_ENABLE));
        sound.setChecked(PreferencesManager.getInstance(getActivity())
                .getBoolean(Preferences.NOTIFICATION_SOUND));
        vibration.setChecked(PreferencesManager.getInstance(getActivity())
                .getBoolean(Preferences.NOTIFICATION_VIBRATE));
        enable(enable.isChecked());

        return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setView(view)
                .setTitle(R.string.notification_settings)
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnCheckedChanged(R.id.notification_settings_enable)
    void enable(boolean checked) {
        PreferencesManager.getInstance(getActivity()).setBoolean(Preferences.NOTIFICATION_ENABLE,
                checked);
        activate(sound, checked);
        activate(vibration, checked);
    }

    @OnCheckedChanged(R.id.notification_settings_sound)
    void sound(boolean checked) {
        PreferencesManager.getInstance(getActivity()).setBoolean(Preferences.NOTIFICATION_SOUND,
                checked);
    }

    @OnCheckedChanged(R.id.notification_settings_vibration)
    void vibration(boolean checked) {
        PreferencesManager.getInstance(getActivity()).setBoolean(Preferences.NOTIFICATION_VIBRATE,
                checked);
    }

    private void activate(Switch switchView, boolean activate) {
        switchView.setEnabled(activate);
        switchView.setAlpha(activate ? 1.0f : 0.5f);
    }
}
