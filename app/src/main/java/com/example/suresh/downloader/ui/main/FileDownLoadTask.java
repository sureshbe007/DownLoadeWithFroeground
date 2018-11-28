package com.example.suresh.downloader.ui.main;

import android.util.Log;

import com.example.suresh.downloader.downloadmanager.Error;
import com.example.suresh.downloader.downloadmanager.OnCancelListener;
import com.example.suresh.downloader.downloadmanager.OnDownloadListener;
import com.example.suresh.downloader.downloadmanager.OnPauseListener;
import com.example.suresh.downloader.downloadmanager.OnProgressListener;
import com.example.suresh.downloader.downloadmanager.OnStartOrResumeListener;
import com.example.suresh.downloader.downloadmanager.PRDownloader;
import com.example.suresh.downloader.downloadmanager.Progress;
import com.example.suresh.downloader.downloadmanager.Status;
import com.example.suresh.downloader.downloadmanager.database.DownloadModel;
import com.example.suresh.downloader.downloadmanager.internal.ComponentHolder;
import com.example.suresh.downloader.ui.callback.ProgressUpdate;
import com.example.suresh.downloader.utils.DirectoryManager;

import java.io.File;

public class FileDownLoadTask {

    private static long latupdate;
    public FileDownLoadTask() {
    }

    public static void downLoadStart(final int filePosition, String fileUrl, final DownloadModel downloadModel, final ProgressUpdate progressUpdate) {
        latupdate = System.currentTimeMillis();

        Log.d("FILE_OPERATION_CHECK", " 12345   ::::::::: " + filePosition);
        Log.d("FILE_OPERATION_CHECK", " 67890   ::::::::: " + fileUrl);
        File file = new File(fileUrl);
        String fileFormat = DirectoryManager.getFileExtension(file);
        String filePath = DirectoryManager.createDirectory();
        PRDownloader.download(fileUrl, filePath, fileFormat)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                        Log.d("FILE_OPERATION_CHECK", "setOnPauseListener         ");
                        progressUpdate.stopDownLoad();
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        if (System.currentTimeMillis() > latupdate + 2000) {
                            downloadModel.setDownloadedBytes(progress.currentBytes);
                            downloadModel.setTotalBytes(progress.totalBytes);
                            progressUpdate.sendDownLoadProgress(filePosition, downloadModel);
                            latupdate = System.currentTimeMillis();
                        }

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        Log.d("FILE_OPERATION_CHECK", "setOnCancelListener         ");
                        downloadModel.setDownloadedBytes(0);
                        downloadModel.setTotalBytes(0);
                        progressUpdate.sendDownLoadProgress(filePosition, downloadModel);
                        progressUpdate.stopDownLoad();

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Log.d("FILE_OPERATION_CHECK", "OnDownloadListener         ");
                        progressUpdate.stopDownLoad();
                    }

                    @Override
                    public void onError(Error error) {
                        progressUpdate.stopDownLoad();
                    }

                });

    }

    public static void downloadPause(String fileUrl) {
        long downLoadId = ComponentHolder.getInstance().getDbHelper().getDownloadID(fileUrl);
        Log.d("FILE_OPERATION_CHECK", "downloadPause         " + downLoadId);
        if (Status.RUNNING == PRDownloader.getStatus((int) downLoadId)) {
            PRDownloader.pause((int) downLoadId);
            return;
        }
    }

    public static void downloadResume(String fileUrl) {
        long downLoadId = ComponentHolder.getInstance().getDbHelper().getDownloadID(fileUrl);
        Log.d("FILE_OPERATION_CHECK", "downloadResume         " + downLoadId);
        if (Status.PAUSED == PRDownloader.getStatus((int) downLoadId)) {
            PRDownloader.resume((int) downLoadId);
            return;
        }

    }

    public static void downloadCancel(String fileUrl) {
        long downLoadId = ComponentHolder.getInstance().getDbHelper().getDownloadID(fileUrl);
        Log.d("FILE_OPERATION_CHECK", "downloadCancel         " + downLoadId);
        Log.d("FILE_OPERATION_CHECK", "downloadCancel         " + PRDownloader.getStatus((int) downLoadId));
        if (Status.RUNNING == PRDownloader.getStatus((int) downLoadId) || Status.PAUSED == PRDownloader.getStatus((int) downLoadId)) {
            PRDownloader.cancel((int) downLoadId);
            return;
        } else if (Status.UNKNOWN == PRDownloader.getStatus((int) downLoadId)) {
            ComponentHolder.getInstance().getDbHelper().remove((int)downLoadId);
            return;
        }
    }
}
