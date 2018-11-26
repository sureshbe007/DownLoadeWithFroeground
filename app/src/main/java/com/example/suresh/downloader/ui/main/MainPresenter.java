package com.example.suresh.downloader.ui.main;

import com.example.suresh.downloader.downloadmanager.database.DownloadModel;
import com.example.suresh.downloader.downloadmanager.internal.ComponentHolder;
import com.example.suresh.downloader.ui.callback.ProgressUpdate;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter implements MainMvpPresenter {

    private MainMvp view;

    MainPresenter(MainMvp mainMvp) {
        view = mainMvp;
    }

    @Override
    public void getDownloadDetails(String[] fileUrl) {

        try {
            List<DownloadModel> downloadModelList = new ArrayList<>();
            for (int i = 0; i < fileUrl.length; i++) {
                downloadModelList.add(ComponentHolder.getInstance().getDbHelper().getDownloadDetails(fileUrl[i]));
            }
            view.setAdapter(downloadModelList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    }


