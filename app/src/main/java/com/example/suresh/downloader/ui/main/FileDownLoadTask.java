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

    private static int downloadID;
    public FileDownLoadTask() {
    }

    public static void downLoadStart(final int filePosition, String fileUrl, final DownloadModel downloadModel, final ProgressUpdate progressUpdate) {
        Log.d("DOWWLOADER2525", " downLoadStart  FileDownLoadTask:  " + ComponentHolder.getInstance().getDbHelper().getDownloadID(fileUrl));
        final ProgressUpdate mProgressUpdate = progressUpdate;
        File file = new File(fileUrl);
        String fileFormat = DirectoryManager.getFileExtension(file);
        String filePath = DirectoryManager.createDirectory();
        downloadID = PRDownloader.download(fileUrl, filePath, fileFormat)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        Log.d("DOWWLOADER25256565", "OnStartOrResumeListener:: onStartOrResume ");
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                        Log.d("DOWWLOADER25256565", "OnStartOrResumeListener:: OnPauseListener ");
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        downloadModel.setDownloadedBytes(progress.currentBytes);
                        downloadModel.setTotalBytes(progress.totalBytes);
                        mProgressUpdate.sendDownLoadProgress(filePosition, downloadModel);
//                        Log.d("DOWWLOADER25253535", "OnProgressListener:: Progress " + downloadID);

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        Log.d("DOWWLOADER25256565", "OnStartOrResumeListener:: OnCancelListener ");
                        downloadModel.setDownloadedBytes(0);
                        mProgressUpdate.sendDownLoadProgress(filePosition, downloadModel);
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        progressUpdate.stopDownLoad();
                        Log.d("DOWWLOADER2525", "downloadPause() Btn  onDownloadComplete ");
                    }

                    @Override
                    public void onError(Error error) {
                        Log.d("DOWWLOADER2525", "downloadPause() Btn  onError ");
                    }

                });

    }

    public static void downloadPause(String fileUrl) {
        Log.d("DOWWLOADER25256565", "FileDownLoadTask downloadPause" + fileUrl);
        long downLoadId = ComponentHolder.getInstance().getDbHelper().getDownloadID(fileUrl);
        Log.d("DOWWLOADER25256565", "FileDownLoadTask downloadPause" + downLoadId);
        if (Status.RUNNING == PRDownloader.getStatus((int) downLoadId)) {
            PRDownloader.pause((int) downLoadId);
            return;
        }
    }

    public static void downloadResume(String fileUrl) {

        Log.d("DOWWLOADER25256565", "FileDownLoadTask downloadResume" + fileUrl);
        long downLoadId = ComponentHolder.getInstance().getDbHelper().getDownloadID(fileUrl);
        Log.d("DOWWLOADER25256565", "FileDownLoadTask downloadResume" + downLoadId);
        if (Status.PAUSED == PRDownloader.getStatus((int) downLoadId)) {
            PRDownloader.resume((int) downLoadId);
            return;
        }
    }

    public static void downloadCancel(String fileUrl) {
        Log.d("DOWWLOADER25256565", "FileDownLoadTask downloadCancel" + fileUrl);
        long downLoadId = ComponentHolder.getInstance().getDbHelper().getDownloadID(fileUrl);
        Log.d("DOWWLOADER25256565", "FileDownLoadTask  downloadCancel" + downLoadId);
        if (Status.RUNNING == PRDownloader.getStatus((int) downLoadId)) {
            PRDownloader.cancel((int) downLoadId);
            return;
        }
    }
}
