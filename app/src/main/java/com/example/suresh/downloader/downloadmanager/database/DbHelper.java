

package com.example.suresh.downloader.downloadmanager.database;

import java.util.List;



public interface DbHelper {

    DownloadModel find(int id);

    void insert(DownloadModel model);

    void update(DownloadModel model);

    void updateProgress(int id, long downloadedBytes, long lastModifiedAt);

    void remove(int id);

    List<DownloadModel> getUnwantedModels(int days);

    DownloadModel getDownloadDetails(String fileUrl);

    void clear();

    long getDownloadID(String fileUrl);

}
