package com.nextsense.nsutils.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nextsense.nsutils.commons.CommonUtils;
import com.nextsense.nsutils.storage.NsPrefs;

import org.json.JSONObject;

@SuppressWarnings("unused")
public abstract class NsFirebaseHandler<T extends NsNotification> extends FirebaseMessagingService {
    private static final String FIREBASE_PREFS = CommonUtils.getAppName() + "FirebasePrefs";
    private static final String FIREBASE_TOKEN_KEY = "FirebaseToken";

    private NotificationManager notificationManager;
    private String channelId;

    public abstract void onNewToken();
    public abstract void onMessageReceived(T notification, @Nullable RemoteMessage.Notification cloudNotification);
    public abstract Class<T> getNotificationClass();
    public abstract NotificationChannel getChannel();

    public static String getToken() {
        return NsPrefs.get(FIREBASE_PREFS).getString(FIREBASE_TOKEN_KEY);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        NsPrefs.get(FIREBASE_PREFS).saveString(FIREBASE_TOKEN_KEY, s);
        onNewToken();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        onMessageReceived(CommonUtils.fromJson(new JSONObject(remoteMessage.getData()).toString(), getNotificationClass()), remoteMessage.getNotification());
    }

    public void publish(@NonNull T notification) {
        getNotificationManager().notify(notification.id(), getNotificationBuilder(notification).build());
    }

    private NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(getChannel());
                channelId = getChannel().getId();
            } else {
                channelId = "DEFAULT";
            }
        }

        return notificationManager;
    }

    private NotificationCompat.Builder getNotificationBuilder(@NonNull T notification) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setChannelId(channelId);
        builder.setContentTitle(notification.title());
        builder.setContentText(notification.message());
        builder.setWhen(System.currentTimeMillis());
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setAutoCancel(true);

        if(notification.icon() != null) {
            builder.setSmallIcon(notification.icon());
        }

        if(notification.thumbnail() != null) {
            builder.setLargeIcon(notification.thumbnail());
        }

        PendingIntent intent = notification.getPendingIntent(this);
        if(intent != null) {
            builder.setContentIntent(intent);
        }

        RemoteViews contentView = notification.getContentView(getPackageName());
        if(contentView != null) {
            builder.setContent(contentView);
        }

        return builder;
    }

    public void cancel(T notification) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notification.id());
    }
}

