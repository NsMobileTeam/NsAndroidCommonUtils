package com.nextsense.nsutils.storage;

public class StorageManager {
    public static final StorageBundle MAIN = get("MainBundle");

    /**
     * Get or create a new sharedPreference bundle by Name
     * @param bundleName Name of the sharedPreference
     * @return StorageBundle for the specified sharedPreference
     */
    public static StorageBundle get(String bundleName) {
        return new StorageBundle(bundleName);
    }
}
