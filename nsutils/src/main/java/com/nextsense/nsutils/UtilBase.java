package com.nextsense.nsutils;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.nextsense.nsutils.locale.LocaleUtil;

import java.util.Locale;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

@SuppressWarnings("unused")
public class UtilBase {
    private static UtilBase base;
    private final Context appContext;
    private final String fileAuthorityName;

    /**
     * Initialise the library singleton
     * @param appContext current application context
     * @param fileAuthorityName string resource of the file authority name
     */
    public static void init(Context appContext, @StringRes int fileAuthorityName) {
        init(appContext, appContext.getString(fileAuthorityName), null);
    }

    /**
     * Initialise the library singleton
     * @param appContext current application context
     * @param fileAuthorityName string resource of the file authority name
     * @param defaultLocale preferred default locale (if null the device default is selected)
     */
    public static void init(Context appContext, @StringRes int fileAuthorityName, @Nullable Locale defaultLocale) {
        init(appContext, appContext.getString(fileAuthorityName), defaultLocale);
    }

    /**
     * Initialise the library singleton
     * @param appContext current application context
     * @param fileAuthorityName string object of the file authority name
     * @param defaultLocale preferred default locale (if null the device default is selected)
     */
    public static void init(Context appContext, String fileAuthorityName, @Nullable Locale defaultLocale) {
        base = new UtilBase(appContext, fileAuthorityName);
        if(defaultLocale != null) {
            LocaleUtil.initAppLocale(defaultLocale);
        }
    }

    /**
     * New UtilBase with an app context
     * @param appContext Current app context
     * @param fileAuthorityName string object of the file authority name
     */
    private UtilBase(Context appContext, String fileAuthorityName) {
        this.appContext = appContext;
        this.fileAuthorityName = fileAuthorityName;
    }

    /**
     * Get the current app context
     * @return the current app context
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

    /**
     * Setup default calligraphy setup
     */
    public static void setupCalligraphy() {
        setupCalligraphy(null);
    }

    /**
     * Setup default calligraphy setup
     * @param defaultFontAssetPath default app font, parameter is nullable
     */
    public static void setupCalligraphy(@Nullable String defaultFontAssetPath) {
        CalligraphyConfig.Builder configBuilder = new CalligraphyConfig.Builder();
        configBuilder.setFontAttrId(R.attr.fontPath);
        if(defaultFontAssetPath != null) {
            configBuilder.setDefaultFontPath(defaultFontAssetPath);
        }

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(configBuilder.build())).build());
    }
}
