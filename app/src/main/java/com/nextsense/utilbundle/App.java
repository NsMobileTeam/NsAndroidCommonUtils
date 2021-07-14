package com.nextsense.utilbundle;

import android.app.Application;

import com.nextsense.nsutils.UtilBase;
import com.nextsense.nsutils.commons.CommonUtils;
import com.nextsense.nsutils.commons.NsLog;

import java.util.Locale;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UtilBase.init(this, R.string.fileProviderName, new Locale("mk"));
        NsLog.setOnlineLogger((date, title, body) -> CommonUtils.toast(body));
    }
}
