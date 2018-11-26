package com.example.suresh.downloader.ui.main;

import com.example.suresh.downloader.downloadmanager.database.DownloadModel;

import java.util.List;

public interface MainMvp {

    void setAdapter(List<DownloadModel> downloadModelList);
}
