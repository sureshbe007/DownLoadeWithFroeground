package com.example.suresh.downloader.service;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

public class StatusChecking {

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) throws Exception {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service already", "running");
                return true;
            }
        }
        Log.i("Service not", "running");
        return false;
    }
}
