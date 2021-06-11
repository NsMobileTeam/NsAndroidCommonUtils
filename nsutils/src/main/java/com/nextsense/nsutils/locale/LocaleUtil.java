package com.nextsense.nsutils.locale;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;

import com.nextsense.nsutils.UtilBase;
import com.nextsense.nsutils.commons.CommonUtils;
import com.nextsense.nsutils.commons.ResourceFetch;
import com.nextsense.nsutils.storage.PreferencesHelper;

import java.util.Locale;

public class LocaleUtil {
    private static final String LOCALE_PREF_NAME = "PreferredLocaleSetting";
    private static final PreferencesHelper LOCALE_PREFERENCE = PreferencesHelper.get("AppLocalePreference");

    public static void initAppLocale(@Nullable Locale defaultLocale) {
        setLocale(UtilBase.getContext(), getPreferredLocale(defaultLocale));
    }

    public static void applyCurrentLocale(Context context) {
        setLocale(context, getCurrentAppLocale());
    }

    public static void setLocale(Activity activity, String languageCode) {
        setLocale(UtilBase.getContext(), languageCode);
        setLocale((Context) activity, languageCode);
        activity.recreate();
    }

    public static void setLocale(Context context, String languageCode) {
        setLocale(context, languageCode, null);
    }

    public static void setLocale(Context context, String languageCode, @Nullable String region) {
        Locale locale = region != null ? new Locale(languageCode, region) : new Locale(languageCode);
        setLocale(context, locale);
    }

    public static void setLocale(Context context, Locale locale) {
        Resources res = context.getResources();
        Configuration configuration = new Configuration();
        DisplayMetrics dm = res.getDisplayMetrics();

        if (CommonUtils.minApiLevel(Build.VERSION_CODES.N)) {
            configuration.setLocale(locale);
            LocaleList localeList = new LocaleList(locale);
            configuration.setLocales(localeList);
            LocaleList.setDefault(localeList);
        } else {
            configuration.setLocale(locale);
        }

        Locale.setDefault(locale);
        res.updateConfiguration(configuration, dm);
        configuration.setLayoutDirection(locale);
        LOCALE_PREFERENCE.saveObject(LOCALE_PREF_NAME, locale);
        context.createConfigurationContext(configuration);
    }

    public static Locale getPreferredLocale(@Nullable Locale defaultLocale) {
        Locale currentLocale = LOCALE_PREFERENCE.getObject(LOCALE_PREF_NAME, Locale.class);
        if(currentLocale == null) {
            return defaultLocale != null ? defaultLocale : Locale.getDefault();
        }

        return currentLocale;
    }

    public static Locale getCurrentAppLocale() {
        if (CommonUtils.minApiLevel(Build.VERSION_CODES.N)) {
            return ResourceFetch.getConfiguration().getLocales().get(0);
        }

        return ResourceFetch.getConfiguration().locale;
    }
}
