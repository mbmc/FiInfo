package com.mbmc.fiinfo.helper;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesManager {

    private static final String FILE = "user_preferences";
    private static final boolean DEFAULT_BOOLEAN = false;

    private static PreferencesManager instance;

    private SharedPreferences sharedPreferences;


    public static PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
        return instance;
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, DEFAULT_BOOLEAN);
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);

        editor.apply();
    }


    private PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    }

}
