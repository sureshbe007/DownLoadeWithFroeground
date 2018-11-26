package com.example.suresh.downloader.utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import static com.example.suresh.downloader.utils.Constant.FILE_SEPARATOR;
import static com.example.suresh.downloader.utils.Constant.ROOT_FOLDER;
import static com.example.suresh.downloader.utils.Constant.ROOT_VIDEO;
import static com.example.suresh.downloader.utils.Constant.SIZE_GB;
import static com.example.suresh.downloader.utils.Constant.SIZE_KB;
import static com.example.suresh.downloader.utils.Constant.SIZE_MB;
import static com.example.suresh.downloader.utils.Constant.SIZE_TB;
import static com.example.suresh.downloader.utils.Constant.file;

public class DirectoryManager {


    // For GetRootPath
    public static String getRootPath() {
        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .getAbsolutePath().toString();
        return extStorageDirectory;
    }

    // For Create Directory
    public static String createDirectory() {
        file = new File(getRootPath() + FILE_SEPARATOR + ROOT_FOLDER
                + FILE_SEPARATOR + ROOT_VIDEO);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.toString();
    }

    // For Check External Storage Availability
    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    // For Check External Storage Availability Memory
    public static double getAvailableInternalMemorySize() {
        long blockSize=0;
        long availableBlocks=0;
        double result;
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
             blockSize = stat.getBlockSize();
             availableBlocks = stat.getAvailableBlocks();
        }catch (NumberFormatException e)
        {
             e.getMessage();
        }
        result=Double.parseDouble(formatFileSize(availableBlocks * blockSize).replaceAll(",", ""));
        Log.d("DOWWLOADER25256565", "getAvailableInternalMemorySize " + result);
        return result;
    }

    public static long getAvailableExternalMemorySize() {
        if (isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
//            formatFileSize(availableBlocks *blockSize);
            return availableBlocks * blockSize;
        } else {

            return 0;
        }
    }

    // For get File Size
    public static String formatFileSize(final long sizeInBytes) {

        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);

        try {
            if (sizeInBytes < SIZE_KB) {
                return nf.format(sizeInBytes)+ " Byte(s)";
            } else if (sizeInBytes < SIZE_MB) {
                Log.d("SDMANAGER123", "formatFileSize" + nf.format(sizeInBytes / SIZE_KB) + " KB");
                return nf.format(sizeInBytes / SIZE_KB);
            } else if (sizeInBytes < SIZE_GB) {
                Log.d("SDMANAGER123", "formatFileSize" + nf.format(sizeInBytes / SIZE_MB) + " MB");
                return nf.format(sizeInBytes / SIZE_MB);
            } else if (sizeInBytes < SIZE_TB) {
                Log.d("SDMANAGER123", "formatFileSize" + nf.format(sizeInBytes / SIZE_GB) + " GB");
                return nf.format(sizeInBytes / SIZE_GB);
            } else {
                Log.d("SDMANAGER123", "formatFileSize" + nf.format(sizeInBytes / SIZE_TB) + " TB");
                return nf.format(sizeInBytes / SIZE_TB);
            }
        } catch (Exception e) {
            return sizeInBytes +"Byte(s)";
        }
    }

    // get File Format
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }


    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    private static String getBytesToMBString(long bytes){
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }
}
