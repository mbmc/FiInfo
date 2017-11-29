package com.mbmc.fiinfo.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.helper.Database;

import java.io.File;
import java.io.IOException;


public final class DatabaseUtil {

    private static final String FILE = "signal_info.db";
    private static final File BACKUP =
            new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), FILE);


    public static void export(Context context) {
        try {
            FileUtil.copy(context.getDatabasePath(Database.NAME), BACKUP);
            Toast.makeText(context, R.string.backup_export_success, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.backup_export_error, Toast.LENGTH_LONG).show();
        }
    }

    public static void replace(Context context) {
        try {
            FileUtil.copy(BACKUP, context.getDatabasePath(Database.NAME));
            Toast.makeText(context, R.string.backup_import_success, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.backup_import_error, Toast.LENGTH_LONG).show();
        }
    }


    private DatabaseUtil() {

    }

}
