package com.nextsense.nsutils.storage;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.nextsense.nsutils.R;
import com.nextsense.nsutils.UtilBase;
import com.nextsense.nsutils.commons.CommonUtils;
import com.nextsense.nsutils.commons.EncryptionUtil;
import com.nextsense.nsutils.commons.ResourceFetch;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class NsPrefs {
    private static final String PREFIX_PREFERENCES = "%sPrefs";
    private static final String PREFIX_LOCKED_PREFERENCES = "%sLockPrefs";
    private static final String PREFIX_SECURE_PREFERENCES = "%sSecPrefs";
    private static final String PREFIX_MASTER_KEY = "%sMasterKey";

    private final SharedPreferences preferences;

    private NsPrefs(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    private NsPrefs(String name) {
        this.preferences = UtilBase.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * Get default app shared preference
     * @return a default preference
     */
    public static NsPrefs get() {
        return get(String.format(PREFIX_PREFERENCES, CommonUtils.getAppName()));
    }

    /**
     * Get or create a new sharedPreferences bundle by Name
     * @param name of the sharedPreferences
     * @return a preference by the name from the parameters
     */
    public static NsPrefs get(String name) {
        return new NsPrefs(name);
    }

    /**
     * Get or create a new EncryptedSharedPreferences bundle by Name locked by device locking mechanism
     * @param listener user authentication listener
     */
    public static void getLocked(IUserAuthListener listener) {
        getLocked(String.format(PREFIX_LOCKED_PREFERENCES, CommonUtils.getAppName()), listener);
    }

    /**
     * Get or create a new EncryptedSharedPreferences bundle by Name locked by device locking mechanism
     * @param name of the shared preference and name
     * @param listener user authentication listener
     */
    public static void getLocked(String name, IUserAuthListener listener) {
        try {
            String masterKeyAlias = String.format(PREFIX_MASTER_KEY, name);
            MasterKey.Builder builder = new MasterKey.Builder(UtilBase.getContext(), masterKeyAlias);
            builder.setKeyScheme(MasterKey.KeyScheme.AES256_GCM);
            builder.setUserAuthenticationRequired(true, 5);
            if (EncryptionUtil.hasStrongBox()) {
                builder.setRequestStrongBoxBacked(true);
            }
            SharedPreferences ePrefs = EncryptedSharedPreferences.create(
                    UtilBase.getContext(),
                    name,
                    builder.build(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            listener.onSecureInstance(new NsPrefs(ePrefs));
        } catch (Exception e) {
            if (isUserAuthException(e)) {
                listener.authenticate(new IContractInterface<Object, Object>() {
                    @Override
                    public Intent createIntent(@NonNull Context context, Object input) {
                        return keyguard().createConfirmDeviceCredentialIntent(ResourceFetch.getString(R.string.vault_authorize), ResourceFetch.getString(R.string.vault_auth_description));
                    }

                    @Override
                    public Object parseResult(int resultCode, @Nullable Intent intent) {
                        return resultCode == Activity.RESULT_OK;
                    }

                    @Override
                    public void onContractResult(Object result) {
                        try {
                            getLocked(name, listener);
                        } catch (Exception e) {
                            listener.onSecureInstance(null);
                        }
                    }
                });
            } else {
                listener.onSecureInstance(null);
            }
        }
    }

    /**
     * Get or create a new EncryptedSharedPreferences bundle by Name
     * @return encrypted shared preference
     */
    public static NsPrefs getSecure() {
        return getSecure(String.format(PREFIX_SECURE_PREFERENCES, CommonUtils.getAppName()));
    }

    /**
     * Get or create a new EncryptedSharedPreferences bundle by Name
     * @param name of the shared preference and name
     * @return encrypted shared preference
     */
    public static NsPrefs getSecure(String name) {
        try {
            String masterKeyAlias = String.format(PREFIX_MASTER_KEY, name);
            MasterKey.Builder builder = new MasterKey.Builder(UtilBase.getContext(), masterKeyAlias);
            builder.setKeyScheme(MasterKey.KeyScheme.AES256_GCM);
            SharedPreferences ePrefs = EncryptedSharedPreferences.create(
                    UtilBase.getContext(),
                    name,
                    builder.build(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            return new NsPrefs(ePrefs);
        } catch (Exception e) {
            return new NsPrefs(name);
        }
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
     * Save byte data for a String key in the sharedPreferences
     * @param key Value of the entry key
     * @param data Value of the byte data
     */
    public void saveBytes(String key, byte[] data) {
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, base64);
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
    public <T> void saveObject(String key, T object) {
        String json = CommonUtils.toJson(object);
        saveString(key, json);
    }

    /**
     * Save a serializable list for a String key in the sharedPreferences
     * @param key Value of the entry key
     * @param list of the Objects
     */
    public <T> void saveList(String key, ArrayList<T> list) {
        String json = CommonUtils.toJson(list);
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
     * Get byte data from its sharedPreference key
     * @param key Value of the entry key
     */
    public byte[] getBytes(String key) {
        if (preferences.contains(key)) {
            return Base64.decode(getString(key), Base64.DEFAULT);
        }
        return new byte[0];
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
        String json = getString(key);
        return CommonUtils.fromJson(json, classObject);
    }

    /**
     * Get any serializable list from shared preferences by its key
     * @param key Value of the entry key
     * @return The Object of the desired sharedPreference key or null if key doesn't exist
     */
    public <T> ArrayList<T> getList(String key, Class<T> classObject) {
        String json = getString(key);
        return CommonUtils.fromJsonArray(json, classObject);
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

    /**
     * Get the keyguard system service
     * @return keyguard service
     */
    public static KeyguardManager keyguard() {
        return ((KeyguardManager)UtilBase.getContext().getSystemService(Context.KEYGUARD_SERVICE));
    }

    private static boolean isUserAuthException(Exception e) {
        return (CommonUtils.minApiLevel(Build.VERSION_CODES.M) && e instanceof UserNotAuthenticatedException) ||
                (e.getCause() != null && e.getCause().getMessage() != null && e.getCause().getMessage().contains("not authenticated"));
    }

    public interface IUserAuthListener {
        void authenticate(IContractInterface<Object, Object> listener);
        void onSecureInstance(@Nullable NsPrefs secPrefs);
    }

    public interface IContractInterface<I,O> {
        Intent createIntent(@NonNull Context context, I input);
        O parseResult(int resultCode, @Nullable Intent intent);
        void onContractResult(O result);
    }
}

