package com.nextsense.nsutils.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nextsense.nsutils.commons.CommonUtils;
import com.nextsense.nsutils.storage.NsPrefs;

import org.json.JSONObject;

@SuppressWarnings("unused")
public abstract class NsFirebaseHandler<T extends NsNotification> extends FirebaseMessagingService {
    private static final String FIREBASE_PREFS = CommonUtils.getAppName() + "FirebasePrefs";
    private static final String FIREBASE_TOKEN_CODE = "FirebaseToken";

    private NotificationManager notificationManager;
    private String channelId;

    /**
     * Abstract method called after a new token was issued and saved
     */
    public abstract void onNewToken();

    /**
     * Abstract method called after a new message was received
     * @param notification of type NsNotification
     */
    public abstract void onMessageReceived(@NonNull T notification);

    /**
     * Abstract method for retrieval of the class object of the T NsNotification
     * @return class object of the T NsNotification
     */
    public abstract @NonNull Class<T> getNotificationClass();

    /**
     * Abstract method for retrieval of the api safe NotificationChannel
     * @return An api safe NotificationChannel
     */
    public abstract NsChannel getChannel();

    /**
     * Get the currently available GMS token
     * @return token
     */
    public static String getToken() {
        return NsPrefs.get(FIREBASE_PREFS).getString(FIREBASE_TOKEN_CODE);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        NsPrefs.get(FIREBASE_PREFS).saveString(FIREBASE_TOKEN_CODE, s);
        onNewToken();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        T notification = CommonUtils.fromJson(new JSONObject(remoteMessage.getData()).toString(), getNotificationClass());
        if(notification != null) {
            notification.setRemoteNotification(remoteMessage.getNotification());
        } else {
            notification = (T) NsNotification.getDefault(remoteMessage.getNotification());
        }

        onMessageReceived(notification);
    }

    /**
     * Show notification of type NsNotification in the notification bar
     * @param notification extending NsNotification
     */
    public void publish(@NonNull T notification) {
        getNotificationManager().notify(notification.id(), getNotificationBuilder(notification).build());
    }

    private NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NsChannel nsChannel = getChannel();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(nsChannel.getNotificationChannel());
            }

            channelId = nsChannel.getChannelId();
        }

        return notificationManager;
    }

    private NotificationCompat.Builder getNotificationBuilder(@NonNull T notification) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setChannelId(channelId);
        builder.setContentTitle(notification.title());
        builder.setContentText(notification.message());
        builder.setSmallIcon(notification.icon());
        builder.setWhen(System.currentTimeMillis());
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setAutoCancel(true);

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

    /**
     * Cancel any NsNotification
     * @param notification of type NsNotification
     */
    private void cancel(T notification) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notification.id());
    }

    /**
     * Cancel any NsNotification
     * @param notification of type NsNotification
     */
    public static <T extends NsNotification> void cancel(Context context, T notification) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notification.id());
    }
}

