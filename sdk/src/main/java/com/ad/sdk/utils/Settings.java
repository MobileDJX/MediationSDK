package com.ad.sdk.utils;

import java.util.Locale;

public class Settings {

    public String app_id = null;

    public String ua = null;

    public final String language = Locale.getDefault().getLanguage();

    public final int FETCH_THREAD_COUNT = 4;

    public final int MIN_REFRESH_MILLISECONDS = 15000;

    // STATICS
    private static Settings settings_instance = null;

    public static Settings getSettings() {
        if (settings_instance == null) {
            settings_instance = new Settings();
            Cdlog.v(Cdlog.baseLogTag, "The Msdk " + Cdlog.baseLogTag
                    + " is initializing.");
        }
        return settings_instance;
    }

    private Settings() {

    }

}
