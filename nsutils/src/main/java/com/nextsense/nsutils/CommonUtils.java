package com.nextsense.nsutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
}
