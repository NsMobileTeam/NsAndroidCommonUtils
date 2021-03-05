package com.nextsense.utilbundle;

import android.app.Application;

import com.nextsense.nsutils.UtilBase;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UtilBase.init(this, this::getFileProviderAuthority);
    }

    private String getFileProviderAuthority() {
        return getString(R.string.fileProviderName);
    }
}
