package com.nextsense.nsutils;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.nextsense.nsutils.locale.LocaleUtil;

import java.util.Locale;

public class UtilBase {
    private static UtilBase base;
    private final Context appContext;
    private final String fileAuthorityName;

    /**
     * Initialise the library singleton
     * @param appContext Current app context
     */
    public static void init(Context appContext, @StringRes int fileAuthorityName, @Nullable Locale defaultLocale) {
        init(appContext, appContext.getString(fileAuthorityName), defaultLocale);
    }

    /**
     * Initialise the library singleton
     * @param appContext Current app context
     */
    public static void init(Context appContext, String fileAuthorityName, @Nullable Locale defaultLocale) {
        base = new UtilBase(appContext, fileAuthorityName, defaultLocale);
        LocaleUtil.initAppLocale(defaultLocale);
    }

    /**
     * New UtilBase with an app context
     * @param appContext Current app context
     */
    private UtilBase(Context appContext, String fileAuthorityName, @Nullable Locale defaultLocale) {
        this.appContext = appContext;
        this.fileAuthorityName = fileAuthorityName;
    }

    /**
     * Get the current app context
     * @return  The current app context
     */
    public static Context getContext() {
        return base.appContext;
    }

    /**
     * Get the FileProviderAuthority from the app
     * @return name of the FileProviderAuthority
     */
    public static String getFileProviderAuthority() {
        return base.fileAuthorityName;
    }
}
