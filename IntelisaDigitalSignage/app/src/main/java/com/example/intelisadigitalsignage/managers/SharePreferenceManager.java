package com.example.intelisadigitalsignage.managers;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import com.example.intelisadigitalsignage.AppState;

/**
 */

public class SharePreferenceManager {
    private static final SharePreferenceManager INSTANCE = new SharePreferenceManager();
    private static final String APPINFO = "app_info";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private SharePreferenceManager() {
        mSharedPreferences = AppState.sContext.getSharedPreferences(APPINFO, MODE_PRIVATE);
    }

    public static SharePreferenceManager getInstance() {
        return INSTANCE;
    }

    public SharedPreferences getSharePreference() {
        return mSharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        if (mEditor == null) {
            mEditor = mSharedPreferences.edit();
        }
        return mEditor;
    }
}
