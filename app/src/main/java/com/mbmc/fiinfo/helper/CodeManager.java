package com.mbmc.fiinfo.helper;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.constant.Preferences;
import com.mbmc.fiinfo.ui.activity.MainActivity;


public class CodeManager {

    public static void send(Activity activity, int code) {
        if (!PreferencesManager.getInstance(activity)
                .getBoolean(Preferences.HIDE_CODE_INSTRUCTIONS)) {
            ((MainActivity) activity).showCodeInstructions(code);
        } else {
            openDialer(activity, code);
        }
    }

    public static void openDialer(Context context, int code) {
        copy(context, code);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private static void copy(Context context, int code) {
        ClipboardManager clipboardManager =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(context.getString(R.string.code), context.getString(R.string.code_dialer, code));
        clipboardManager.setPrimaryClip(clipData);
    }

}
