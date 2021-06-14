package com.nextsense.nsutils.baseElements;

import android.annotation.SuppressLint;

import com.nextsense.nsutils.commons.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class NsDate extends Date {
    public static final String DATE_STRING_KEYWORD = "&DATE&";

    /**
     * New date object from a textString that conforms to the defined SimpleDateFormat formatString
     * @param rawDate A textString conforming to the defined SimpleDateFormat
     * @param formatPattern SimpleDateFormat syntax example "yyyy-MM-dd'T'HH:mm:ss.SSS (XXX)"
     */
    public NsDate(String rawDate, String formatPattern) {
        setTime(rawDate, formatPattern);
    }

    /**
     * New date object from a textString that conforms to the defined localised SimpleDateFormat
     * @param rawDate A textString conforming to the defined SimpleDateFormat
     * @param formatPattern SimpleDateFormat syntax example "yyyy-MM-dd'T'HH:mm:ss.SSS (XXX)"
     * @param locale Desired locale
     */
    public NsDate(String rawDate, String formatPattern, Locale locale) {
        setTime(rawDate, formatPattern, locale);
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
     * New date object from a timeString containing a millisecond substring
     * @param rawMillis TimeString in milliseconds
     */
    public NsDate(String rawMillis) {
        setTime(rawMillis);
    }

    /**
     * Date from a timeString containing a timeMark in unknown scale multiplied by its divisor into milliseconds
     * @param rawTime TimeString in unknown scale
     * @param timeDivisor Time multiplier into milliseconds
     */
    public NsDate(String rawTime, long timeDivisor) {
        setTime(longFromString(rawTime) / timeDivisor);
    }

    /**
     * Date from milliseconds
     * @param millis Time in milliseconds
     */
    public NsDate(long millis) {
        setTime(millis);
    }

    /**
     * Current time and date
     */
    public NsDate() {
        setTime(System.currentTimeMillis());
    }

    /**
     * Set date object from a textString that conforms to the defined SimpleDateFormat formatString
     * @param rawDate A textString conforming to the defined SimpleDateFormat
     * @param formatPattern SimpleDateFormat syntax example "yyyy-MM-dd'T'HH:mm:ss.SSS (XXX)"
     */
    @SuppressLint("SimpleDateFormat")
    public void setTime(String rawDate, String formatPattern) {
        setTime(rawDate, new SimpleDateFormat(formatPattern));
    }

    /**
     * Set date object from a textString that conforms to the defined localised SimpleDateFormat
     * @param rawDate A textString conforming to the defined SimpleDateFormat
     * @param formatPattern SimpleDateFormat syntax example "yyyy-MM-dd'T'HH:mm:ss.SSS (XXX)"
     * @param locale Desired locale
     */
    public void setTime(String rawDate, String formatPattern, Locale locale) {
        setTime(rawDate, new SimpleDateFormat(formatPattern, locale));
    }

    /**
     * Set date object from a timeString containing a millisecond substring
     * @param rawMillis TimeString in milliseconds
     */
    public void setTime(String rawMillis) {
        setTime(longFromString(rawMillis));
    }

    /**
     * Sets the date from a textString that conforms to the defined SimpleDateFormat
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
     * Set date object from a timeString containing a timeMark in unknown scale multiplied by its divisor into milliseconds
     * @param rawTime TimeString in unknown scale
     * @param timeMultiplier Time multiplier into milliseconds
     */
    public void setTime(String rawTime, long timeMultiplier) {
        setTime(longFromString(rawTime) * timeMultiplier);
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
     * @param dateFormat SimpleDateFormat syntax example "yyyy-MM-dd'T'HH:mm:ss.SSS (XXX)"
     * @return Time formatted according to SimpleDateFormat string
     */
    @SuppressLint("SimpleDateFormat")
    public String toString(String dateFormat) {
        try {
            return toString(new SimpleDateFormat(dateFormat));
        } catch (Exception ignore) {
            return "";
        }
    }

    /**
     * Creates localised SimpleDateFormat from a String and returns a formatted date string
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
     * String value of the time in milliseconds
     * @return String value of time in milliseconds
     */
    public String toStringMs() {
        return String.valueOf(getTime());
    }

    /**
     * String value of time in milliseconds devided by the divisor
     * @param divisor If greater than 0 will be used to divide the milliseconds in final result
     * @return String value of time in milliseconds divided by divisor
     */
    public String toStringMs(int divisor) {
        return String.valueOf(getTime() / (divisor > 0 ? divisor : 1));
    }

    /**
     * Get a formatted time string using the keyword &DATE& to place a value of time
     * @param stringFormat Default string format containing the &DATE& keyword, example: "%s&DATE&%s%d"
     * @param objects Using previous example objects should be ("/Date(\'", "\')", 12) and will result in /Date('1614947297709')12
     * @return Formatted string according to String.format(String format, Object[] values)
     */
    public String toStringFormat(String stringFormat, Object... objects) {
        return toStringFormat(0, stringFormat, objects);
    }

    /**
     * Get a formatted time string (divided by the divisor) using the keyword &DATE& to place a value of time
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