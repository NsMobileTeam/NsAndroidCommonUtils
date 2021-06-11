package com.nextsense.nsutils.storage;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.Nullable;

import com.nextsense.nsutils.commons.ResourceFetch;
import com.nextsense.nsutils.UtilBase;

import java.io.File;

import static com.nextsense.nsutils.storage.FileUtil.fileNameFromUrl;
import static com.nextsense.nsutils.storage.FileUtil.fullFileNameFromUrl;
import static com.nextsense.nsutils.storage.FileUtil.mimeTypeOf;

public class DownloadUtil {


    /**
     * Download a file from a HTTP url into the internal storage
     * @param url HTTP link
     */
    public static void downloadUrl(String url) {
        downloadUrl(url, null, true, false, null);
    }


    /**
     * Download a file from a HTTP url into the internal storage
     * @param url HTTP link
     * @param callback Download completion listener
     */
    public static void downloadUrl(String url, @Nullable DownloadUtil.DownloadCallback callback) {
        downloadUrl(url, null, false, false, callback);
    }


    /**
     * Download a file from a HTTP url
     * @param url HTTP link
     * @param publicFile if true the file will be saved under the Downloads directory
     * @param callback Download completion listener
     */
    public static void downloadUrl(String url, boolean publicFile, @Nullable DownloadUtil.DownloadCallback callback) {
        downloadUrl(url, null, false, publicFile, callback);
    }

    /**
     * Download a file from a HTTP url
     * @param url HTTP link
     * @param subDirectory Name of a subdirectory for the downloaded file
     * @param notification visibility of the download notification
     * @param publicFile if true the file will be saved under the Downloads directory
     * @param callback Download completion listener
     */
    public static void downloadUrl(String url, @Nullable String subDirectory, boolean notification, boolean publicFile, @Nullable DownloadUtil.DownloadCallback callback) {
        try {
            Uri uri = Uri.parse(url);
            DownloadManager.Request downloadRequest = new DownloadManager.Request(uri);
            downloadRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            downloadRequest.setTitle(fileNameFromUrl(url));
            downloadRequest.setAllowedOverMetered(true);
            downloadRequest.setNotificationVisibility(notification ? DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED : DownloadManager.Request.VISIBILITY_HIDDEN);
            if (publicFile) {
                String subPath = subDirectory != null ? String.format("%s/%s", subDirectory, fullFileNameFromUrl(url)) : fullFileNameFromUrl(url);
                downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, subPath);
            } else {
                downloadRequest.setDestinationInExternalFilesDir(UtilBase.getContext(), subDirectory, fullFileNameFromUrl(url));
            }
            downloadRequest.setMimeType(mimeTypeOf(uri));
            enqueueDownload(downloadRequest, callback);
        } catch (Exception e) {
            if (callback != null) {
                callback.onDownloadCallback(null, e);
            }
        }
    }

    /**
     * Start the download service for the current request
     * @param request Defined download request
     * @param callback Download completion listener
     */
    protected static void enqueueDownload(DownloadManager.Request request, DownloadUtil.DownloadCallback callback) {
        DownloadManager downloadManager = ResourceFetch.getSystemService(DownloadManager.class);
        final long downloadId = downloadManager.enqueue(request);
        if (callback != null) {
            UtilBase.getContext().registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    context.unregisterReceiver(this);
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (downloadId == id) {
                        DownloadManager manager = ResourceFetch.getSystemService(DownloadManager.class);
                        Uri uri = manager.getUriForDownloadedFile(downloadId);
                        callback.onDownloadCallback(FileUtil.fileFromUri(uri), null);
                    } else {
                        callback.onDownloadCallback(null, new Exception("Download Error"));
                    }
                }
            }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    public interface DownloadCallback {
        void onDownloadCallback(@Nullable File file, @Nullable Exception e);
    }
}
