package com.nextsense.nsutils.baseElements;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class NsDialogResult extends HashMap<String, String> {
    /**
     * Dialog response model
     * @param tagName name of the view tag
     * @param defaultValue return if tag not found or null
     * @return value of the tag
     */
    public String get(String tagName, @Nullable String defaultValue) {
        String result = get(tagName);
        if(result != null) {
            return get(tagName);
        }

        return defaultValue;
    }
}
