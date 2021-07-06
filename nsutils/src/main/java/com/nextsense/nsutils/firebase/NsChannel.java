package com.nextsense.nsutils.firebase;

import android.app.NotificationChannel;
import android.os.Build;

public class NsChannel {
    private NotificationChannel notificationChannel = null;

    public NsChannel(String channelId, String channelName, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, channelName, importance);
        }
    }

    public NotificationChannel getNotificationChannel() {
        return notificationChannel;
    }
}
