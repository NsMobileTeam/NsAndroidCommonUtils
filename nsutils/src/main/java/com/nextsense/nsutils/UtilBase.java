package com.nextsense.nsutils;

import android.content.Context;

public class UtilBase {
    private static UtilBase base;
    private final Context appContext;
    private final IFileProviderAuthority listener;

    /**
     * Initialise the library singleton
     * @param appContext Current app context
     */
    public static void init(Context appContext, IFileProviderAuthority listener) {
        base = new UtilBase(appContext, listener);
    }


    /**
     * New UtilBase with an app context
     * @param appContext Current app context
     */
    private UtilBase(Context appContext, IFileProviderAuthority listener) {
        this.appContext = appContext;
        this.listener = listener;
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
        return base.listener.getFileProviderAuthority();
    }

    public interface IFileProviderAuthority {
        String getFileProviderAuthority();
    }
}
