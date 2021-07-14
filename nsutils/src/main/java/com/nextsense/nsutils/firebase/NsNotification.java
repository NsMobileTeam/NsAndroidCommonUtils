package com.nextsense.nsutils.firebase;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.messaging.RemoteMessage;
import com.nextsense.nsutils.commons.EncryptionUtil;
import com.nextsense.nsutils.commons.Safe;

import java.io.Serializable;

@SuppressWarnings("unused")
public abstract class NsNotification implements Serializable {
    public static final String BUNDLE_NOTIFICATION_KEY = "PushNotification";

    private final int id;
    private RemoteMessage.Notification remoteNotification;

    public NsNotification() {
        this.id = EncryptionUtil.secureRandomInstance(false, false,false).nextInt();
    }

    /**
     * Abstract method for retrieval of the notification's title
     * @return title text
     */
    public @NonNull abstract String title();

    /**
     * Abstract method for retrieval of the notification's message
     * @return message text
     */
    public @NonNull abstract String message();

    /**
     * Abstract method for retrieval of the notification's icon
     * @return an icon resource id
     */
    public @NonNull @DrawableRes abstract Integer icon();

    /**
     * Abstract method for retrieval of the notification's body icon
     * @return a bitmap of the icon
     */
    public @Nullable abstract Bitmap thumbnail();

    /**
     * Abstract method for retrieval of the notification's pending intent
     * @return a pending intent
     */
    public @Nullable abstract PendingIntent getPendingIntent(Context context);

    /**
     * Abstract method for retrieval of the notification custom view
     * @return custom remote view
     */
    public @Nullable abstract RemoteViews getContentView(String packageName);

    /**
     * Gets the randomised notification id
     * @return random notification id
     */
    public int id() {
        return id;
    }

    /**
     * Create a pending intent for this Notification
     * @param context for the creation of the intent
     * @param activityClass class of the launcher activity
     * @return a fully constructend pending intent
     */
    public PendingIntent getPendingIntent(Context context, Class<? extends Activity> activityClass) {
        Intent intent = new Intent(context, activityClass);
        intent.putExtra(BUNDLE_NOTIFICATION_KEY,this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Set the remotely created notification
     * @param remoteNotification the remote notification
     */
    public void setRemoteNotification(RemoteMessage.Notification remoteNotification) {
        this.remoteNotification = remoteNotification;
    }

    /**
     * Gets the remotely created notification
     */
    public RemoteMessage.Notification getRemoteNotification() {
        return remoteNotification;
    }

    /**
     * Create an NsNotification embedded within the extras in an intent
     * @param intent any intent
     * @param <T> type of the NsNotification
     * @return an NsNotification of type T
     */
    @SuppressWarnings("unchecked")
    public static <T extends NsNotification> T fromBundle(Intent intent) {
        try {
            return (T) intent.getExtras().get(BUNDLE_NOTIFICATION_KEY);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Creates a default NsNotification from a remotely crated notification
     * @param remoteNotification the remote notification
     * @return a basic NsNotification
     */
    public static NsNotification getDefault(RemoteMessage.Notification remoteNotification) {
        NsNotification notification = new NsNotification() {
            @NonNull
            @Override
            public String title() {
                return Safe.text(getRemoteNotification().getTitle());
            }

            @NonNull
            @Override
            public String message() {
                return Safe.text(getRemoteNotification().getBody());
            }

            @NonNull
            @Override
            public Integer icon() {
                return android.R.drawable.sym_def_app_icon;
            }

            @Nullable
            @Override
            public Bitmap thumbnail() {
                return null;
            }

            @Nullable
            @Override
            public PendingIntent getPendingIntent(Context context) {
                return null;
            }

            @Nullable
            @Override
            public RemoteViews getContentView(String packageName) {
                return null;
            }
        };

        notification.setRemoteNotification(remoteNotification);
        return notification;
    }
}
