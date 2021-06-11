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

    /**
     * Initialises and sets application locale
     * @param defaultLocale preferred locale
     */
    public static void initAppLocale(@Nullable Locale defaultLocale) {
        setLocale(UtilBase.getContext(), getPreferredLocale(defaultLocale));
    }

    /**
     * Apply locale to a context
     * @param context reference
     */
    public static void applyCurrentLocale(Context context) {
        setLocale(context, getCurrentAppLocale());
    }

    /**
     * Apply locale to an Activity
     * @param activity reference
     * @param languageCode standardised locale short code
     */
    public static void setLocale(Activity activity, String languageCode) {
        setLocale(UtilBase.getContext(), languageCode);
        setLocale((Context) activity, languageCode);
        activity.recreate();
    }

    /**
     * Apply locale to a context
     * @param context reference
     * @param languageCode standardised locale short code
     */
    public static void setLocale(Context context, String languageCode) {
        setLocale(context, languageCode, null);
    }

    /**
     * Apply locale to a context
     * @param context reference
     * @param languageCode standardised locale short code
     * @param region standardised region short code
     */
    public static void setLocale(Context context, String languageCode, @Nullable String region) {
        Locale locale = region != null ? new Locale(languageCode, region) : new Locale(languageCode);
        setLocale(context, locale);
    }

    /**
     * Apply locale to a context
     * @param context reference
     * @param locale reference
     */
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

    /**
     * Gets the prefered locale from preferences if saved, the argument provided or device locale
     * @param defaultLocale optional fail-safe reference of any locale
     * @return object of the preferred locale
     */
    public static Locale getPreferredLocale(@Nullable Locale defaultLocale) {
        Locale currentLocale = LOCALE_PREFERENCE.getObject(LOCALE_PREF_NAME, Locale.class);
        if(currentLocale == null) {
            return defaultLocale != null ? defaultLocale : Locale.getDefault();
        }

        return currentLocale;
    }

    /**
     * Gets the currently set locale from app configuration
     * @return object of the currently set locale
     */
    public static Locale getCurrentAppLocale() {
        if (CommonUtils.minApiLevel(Build.VERSION_CODES.N)) {
            return ResourceFetch.getConfiguration().getLocales().get(0);
        }

        return ResourceFetch.getConfiguration().locale;
    }
}
