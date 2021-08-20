package com.nextsense.nsutils.commons;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.ChecksSdkIntAtLeast;
import androidx.annotation.ColorInt;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nextsense.nsutils.UtilBase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class CommonUtils {
    /**
     * Display a toast message
     * @param message The String message
     */
    public static void toast(String message) {
        toast(message, Toast.LENGTH_SHORT);
    }

    /**
     * Display a toast message with duration
     * @param message The String message
     * @param duration Toast duration of the message
     */
    public static void toast(String message, int duration) {
        duration = Math.max(Math.min(duration, Toast.LENGTH_LONG), Toast.LENGTH_SHORT);
        Toast.makeText(UtilBase.getContext(), message, duration).show();
    }

    /**
     * Hide the software keyboard
     * @param view Editable View Object on the current screen
     */
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) UtilBase.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Get the current display metrics
     * @return Current display metrics
     */
    public static DisplayMetrics getDisplayMetrics() {
        return UtilBase.getContext().getResources().getDisplayMetrics();
    }

    /**
     * Convert dp to pixels
     * @param dp Dimension value in dp
     * @return Converted value in pixels
     */
    public static float dpToPx(float dp) {
        return dp * getDisplayMetrics().density;
    }

    /**
     * Convert pixels in dp
     * @param px Dimension value in pixels
     * @return Converted value in dp
     */
    public static float pxToDp(float px) {
        return px / getDisplayMetrics().density;
    }

    /**
     * Check if current api level is at least the value of apiLevel
     * @param apiLevel value of an android api build version
     * @return true if current api level is at least the value of apiLevel, otherwise false
     */
    @ChecksSdkIntAtLeast(parameter = 0)
    public static boolean minApiLevel(int apiLevel) {
        return android.os.Build.VERSION.SDK_INT >= apiLevel;
    }

    /**
     * Check if current api level is below or equal the to value of apiLevel
     * @param apiLevel value of an android api build version
     * @return true if current api level is below or equal to the value of apiLevel, otherwise false
     */
    public static boolean maxApiLevel(int apiLevel) {
        return android.os.Build.VERSION.SDK_INT <= apiLevel;
    }

    /**
     * Generate a bitmap form a Drawable object
     * @param drawable Drawable object
     * @return Bitmap form a Drawable object
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Get a list of Strings(Groups) that match a regex pattern in a text string
     * @param text String value of the search subject
     * @param regex Search pattern in the regex syntax
     * @return List of Strings(Groups) that match a regex pattern in a text string
     */
    public static ArrayList<String> regexSearch(String text, String regex) {
        ArrayList<String> groups = new ArrayList<>();
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                groups.add(matcher.group());
            }
        } catch (Exception ignore) {}

        return groups;
    }

    /**
     * Converts any serializable object or array of objects into a json string
     * @param object any serializable object or array
     * @return json string
     */
    public static <T> String toJson(T object) {
        return new Gson().toJson(object);
    }

    /**
     * Parse a json into a serializable object of type T
     * @param json a valid json string
     * @param classObject class object of T type
     * @param <T> returning class
     * @return an object of type T
     */
    public static <T> T fromJson(String json, Class<T> classObject) {
        try {
            return new Gson().fromJson(json, classObject);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Parse a json into a serializable array of objects of type T
     * @param jsonArray a valid jsonArray string
     * @param itemClass class object of the items within the ArrayList
     * @param <T> returning class
     * @return a serializable array of objects of type T
     */
    public static <T> ArrayList<T> fromJsonArray(String jsonArray, Class<T> itemClass) {
        ArrayList<T> list = null;
        try {
            Gson gson = new Gson();
            JsonArray arry = JsonParser.parseString(jsonArray).getAsJsonArray();
            list = new ArrayList<>();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, itemClass));
            }
        } catch (Exception ignore) { }

        return list;
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
     * Get a color state list for changing a view tint programmatically
     * @param color color integer
     * @return ColorStateList
     */
    public static ColorStateList getTint(@ColorInt int color) {
        return new ColorStateList(new int[][]{new int[] { android.R.attr.state_enabled }}, new int[]{ color });
    }

    /**
     * Get application name
     * @return name of app
     */
    public static String getAppName() {
        ApplicationInfo applicationInfo = UtilBase.getContext().getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : ResourceFetch.getString(stringId);
    }
}
