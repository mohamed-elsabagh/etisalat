package com.delvebyte.etisalatapp.activity;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static Context getsAppContext() {
        return sInstance.getApplicationContext();
    }
}
