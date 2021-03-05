package com.nextsense.nsutils.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.nextsense.nsutils.UtilBase;

class StorageBundle {
    private final SharedPreferences preferences;

    /**
     * Get or create a new sharedPreference bundle by Name
     * @param name Name of the sharedPreference
     */
    public StorageBundle(String name) {
        preferences = UtilBase.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * Save a boolean value for a String key in the sharedPreferences
     * @param key Value of the entry key
     * @param bool Value of the boolean
     */
    public void saveBoolean(String key, boolean bool) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, bool);
        editor.apply();
    }


    /**
     * Save an int value for a String key in the sharedPreferences
     * @param key Value of the entry key
     * @param n Value of the int
     */
    public void saveInt(String key, int n) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, n);
        editor.apply();
    }


    /**
     * Save a long value for a String key in the sharedPreferences
     * @param key Value of the entry key
     * @param n Value of the long
     */
    public void saveLong(String key, long n) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, n);
        editor.apply();
    }


    /**
     * Save a String value for a String key in the sharedPreferences
     * @param key Value of the entry key
     * @param text Value of the String
     */
    public void saveString(String key, String text) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, text);
        editor.apply();
    }


    /**
     * Save a serializable Object value for a String key in the sharedPreferences
     * @param key Value of the entry key
     * @param object Value of the Object
     */
    public void saveObject(String key, Object object) {
        String json = new Gson().toJson(object);
        saveString(key, json);
    }

    /**
     * Get a boolean value from its sharedPreference key
     * @param key Value of the entry key
     * @param defValue Default value if key does't exist
     * @return The boolean value of the desired sharedPreference key
     */
    public boolean getBoolean(String key, boolean defValue) {
        if (preferences.contains(key)) {
            return preferences.getBoolean(key, defValue);
        }
        return defValue;
    }

    /**
     * Get an Integer object from the sharedPreferences by its key
     * @param key Value of the entry key
     * @return The Integer object of the desired sharedPreference key or null if key doesn't exist
     */
    public Integer getInt(String key) {
        if (preferences.contains(key)) {
            return preferences.getInt(key, 0);
        }
        return null;
    }

    /**
     * Get a Long object from the sharedPreferences by its key
     * @param key Value of the entry key
     * @return The object of the desired sharedPreference key or null if key doesn't exist
     */
    public Long getLong(String key) {
        if (preferences.contains(key)) {
            return preferences.getLong(key, 0);
        }
        return null;
    }

    /**
     * Get a String object from the sharedPreferences by its key
     * @param key Value of the entry key
     * @return The String of the desired sharedPreference key or null if key doesn't exist
     */
    public String getString(String key) {
        if (preferences.contains(key)) {
            return preferences.getString(key, null);
        }
        return null;
    }

    /**
     * Get any serializable object from shared preferences by its key
     * @param key Value of the entry key
     * @return The Object of the desired sharedPreference key or null if key doesn't exist
     */
    public <T> T getObject(String key, Class<T> classObject) {
        try {
            String json = getString(key);
            return new Gson().fromJson(json, classObject);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Delete a list of keys from the sharedPreferences
     * @param keys list of Entry keys
     */
    public void delete(String... keys) {
        SharedPreferences.Editor editor = preferences.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
    }
}

