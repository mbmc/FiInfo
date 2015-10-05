package com.mbmc.fiinfo.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.mbmc.fiinfo.BuildConfig;
import com.mbmc.fiinfo.helper.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class DatabaseUtil {

    public static void export(Context context){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/"+ BuildConfig.APPLICATION_ID +"/databases/" + Database.NAME;
        String backupDBPath = "events.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(context, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
