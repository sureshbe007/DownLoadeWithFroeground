package com.example.suresh.downloader.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.suresh.downloader.R;
import com.example.suresh.downloader.downloadmanager.database.DownloadModel;
import com.example.suresh.downloader.service.DownloadService;
import com.example.suresh.downloader.ui.base.BaseActivity;
import com.example.suresh.downloader.ui.callback.DownLoadStatus;
import com.example.suresh.downloader.utils.Constant;
import com.example.suresh.downloader.utils.DirectoryManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.suresh.downloader.ui.main.FileDownLoadTask.downloadCancel;
import static com.example.suresh.downloader.ui.main.FileDownLoadTask.downloadPause;
import static com.example.suresh.downloader.ui.main.FileDownLoadTask.downloadResume;
import static com.example.suresh.downloader.utils.Constant.DOWNLOAD_CANCEL;
import static com.example.suresh.downloader.utils.Constant.INTENT_PROGRESS_UPDATE;
import static com.example.suresh.downloader.utils.Constant.SIZE_ONE_GB;
import static com.example.suresh.downloader.utils.DirectoryManager.getAvailableInternalMemorySize;
import static com.example.suresh.downloader.utils.DirectoryManager.isExternalStorageAvailable;

public class MainActivity extends BaseActivity implements MainMvp {

    @BindView(R.id.rvDownload)
    RecyclerView downLoadRecycle;
    private MainAdapter downLoadAdapter;
    final String BASE_URL[] = {
            "https://dl.google.com/dl/android/studio/ide-zips/3.2.1.0/android-studio-ide-181.5056338-linux.zip",
            "https://dl.google.com/android/repository/android-ndk-r18b-windows-x86.zip",
            "https://dl.google.com/android/repository/android-ndk-r18b-darwin-x86_64.zip"};

    MainPresenter mMainPresenter;
    private Handler mHandler;
    public static List<DownloadModel> mDownloadModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainPresenter = new MainPresenter(this);
        mMainPresenter.getDownloadDetails(BASE_URL);
        mHandler = new Handler();
        if (isExternalStorageAvailable() && getAvailableInternalMemorySize() > SIZE_ONE_GB) {
            DirectoryManager.createDirectory();
        }
    }

//    @Override
//    public void sendDownLoadProgress(int position, DownloadModel downloadModel) {
//
//        try {
//            Log.d("DOWWLOADER2525", " sendDownLoadProgress  notify     Activity " + downloadModel.getUrl());
//            Log.d("DOWWLOADER2525", " sendDownLoadProgress    notify   Activity " + downloadModel.getDownloadedBytes());
//            downLoadAdapter.notifyItemChanged(position, downloadModel);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    @Override
    public void setAdapter(List<DownloadModel> downloadModelList) {

        try {
            mDownloadModelList = downloadModelList;
            downLoadAdapter = new MainAdapter(MainActivity.this, new DownLoadStatus() {
                @Override
                public void sendDownLodStatus(String DownLoadStatus, int filePosition, String fileUrl, DownloadModel downloadModel) {

                    switch (DownLoadStatus) {
                        case Constant.DOWNLOAD_START:
                            Intent intent = new Intent(MainActivity.this, DownloadService.class);
                            intent.putExtra("FILE_POSITION", filePosition);
                            intent.putExtra("FILE_URL", fileUrl);
                            intent.putExtra("FILE_MODEL", downloadModel);
                            startService(intent);
                            break;
                        case DOWNLOAD_CANCEL:
                            Log.d("DOWWLOADER2525", " downloadCancel Activity   " + downloadModel.getUrl());
                            downloadCancel(fileUrl);
                            mMainPresenter.getDownloadDetails(BASE_URL);
                            break;
                        case Constant.DOWNLOAD_PAUSE:
                            Log.d("DOWWLOADER2525", " downloadPause Activity   " + downloadModel.getUrl());
                            downloadPause(fileUrl);
                            break;
                        case Constant.DOWNLOAD_RESUME:
                            Intent resume = new Intent(MainActivity.this, DownloadService.class);
                            resume.putExtra("FILE_POSITION", filePosition);
                            resume.putExtra("FILE_URL", fileUrl);
                            resume.putExtra("FILE_MODEL", downloadModel);
                            startService(resume);
                            break;
                    }
                }


            }, mDownloadModelList);

            downLoadRecycle.setHasFixedSize(true);
            downLoadRecycle.setAdapter(downLoadAdapter);
            downLoadRecycle.setLayoutManager(new LinearLayoutManager(this));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(progressUpdate, new IntentFilter(INTENT_PROGRESS_UPDATE));
    }

    BroadcastReceiver progressUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                //UI update here
                if (intent != null) {
                    final int position = intent.getIntExtra("FILE_POSITION", -1);
                    final DownloadModel downloadModel = (DownloadModel) intent.getSerializableExtra("FILE_MODEL");
                    if (position != -1) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mDownloadModelList.set(position, downloadModel);
                                downLoadAdapter.notifyItemChanged(position, mDownloadModelList);
                            }
                        });
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (progressUpdate != null)
            unregisterReceiver(progressUpdate);
    }
}
