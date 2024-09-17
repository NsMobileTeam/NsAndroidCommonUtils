package com.nextsense.utilbundle;

import android.app.Application;
import android.content.Context;

import com.nextsense.nsutils.UtilBase;
import com.nextsense.nsutils.commons.CommonUtils;
import com.nextsense.nsutils.commons.Note;
import com.nextsense.nsutils.uiBaseElements.NsLoadingView;

import java.util.Locale;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UtilBase.init(this, R.string.fileProviderName, context -> { });
        Note.setOnlineLogger((date, title, body) -> CommonUtils.toast(body));
        NsLoadingView.setLayout(R.layout.layout_default_loader);
    }
}
