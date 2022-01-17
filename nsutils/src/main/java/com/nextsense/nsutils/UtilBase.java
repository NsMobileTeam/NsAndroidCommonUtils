package com.nextsense.nsutils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

@SuppressWarnings("unused")
public class UtilBase {
    private static UtilBase base;
    private static ContextConfigChangeListener contextListener;
    private Context appContext;
    private final String fileAuthorityName;

    /**
     * Initialise the library singleton
     * @param appContext current application context
     * @param fileAuthorityName string resource of the file authority name
     */
    public static void init(Context appContext, @StringRes int fileAuthorityName, @NonNull ContextConfigChangeListener configChangeListener) {
        init(appContext, appContext.getString(fileAuthorityName), configChangeListener);
    }

    /**
     * Initialise the library singleton
     * @param appContext current application context
     * @param fileAuthorityName string object of the file authority name
     */
    public static void init(Context appContext, String fileAuthorityName, @NonNull ContextConfigChangeListener configChangeListener) {
        base = new UtilBase(appContext, fileAuthorityName, configChangeListener);
    }

    /**
     * New UtilBase with an app context
     * @param appContext Current app context
     * @param fileAuthorityName string object of the file authority name
     */
    private UtilBase(Context appContext, String fileAuthorityName, @NonNull ContextConfigChangeListener externalListener) {
        this.appContext = appContext;
        this.fileAuthorityName = fileAuthorityName;
        UtilBase.contextListener = applicationContext -> {
            UtilBase.this.appContext = applicationContext;
            externalListener.onNewContext(applicationContext);
        };
    }

    /**
     * Update the appContext on configuration changed
     * @param appContext new Application Context
     */
    public static void updateAppContext(Context appContext) {
        contextListener.onNewContext(appContext);
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

    public interface ContextConfigChangeListener {
        void onNewContext(Context context);
    }
}
