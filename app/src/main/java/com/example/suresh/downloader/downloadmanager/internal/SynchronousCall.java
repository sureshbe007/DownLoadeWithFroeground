package com.example.suresh.downloader.downloadmanager.internal;


import com.example.suresh.downloader.downloadmanager.Response;
import com.example.suresh.downloader.downloadmanager.request.DownloadRequest;

public class SynchronousCall {

    public final DownloadRequest request;

    public SynchronousCall(DownloadRequest request) {
        this.request = request;
    }

    public Response execute() {
        DownloadTask downloadTask = DownloadTask.create(request);
        return downloadTask.run();
    }

}
