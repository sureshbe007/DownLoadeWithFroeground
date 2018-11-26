package com.example.suresh.downloader.ui.callback;

import com.example.suresh.downloader.downloadmanager.database.DownloadModel;

public interface DownLoadStatus {

    void sendDownLodStatus(String DownLoadStatus, int filePosition, String fileUrl, DownloadModel DownloadModel);
}
