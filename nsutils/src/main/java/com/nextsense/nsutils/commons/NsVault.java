package com.nextsense.nsutils.commons;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.nextsense.nsutils.R;
import com.nextsense.nsutils.UtilBase;
import com.nextsense.nsutils.listeners.IUniversalListener;
import com.nextsense.nsutils.storage.NsPrefs;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

@RequiresApi(api = Build.VERSION_CODES.M)
@SuppressWarnings("unused")
public class NsVault {
    private static final String KEY_STORE = "AndroidKeyStore";
    private static final String NON_AUTH_ALIAS = "nonAuthorisedKey";
    private static final String USER_AUTH_ALIAS = "userAuthorisedKey";
    private static final int AUTH_VALIDITY_DURATION = 5;//seconds
    private static final int IV_SIZE = 16;
    private static final String AES_ALGORITHM = "AES/CBC/PKCS7Padding";

    private static final String PREF_IV_SPEC = "%sIvSpec";
    private static final String PREF_SECURED_DATA = "%sSecured";

    private static NsVault instance;

    /**
     * Get the system's keyguard service
     * @return the keyguard service
     */
    public static KeyguardManager keyguard() {
        return ((KeyguardManager)UtilBase.getContext().getSystemService(Context.KEYGUARD_SERVICE));
    }

    /**
     * Encrypt and save serializable data with the system's Secure Element requiring user authentication
     * @param key address of the secured object in the persistent storage
     * @param plainData serializable data
     * @param listener used to require user authentication and return status
     * @param <T> type of the serializable object
     */
    public static <T> void authSave(String key, T plainData, @NonNull IVaultListener<Boolean> listener) {
        get().save(USER_AUTH_ALIAS, true, key,plainData,listener);
    }

    /**
     * Decrypt saved encrypted data after user authentication
     * @param key address of the secured object in the persistent storage
     * @param classOfObject class of the encrypted object
     * @param listener used to require user authentication and return the decrypted object
     * @param <T> type of the serializable object
     */
    public static <T> void authLoad(String key, Class<T> classOfObject, @NonNull IVaultListener<T> listener) {
        get().load(USER_AUTH_ALIAS, true, key, classOfObject, listener);
    }

    /**
     * Encrypt and save serializable data with the system's Secure Element
     * @param key address of the secured object in the persistent storage
     * @param plainData serializable data
     * @param <T> type of the serializable object
     */
    public static <T> void save(String key, T plainData) {
        try {
            save(NON_AUTH_ALIAS, false, key, plainData);
        } catch (Exception ignore) { }
    }

    /**
     * Decrypt saved encrypted data
     * @param key address of the secured object in the persistent storage
     * @param classOfObject class of the encrypted object
     * @param <T> type of the serializable object
     * @return decrypted object of type T
     */
    public static @Nullable <T> T load(String key, Class<T> classOfObject) {
        try {
            return load(NON_AUTH_ALIAS, false, key, classOfObject);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Delete saved data entry
     * @param key address of the secured object in the persistent storage
     */
    public static void delete(String key) {
        secPrefs().delete(key);
    }

    private static NsVault get() {
        if(instance == null) {
            instance = new NsVault();
        }

        return instance;
    }

    private static SecretKey getKey(String alias, boolean userAuthenticated) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KEY_STORE);
        keyStore.load(null);

        if(!keyStore.containsAlias(alias)) {
            return genKey(alias, userAuthenticated);
        }

        return ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();
    }

