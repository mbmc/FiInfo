package com.mbmc.fiinfo.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.ui.activity.MainActivity;
import com.mbmc.fiinfo.util.DatabaseUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BackupFragment extends DialogFragment {

    @BindView(R.id.backup_location) TextView location;

    private Unbinder unbinder;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_backup, null);
        unbinder = ButterKnife.bind(this, view);

        location.setText(getString(R.string.backup_location, Environment.DIRECTORY_DOWNLOADS));

        return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setView(view)
                .setTitle(R.string.menu_backup)
                .create();
    }

    @OnClick(R.id.backup_export)
    void exportDb() {
        DatabaseUtil.export(getContext());
    }

    @OnClick(R.id.backup_import)
    void importDb() {
        DatabaseUtil.replace(getContext());
        ((MainActivity) getActivity()).onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
