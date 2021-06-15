package com.nextsense.nsutils.commons;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.nextsense.nsutils.UtilBase;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class PermissionUtil {
    private static final int BASE_REQUEST_CODE = 2393;
    private static final int REQUEST_CODE_COUNTER = 10000000;

    private static final Map<String, Integer> permissionMap = new HashMap<>();

    /**
     * Initialise the permission Map example: MapEntry(key: "android.permission.ACCESS_FINE_LOCATION", value: 2301)
     */
    private static void initIfEmpty() {
        if (permissionMap.isEmpty()) {
            Field[] fields = Manifest.permission.class.getDeclaredFields();
            int requestCode = BASE_REQUEST_CODE;
            for (Field f : fields) {
                if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())) {
                    try {
                        String key = (String) f.get(String.class);
                        permissionMap.put(key, requestCode += 19);
                    } catch (Exception ignore) {
                    }
                }
            }
        }
    }

    /**
     * Determine whether all the listed permissions are granted
     * @param permissions Array of any of the permissions defined in the Manifest.permission class
     * @return True if all the listed permissions are granted, otherwise returns false
     */
    public static boolean arePermissionsGranted(String... permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean arePermissionsGranted = true;
            for (String permission : permissions) {
                arePermissionsGranted &= (UtilBase.getContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
            return arePermissionsGranted;
        }
        return true;
    }

    /**
     * Request listed permissions from the context of an Activity
     * @param activity Instance of the Activity requesting the permissions
     * @param permissions Array of any of the permissions defined in the Manifest.permission class
     */
    public static void requestPermissions(Activity activity, String... permissions) {
        int requestCode = requestCodeFor(permissions);
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * Request listed permissions from the context of a Fragment
     * @param fragment Instance of the Fragment requesting the permissions
     * @param permissions Array of any of the permissions defined in the Manifest.permission class
     */
    public static void requestPermission(Fragment fragment, String... permissions) {
        int requestCode = requestCodeFor(permissions);
        fragment.requestPermissions(permissions, requestCode);
    }

    /**
     * Opens the app settings of the current app
     */
    public static void openAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", UtilBase.getContext().getPackageName(), null);
        intent.setData(uri);
        UtilBase.getContext().startActivity(intent);
    }

    /**
     * Gets a unique request code for the listed permissions
     * @param permissions Array of any of the permissions defined in the Manifest.permission class
     * @return unique request code for the listed permissions
     */
    public static int requestCodeFor(String... permissions) {
        initIfEmpty();
        int code = -1;
        for (String permission : permissions) {
            Integer requestBaseCode = permissionMap.get(permission);
            code += requestBaseCode == null ? 0 : requestBaseCode + REQUEST_CODE_COUNTER;
        }

        return code;
    }
}
