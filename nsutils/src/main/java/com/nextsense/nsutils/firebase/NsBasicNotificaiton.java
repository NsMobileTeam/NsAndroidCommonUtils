package com.nextsense.nsutils.firebase;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.messaging.RemoteMessage;
import com.nextsense.nsutils.commons.Safe;

public class NsBasicNotificaiton extends NsNotification {
    public NsBasicNotificaiton(RemoteMessage.Notification notification) {
        setRemoteNotification(notification);
    }

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

    @Nullable
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
}
