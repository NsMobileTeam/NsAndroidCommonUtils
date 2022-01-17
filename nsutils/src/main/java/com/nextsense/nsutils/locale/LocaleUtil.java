package com.nextsense.nsutils.locale;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.nextsense.nsutils.UtilBase;
import com.nextsense.nsutils.storage.NsPrefs;

import java.util.Locale;

public class LocaleUtil {
    private static final String LOCALE_PREF_NAME = "PreferredLocaleSetting";
    private static final NsPrefs LOCALE_PREFERENCE = NsPrefs.get("AppLocalePreference");

    /**
     * Initialises and sets application locale
     * @param defaultLocale preferred locale
     */
    public static void initAppLocale(@Nullable Locale defaultLocale) {
        setLocale(UtilBase.getContext(), getCurrentLocale(defaultLocale));
    }

    /**
     * Apply locale to a context
     * @param context reference
     */
    public static void applyCurrentLocale(Context context) {
        setLocale(context, getCurrentLocale());
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
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        Context newAppContext = context.getApplicationContext().createConfigurationContext(config);
        UtilBase.updateAppContext(newAppContext);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        LOCALE_PREFERENCE.saveObject(LOCALE_PREF_NAME, locale);
    }

    /**
     * Gets the preferred locale from preferences if saved, the argument provided or device locale
     * @return object of the preferred locale
     */
    public static Locale getCurrentLocale() {
        return getCurrentLocale(null);
    }

    /**
     * Gets the preferred locale from preferences if saved, the argument provided or device locale
     * @param defaultLocale optional fail-safe reference of any locale
     * @return object of the preferred locale
     */
    public static Locale getCurrentLocale(@Nullable Locale defaultLocale) {
        Locale currentLocale = LOCALE_PREFERENCE.getObject(LOCALE_PREF_NAME, Locale.class);
        if(currentLocale == null) {
            return defaultLocale != null ? defaultLocale : Locale.getDefault();
        }

        return currentLocale;
    }
}
