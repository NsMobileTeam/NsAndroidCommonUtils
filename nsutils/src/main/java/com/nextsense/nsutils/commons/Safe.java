package com.nextsense.nsutils.commons;

import androidx.annotation.NonNull;

import com.nextsense.nsutils.R;

@SuppressWarnings("unused")
public class Safe {
    /**
     * Null-safe string value of any object
     * @param value any object
     * @param <T> class of object
     * @return string value of the object
     */
    public static @NonNull <T> String text(T value) {
        return text(value, ResourceFetch.getString(R.string.dummy_char));
    }

    /**
     * Null-safe string value of any object returns default object if null
     * @param value any object
     * @param fallbackValue default value if failed to get string from the object
     * @param <T> class of object
     * @return string value of the object
     */
    public static @NonNull <T> String text(T value, @NonNull String fallbackValue) {
        try {
            if(value != null && !String.valueOf(value).isEmpty()) {
                return String.valueOf(value);
            }
        } catch (Exception ignore) { }

        return fallbackValue;
    }
}
