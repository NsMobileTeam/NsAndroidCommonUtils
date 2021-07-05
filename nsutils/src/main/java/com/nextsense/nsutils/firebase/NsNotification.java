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

import java.io.Serializable;

public abstract class NsNotification implements Serializable {
    public static final String BUNDLE_NOTIFICATION_KEY = "PushNotification";

    public abstract int id();
    public @NonNull abstract String title();
    public @NonNull abstract String message();
    public @Nullable @DrawableRes abstract Integer icon();
    public @Nullable abstract Bitmap thumbnail();
    public @Nullable abstract PendingIntent getPendingIntent(Context context);
    public @Nullable abstract RemoteViews getContentView(String packageName);

    public PendingIntent createPendingIntent(Context context, Class<? extends Activity> activityClass) {
        Intent intent = new Intent(context, activityClass);
        intent.putExtra(BUNDLE_NOTIFICATION_KEY,this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
