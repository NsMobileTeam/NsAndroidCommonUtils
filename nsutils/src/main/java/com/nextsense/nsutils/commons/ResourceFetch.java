package com.nextsense.nsutils.commons;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.nextsense.nsutils.UtilBase;

import java.util.Locale;

@SuppressWarnings("unused")
public class ResourceFetch {

    /**
     * Get string from its resource id
     * @param stringId String resource id
     * @return String value of the resource
     */
    public static String getString(@StringRes int stringId) {
        return UtilBase.getContext().getString(stringId);
    }

    /**
     * Get localised string from its resource id
     * @param stringId String resource id
     * @param locale Desired locale
     * @return Localised string value of the resource
     */
    public static String getString(@StringRes int stringId, @Nullable Locale locale) {
        if(locale != null) {
            Configuration config = new Configuration(UtilBase.getContext().getResources().getConfiguration());
            config.setLocale(locale);
            Resources resources = new Resources(UtilBase.getContext().getAssets(), Resources.getSystem().getDisplayMetrics(), config);
            return resources.getString(stringId);
        }

        return getString(stringId);
    }

    /**
     * Get a Drawable object from its resource id
     * @param drawableId Drawable resource id
     * @return Drawable object
     */
    public static Drawable getDrawable(@DrawableRes int drawableId) {
        return ContextCompat.getDrawable(UtilBase.getContext(), drawableId);
    }

    /**
     * Get a Bitmap from a Drawable resource id
     * @param drawableId Drawable resource id
     * @return Bitmap image
     */
    public static Bitmap getDrawableBitmap(@DrawableRes int drawableId) {
        return CommonUtils.drawableToBitmap(getDrawable(drawableId));
    }

    /**
     * Get a color integer from its resource id
     * @param colorId Color resource id
     * @return A color integer
     */
    @ColorInt
    public static int getColor(@ColorRes int colorId) {
        return ContextCompat.getColor(UtilBase.getContext(), colorId);
    }

    /**
     * Get the app resources
     * @return The app resources
     */
    public static Resources getResources() {
        return UtilBase.getContext().getResources();
    }

    /**
     * Get the app configuration
     * @return The app configuration
     */
    public static Configuration getConfiguration() {
        return getResources().getConfiguration();
    }

    /**
     * Get a System Service by the class object of the desired system service
     * @param serviceClass Class object of the desired system service
     * @return instance of the desired system service
     */
    public static <T> T getSystemService(Class<T> serviceClass) {
        return ContextCompat.getSystemService(UtilBase.getContext(), serviceClass);
    }
}
