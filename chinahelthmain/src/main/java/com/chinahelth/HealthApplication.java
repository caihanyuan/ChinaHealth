package com.chinahelth;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by caihanyuan on 6/27/15.
 */
public class HealthApplication extends Application {
    private final static String TAG = HealthApplication.class.getName();

    private static HealthApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        HealthConfig.initDefaultImageLoader();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTerminate() {
        instance = null;
        HealthConfig.destory();
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static HealthApplication getInstance() {
        return instance;
    }
}
