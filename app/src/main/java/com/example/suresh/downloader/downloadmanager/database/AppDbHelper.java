/*
 *    Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.example.suresh.downloader.downloadmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.example.suresh.downloader.downloadmanager.database.DownloadModel.DIR_PATH;
import static com.example.suresh.downloader.downloadmanager.database.DownloadModel.DOWNLOADED_BYTES;
import static com.example.suresh.downloader.downloadmanager.database.DownloadModel.ETAG;
import static com.example.suresh.downloader.downloadmanager.database.DownloadModel.FILE_NAME;
import static com.example.suresh.downloader.downloadmanager.database.DownloadModel.ID;
import static com.example.suresh.downloader.downloadmanager.database.DownloadModel.LAST_MODIFIED_AT;
import static com.example.suresh.downloader.downloadmanager.database.DownloadModel.TOTAL_BYTES;
import static com.example.suresh.downloader.downloadmanager.database.DownloadModel.URL;


/**
 * Created by anandgaurav on 14-11-2017.
 */

public class AppDbHelper implements DbHelper {

    public static final String TABLE_NAME = "prdownloader";
    private final SQLiteDatabase db;

    public AppDbHelper(Context context) {
        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(context);
        db = databaseOpenHelper.getWritableDatabase();
    }

    @Override
    public DownloadModel find(int id) {
        Cursor cursor = null;
        DownloadModel model = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                    ID + " = " + id, null);
            if (cursor != null && cursor.moveToFirst()) {
                model = new DownloadModel();
                model.setId(id);
                model.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
                model.setETag(cursor.getString(cursor.getColumnIndex(ETAG)));
                model.setDirPath(cursor.getString(cursor.getColumnIndex(DIR_PATH)));
                model.setFileName(cursor.getString(cursor.getColumnIndex(FILE_NAME)));
                model.setTotalBytes(cursor.getLong(cursor.getColumnIndex(TOTAL_BYTES)));
                model.setDownloadedBytes(cursor.getLong(cursor.getColumnIndex(DOWNLOADED_BYTES)));
                model.setLastModifiedAt(cursor.getLong(cursor.getColumnIndex(LAST_MODIFIED_AT)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return model;
    }

    @Override
    public void insert(DownloadModel model) {
        try {
            ContentValues values = new ContentValues();
            values.put(ID, model.getId());
            values.put(URL, model.getUrl());
            values.put(ETAG, model.getETag());
            values.put(DIR_PATH, model.getDirPath());
            values.put(FILE_NAME, model.getFileName());
            values.put(TOTAL_BYTES, model.getTotalBytes());
            values.put(DOWNLOADED_BYTES, model.getDownloadedBytes());
            values.put(LAST_MODIFIED_AT, model.getLastModifiedAt());
            db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(DownloadModel model) {
        try {
            ContentValues values = new ContentValues();
            values.put(URL, model.getUrl());
            values.put(ETAG, model.getETag());
            values.put(DIR_PATH, model.getDirPath());
            values.put(FILE_NAME, model.getFileName());
            values.put(TOTAL_BYTES, model.getTotalBytes());
            values.put(DOWNLOADED_BYTES, model.getDownloadedBytes());
            values.put(LAST_MODIFIED_AT, model.getLastModifiedAt());
            db.update(TABLE_NAME, values, ID + " = ? ",
                    new String[]{String.valueOf(model.getId())});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProgress(int id, long downloadedBytes, long lastModifiedAt) {
        try {
            ContentValues values = new ContentValues();
            values.put(DOWNLOADED_BYTES, downloadedBytes);
            values.put(LAST_MODIFIED_AT, lastModifiedAt);
            db.update(TABLE_NAME, values, ID + " = ? ",
                    new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) {
        try {
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " +
                    ID + " = " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<DownloadModel> getUnwantedModels(int days) {
        List<DownloadModel> models = new ArrayList<>();
        Cursor cursor = null;
        try {
            final long daysInMillis = days * 24 * 60 * 60 * 1000L;
            final long beforeTimeInMillis = System.currentTimeMillis() - daysInMillis;
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                    LAST_MODIFIED_AT + " <= " + beforeTimeInMillis, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    DownloadModel model = new DownloadModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    model.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
                    model.setETag(cursor.getString(cursor.getColumnIndex(ETAG)));
                    model.setDirPath(cursor.getString(cursor.getColumnIndex(DIR_PATH)));
                    model.setFileName(cursor.getString(cursor.getColumnIndex(FILE_NAME)));
                    model.setTotalBytes(cursor.getLong(cursor.getColumnIndex(TOTAL_BYTES)));
                    model.setDownloadedBytes(cursor.getLong(cursor.getColumnIndex(DOWNLOADED_BYTES)));
                    model.setLastModifiedAt(cursor.getLong(cursor.getColumnIndex(LAST_MODIFIED_AT)));

                    models.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return models;
    }

    @Override
    public DownloadModel getDownloadDetails(String fileUrl) {
        Log.d("DOWNLOAD_DETAILS", " ::::::::fileUrl::::::: " + fileUrl);
        DownloadModel downloadModel = null;
        Cursor cursor = null;
        try {
//            cursor = db.query(TABLE_NAME, new String[]{ID,
//                            URL, ETAG, DIR_PATH, FILE_NAME, TOTAL_BYTES, DOWNLOADED_BYTES, LAST_MODIFIED_AT}, ID + "=?",
//                    new String[]{String.valueOf(fileUrl)}, null, null, null, null);

//            Here i am using raw query
            String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + URL + " = '" + fileUrl + "'";
            cursor = db.rawQuery(queryString, new String[]{});
            downloadModel = new DownloadModel();
            if (cursor != null && cursor.moveToFirst()) {
                downloadModel.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                downloadModel.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
                downloadModel.setETag(cursor.getString(cursor.getColumnIndex(ETAG)));
                downloadModel.setDirPath(cursor.getString(cursor.getColumnIndex(DIR_PATH)));
                downloadModel.setFileName(cursor.getString(cursor.getColumnIndex(FILE_NAME)));
                downloadModel.setTotalBytes(cursor.getLong(cursor.getColumnIndex(TOTAL_BYTES)));
                downloadModel.setDownloadedBytes(cursor.getLong(cursor.getColumnIndex(DOWNLOADED_BYTES)));
                downloadModel.setLastModifiedAt(cursor.getLong(cursor.getColumnIndex(LAST_MODIFIED_AT)));
            } else {
                downloadModel.setId(0);
                downloadModel.setUrl((fileUrl));
                downloadModel.setETag("");
                downloadModel.setDirPath((""));
                downloadModel.setFileName("");
                downloadModel.setTotalBytes(0);
                downloadModel.setDownloadedBytes(0);
                downloadModel.setLastModifiedAt(0);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return downloadModel;
    }

    @Override
    public void clear() {
        try {
            switch (db.delete(TABLE_NAME, null, null)) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getDownloadID(String fileUrl) {
        Log.d("ADAPTER_POSITION", "SQLite getDownloadID()  " + fileUrl);
        long downloadId = 0;
        Cursor cursor = db.query(TABLE_NAME, new String[]{ID}, URL + "=?",
                new String[]{fileUrl}, null, null, null, null);
        Log.d("ADAPTER_POSITION", "SQLite getDownloadID()  " + cursor.getCount());
        if (cursor != null && cursor.moveToFirst())
            downloadId = cursor.getInt(cursor.getColumnIndex(ID));
        return downloadId;

    }
}
