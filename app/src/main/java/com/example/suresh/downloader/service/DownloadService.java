package com.example.suresh.downloader.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.suresh.downloader.R;
import com.example.suresh.downloader.downloadmanager.database.DownloadModel;
import com.example.suresh.downloader.ui.callback.ProgressUpdate;
import com.example.suresh.downloader.ui.main.MainActivity;
import com.example.suresh.downloader.utils.Constant;

import static com.example.suresh.downloader.ui.main.FileDownLoadTask.downLoadStart;

public class DownloadService extends Service implements ProgressUpdate {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (intent.getExtras() == null) {
                Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show();
            } else {
                int filePosition = intent.getIntExtra("FILE_POSITION", -1);
                String fileUrl = intent.getStringExtra("FILE_URL");
                DownloadModel downloadModel = (DownloadModel) intent.getSerializableExtra("FILE_MODEL");
                startForegroundService(filePosition, fileUrl, downloadModel);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        return START_STICKY;
    }


    @Override

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startForegroundService(int filePosition, String fileUrl, DownloadModel downloadModel) {
        String NOTIFICATION_CHANNEL_ID = "100";
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("File Download")
                .setContentText("Download in progress...")
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        if (Build.VERSION.SDK_INT >= 26) {

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("NOTIFICATION_CHANNEL_DESC");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(1, notification);
        downLoadStart(filePosition, fileUrl, downloadModel, this);
    }

    @Override
    public void sendDownLoadProgress(int position, DownloadModel downloadModel) {
        Intent intent = new Intent(Constant.INTENT_PROGRESS_UPDATE);
        intent.putExtra("FILE_POSITION", position);
        intent.putExtra("FILE_MODEL", downloadModel);
        sendBroadcast(intent);

    }

    @Override
    public void stopDownLoad() {
        stopForeground(true);
        stopSelf();
    }
    }

