package com.mbmc.fiinfo.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.mbmc.fiinfo.BuildConfig;
import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.constant.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AboutFragment extends DialogFragment {

    @BindView(R.id.about_version) TextView version;
    @BindView(R.id.about_privacy_policy) TextView privacyPolicy;

    private Unbinder unbinder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_about, null);
        unbinder = ButterKnife.bind(this, view);

        version.setText(getString(R.string.about_version, BuildConfig.VERSION_NAME));

        Linkify.addLinks(privacyPolicy, Linkify.WEB_URLS);

        return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setView(view)
                .setTitle(R.string.about)
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.about_privacy_policy)
    void privacyPolicy() {
        openUrl(Constants.PRIVACY_POLICY);
    }

    private void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