    private static SecretKey genKey(String alias, boolean userAuthenticated) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyGenParameterSpec.Builder aesKeyBuilder = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
        aesKeyBuilder.setBlockModes(KeyProperties.BLOCK_MODE_CBC);
        aesKeyBuilder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
        aesKeyBuilder.setUserAuthenticationValidityDurationSeconds(AUTH_VALIDITY_DURATION);
        aesKeyBuilder.setRandomizedEncryptionRequired(false);
        aesKeyBuilder.setKeySize(256);
        aesKeyBuilder.setUserAuthenticationRequired(userAuthenticated);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            aesKeyBuilder.setUserPresenceRequired(userAuthenticated);
        }

        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEY_STORE);
        keyGenerator.init(aesKeyBuilder.build());
        return keyGenerator.generateKey();
    }

    private <T> void save(String alias, boolean userAuth, String key, T plainData, @NonNull IVaultListener<Boolean> listener) {
        try {
            save(alias, userAuth, key, plainData);
            listener.onFinished(true, null);
        } catch (Exception e) {
            if(isAuthException(e)) {
                requestAuthentication(listener, result -> {
                    if(result instanceof Boolean && (Boolean) result) {
                        save(alias, userAuth, key, plainData, listener);
                    } else {
                        listener.onFinished(null, e);
                    }
                });
            } else {
                listener.onFinished(null, e);
            }
        }
    }

    private <T> void load(String alias, boolean userAuth, String key, Class<T> classOfObject, @NonNull IVaultListener<T> listener) {
        try {
            listener.onFinished(load(alias, userAuth, key, classOfObject), null);
        } catch (Exception e) {
            if (isAuthException(e)) {
                requestAuthentication(listener, result -> {
                    if (result instanceof Boolean && (Boolean) result) {
                        load(alias, userAuth, key, classOfObject, listener);
                    } else {
                        listener.onFinished(null, e);
                    }
                });
            } else {
                listener.onFinished(null, e);
            }
        }
    }

    private static  <T> void save(String alias, boolean userAuth, String key, T plainData) throws Exception {
        String json = new Gson().toJson(plainData);
        IvParameterSpec iv = EncryptionUtil.randomIv(IV_SIZE);
        byte[] cipherText = EncryptionUtil.encrypt(AES_ALGORITHM, getKey(alias, userAuth), iv, json.getBytes());
        secPrefs().saveBytes(String.format(PREF_IV_SPEC, key), iv.getIV());
        secPrefs().saveBytes(String.format(PREF_SECURED_DATA, key), cipherText);
    }
    
    private static <T> T load(String alias, boolean userAuth, String key, Class<T> classOfObject) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(secPrefs().getBytes(String.format(PREF_IV_SPEC, key)));
        if (iv.getIV().length < IV_SIZE) throw new SecurityException(String.format("Wrong size iv for the %s key", key));
        byte[] cipherText = secPrefs().getBytes(String.format(PREF_SECURED_DATA, key));
        byte[] decrypted = EncryptionUtil.decrypt(AES_ALGORITHM, getKey(alias, userAuth), iv, cipherText);
        return CommonUtils.fromJson(new String(decrypted, StandardCharsets.UTF_8), classOfObject);
    }

    private static NsPrefs secPrefs() {
        return NsPrefs.get(String.format("%sSecureData", CommonUtils.getAppName()));
    }

    public boolean isAuthException(Exception e) {
        return e instanceof UserNotAuthenticatedException ||
                (e.getCause() instanceof KeyStoreException && e.getCause().getMessage() != null && e.getCause().getMessage().contains("not authenticated"));
    }

    private void requestAuthentication(@NonNull IVaultListener<?> launcher, IUniversalListener<Object> listener) {
        launcher.authenticate(new NsActivityContract.IContractInterface<Object, Object>() {
            @Override
            public Intent createIntent(@NonNull Context context, Object input) {
                return NsVault.keyguard().createConfirmDeviceCredentialIntent(ResourceFetch.getString(R.string.vault_authorize), ResourceFetch.getString(R.string.vault_auth_description));
            }

            @Override
            public Object parseResult(int resultCode, Intent intent) {
                return resultCode == Activity.RESULT_OK;
            }

            @Override
            public void onContractResult(Object result) {
                listener.onSuccess(result);
            }
        });
    }

    public interface IVaultListener<T> {
        void authenticate(NsActivityContract.IContractInterface<Object, Object> listener);
        void onFinished(@Nullable T result, @Nullable Exception error);
    }
}
