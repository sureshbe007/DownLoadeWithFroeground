package com.example.suresh.downloader;

import android.app.Application;

import com.example.suresh.downloader.downloadmanager.PRDownloader;
import com.example.suresh.downloader.downloadmanager.PRDownloaderConfig;

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(this, config);
    }
}
