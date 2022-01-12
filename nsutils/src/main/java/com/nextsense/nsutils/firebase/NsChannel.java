package com.nextsense.nsutils.firebase;

import android.app.NotificationChannel;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class NsChannel {
    private NotificationChannel notificationChannel;
    private final String channelId;

    public NsChannel(@NonNull String channelId, @NonNull String channelName, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, channelName, importance);
        }

        this.channelId = channelId;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public NotificationChannel getNotificationChannel() {
        return notificationChannel;
    }

    public String getChannelId() {
        if(notificationChannel != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return notificationChannel.getId();
        }

        return channelId;
    }
}
