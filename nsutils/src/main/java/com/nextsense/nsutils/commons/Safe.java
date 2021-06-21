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
        try {
            if(value != null && !String.valueOf(value).isEmpty()) {
                return String.valueOf(value);
            }
        } catch (Exception ignore) { }

        return ResourceFetch.getString(R.string.dummy_char);
    }
}
