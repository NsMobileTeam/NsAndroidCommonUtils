package com.nextsense.nsutils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class NsDate extends Date {
    public static final String DATE_STRING_KEYWORD = "&DATE&";

    /**
     * New date object from a textString that conforms to the defined SimpleDateFormat
     * @param rawDate A textString conforming to the defined SimpleDateFormat
     * @param formatPattern SimpleDateFormat syntax example "yyyy-MM-dd'T'HH:mm:ss.SSS (XXX)"
     */
    @SuppressLint("SimpleDateFormat")
    public NsDate(String rawDate, String formatPattern) {
        this(rawDate, new SimpleDateFormat(formatPattern));
    }

    /**
     * New date object from a textString that conforms to the defined SimpleDateFormat
     * @param rawDate A textString conforming to the defined SimpleDateFormat
     * @param formatPattern SimpleDateFormat syntax example "yyyy-MM-dd'T'HH:mm:ss.SSS (XXX)"
     * @param locale Desired locale
     */
    public NsDate(String rawDate, String formatPattern, Locale locale) {
        this(rawDate, new SimpleDateFormat(formatPattern, locale));

    }

    /**
     * New date object from a textString that conforms to the defined SimpleDateFormat
     * @param rawDate A textString conforming to the defined SimpleDateFormat
     * @param format SimpleDateFormat syntax example "yyyy-MM-dd'T'HH:mm:ss.SSS (XXX)"
     */
    public NsDate(String rawDate, SimpleDateFormat format) {
        setTime(rawDate, format);
    }

    /**
     * Date from a timeString in milliseconds
     * @param rawMillis TimeString in milliseconds
     */
    public NsDate(String rawMillis) {
        setTime(longFromString(rawMillis));
    }

    /**
     * Date from a timeString in unknown scale multiplied by its divisor into milliseconds
     * @param rawTime TimeString in unknown scale
     * @param timeDivisor Time multiplier into milliseconds
     */
    public NsDate(String rawTime, long timeDivisor) {
        setTime(longFromString(rawTime) / timeDivisor);
    }

    /**
     * Date from time in milliseconds
     * @param millis Time in milliseconds
     */
    public NsDate(long millis) {
        setTime(millis);
    }

    /**
     * Converts a date from a textString that conforms to the defined SimpleDateFormat
     * @param rawDate A textString conforming to the defined SimpleDateFormat
     * @param format SimpleDateFormat syntax example "yyyy-MM-dd'T'HH:mm:ss.SSS (XXX)"
     */
    public void setTime(String rawDate, SimpleDateFormat format) {
        try {
            setTime(Objects.requireNonNull(format.parse(rawDate)).getTime());
        } catch (Exception e) {
            setTime(0L);
        }
    }

    /**
     * Converts any string containing a number into a longInt
     * @param rawNumber TextString containing a number example: /Date('1614947297709')12
     * @return The first number in a textString, from the above example 1614947297709L
     */
    private long longFromString(String rawNumber) {
        try {
            ArrayList<String> extractedMillis = CommonUtils.regexSearch(rawNumber, "\\d+");
            return Long.parseLong(extractedMillis.get(0));
        } catch (Exception ignore) {}

        return 0;
    }

    /**
     * Creates SimpleDateFormat from a String and returns a formatted date string
     * @param format SimpleDateFormat syntax example "yyyy-MM-dd'T'HH:mm:ss.SSS (XXX)"
     * @return Time formatted according to SimpleDateFormat string
     */
    @SuppressLint("SimpleDateFormat")
    public String toString(String format) {
        try {
            return toString(new SimpleDateFormat(format));
        } catch (Exception ignore) {
            return "";
        }
    }

    /**
     * Creates SimpleDateFormat from a String and returns a formatted date string
     * @param format SimpleDateFormat syntax example "yyyy-MM-dd'T'HH:mm:ss.SSS (XXX)"
     * @param locale Desired locale
     * @return Time formatted according to SimpleDateFormat string
     */
    public String toString(String format, Locale locale) {
        try {
            return toString(new SimpleDateFormat(format, locale));
        } catch (Exception ignore) {
            return "";
        }
    }

    /**
     * Formats date into string defined in SimpleDateFormat
     * @param format Predefined SimpleDateFormat
     * @return Time formatted according to SimpleDateFormat
     */
    public String toString(SimpleDateFormat format) {
        return format.format(getTime());
    }

    /**
     * String value of time in milliseconds
     * @return String value of time in milliseconds
     */
    public String toStringMs() {
        return String.valueOf(getTime());
    }

    /**
     * String value of time in milliseconds
     * @param divisor If greater than 0 will be used to divide the milliseconds in final result
     * @return String value of time in milliseconds divided by divisor
     */
    public String toStringMs(int divisor) {
        return String.valueOf(getTime() / (divisor > 0 ? divisor : 1));
    }

    /**
     * Use keyword &DATE& to place value of time
     * @param stringFormat Default string format containing the &DATE& keyword, example: "%s&DATE&%s%d"
     * @param objects Using previous example objects should be ("/Date(\'", "\')", 12) and will result in /Date('1614947297709')12
     * @return Formatted string according to String.format(String format, Object[] values)
     */
    public String toStringFormat(String stringFormat, Object... objects) {
        return toStringFormat(0, stringFormat, objects);
    }

    /**
     * Use keyword &DATE& to place value of time
     * @param divisor If greater than 0 will be used to divide the milliseconds in final result
     * @param stringFormat Default string format containing the &DATE& keyword, example: "%s&DATE&%s%d"
     * @param objects Using previous example objects should be ("/Date(\'", "\')", 12) and will result in /Date('1614947297709')12
     * @return Formatted string according to String.format(String format, Object[] values)
     */
    public String toStringFormat(int divisor, String stringFormat, Object... objects) {
        stringFormat = stringFormat.replaceAll(DATE_STRING_KEYWORD, toStringMs(divisor));
        return String.format(stringFormat, objects);
    }
}
