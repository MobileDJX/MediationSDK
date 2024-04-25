package com.djax.sdkaddemo;

import android.app.Application;
import android.content.SharedPreferences;

public class AppApplication extends Application {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("Djaxdemo",MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }
}
