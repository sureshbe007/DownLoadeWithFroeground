package com.example.suresh.downloader.ui.callback;

import com.example.suresh.downloader.downloadmanager.database.DownloadModel;

public interface ProgressUpdate {

    void sendDownLoadProgress(int position, DownloadModel downloadModel);
    void stopDownLoad();
}
