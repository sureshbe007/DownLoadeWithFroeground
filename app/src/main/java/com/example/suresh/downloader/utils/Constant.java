package com.example.suresh.downloader.utils;

import java.io.File;

public class Constant {



    // For Directory Creation

    public static File file = null;
    public static final String FILE_SEPARATOR = File.separator;
    public static String FILE_URL = "file_url";
    public static String FILE_PATH = "file_path";
    public static String FILE_FORMAT = "file_format";
    public static String FILE_POSITION = "FILE_POSITION";
    public static final String ROOT_FOLDER = "CHENNAI";
    public static final String ROOT_VIDEO = "GUINDY";

    // For File Size Checking
    public static final double SIZE_KB = 1024;
    public static final double SIZE_MB = SIZE_KB * 1024L;
    public static final double SIZE_GB = SIZE_MB * 1024L;
    public static final double SIZE_TB = SIZE_GB * 1024L;
    public static final double SIZE_ONE_GB = (1024L * 1024L * 1024L) / SIZE_GB;

    // For BroadCastReceiver with Switch
    public static final String DOWNLOAD_START = "START";
    public static final String DOWNLOAD_CANCEL = "CANCEL";
    public static final String DOWNLOAD_PAUSE = "PAUSE";
    public static final String DOWNLOAD_RESUME = "RESUME";
    public static final String DOWNLOAD_PROGRESS = "PROGRESS";
    public static final String DOWNLOAD_COMPLETED = "COMPLETED";
    public static String ACTION_PROGRESS_STATUS = "ACTION_PROGRESS_STATUS"; // for receiver
    public static String ACTION_PROGRESS_NOTIFY = "ACTION_PROGRESS_NOTIFY"; // for receiver
    public static String ACTION_PROGRESS_POSITION = "ACTION_PROGRESS_POSITION"; // for receiver

    // For Foreground Service
    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_RESUME = "ACTION_RESUME";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_PROGRESS = "ACTION_PROGRESS";

    // For Button Click Listener
    public static String BUTTON_TEXT_START = "START";
    public static String BUTTON_TEXT_CANCEL = "CANCEL";
    public static String BUTTON_TEXT_PAUSE = "PAUSE";
    public static String BUTTON_TEXT_RESUME = "RESUME";

    public static String FILE_POSITION_NEW = "FILE_POSITION_NEW";
    public static String FILE_URL_NEW = "FILE_URL_NEW";
    public static String FILE_PATH_NEW = "FILE_PATH_NEW";
    public static String FILE_FORMAT_NEW = "FILE_FORMAT_NEW";

    public static String INTENT_PROGRESS_UPDATE = "INTENT_PROGRESS_UPDATE";
}
