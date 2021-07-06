package com.nextsense.nsutils.commons;

import android.annotation.SuppressLint;
import android.util.Log;

import com.nextsense.nsutils.baseElements.NsDate;

import java.io.PrintWriter;
import java.io.StringWriter;

@SuppressWarnings("unused")
@SuppressLint("DefaultLocale")
public class NsLog {
    private static ILogOnline onlineLogger;

    /**
     * Log an exception online and within the logcat
     * @param e any Exception
     */
    public static void o(Exception e) {
        o(e.getClass().getSimpleName(), logFromException(e));
    }

    /**
     * Log any type of content online and within the logcat
     * @param tag String
     * @param content String
     */
    public static void o(String tag, String content) {
        if(onlineLogger != null) {
            onlineLogger.newLog(new NsDate().toString("yyyy-MM-dd'T'HH:mm:ss.SSS (XXX)"), tag, content);
        }

        l(tag, content);
    }

    /**
     * Log an exception within the logcat
     * @param e any Exception
     */
    public static void l(Exception e) {
        l(e.getClass().getSimpleName(), logFromException(e));
    }

    /**
     * Log any type of content online and within the logcat
     * @param tag String
     * @param content String
     */
    public static void l(String tag, String content) {
        tag = tag.length() > 17 ? tag.substring(0, 17) : tag;
        String logPageFormat = "=======PART [%03d/%03d]=======\n%s";
        int charsPerPage = 3600 - String.format(logPageFormat, 0, 0, "").length();
        int totalPages = (int) Math.ceil((float) content.length() / (float) charsPerPage);
        logLargeMessage(String.format("NsLog %s", tag), logPageFormat, content, charsPerPage, totalPages, 1);
    }

    private static void logLargeMessage(String tag, String logPageFormat, String content, int charsPerPage, int totalPages, int page) {
        if (content.length() > charsPerPage) {
            Log.d(tag, String.format(logPageFormat, page, totalPages, content.substring(0, charsPerPage)));
            logLargeMessage(tag, logPageFormat, content.substring(charsPerPage), charsPerPage, totalPages, page + 1);
        } else {
            Log.d(tag, page <= 1 ? content : String.format(logPageFormat, page, totalPages, content));
        }
    }

    private static String logFromException(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return String.format("Message: %s\nStackTrace: %s", Safe.text(e), Safe.text(sw));
        } catch(Exception ignore) {}

        return "NULL error logged";
    }

    /**
     * Setup a handler for online logging
     * @param onlineLogger listener of type ILogOnline
     */
    public static void setOnlineLogger(ILogOnline onlineLogger) {
        NsLog.onlineLogger = onlineLogger;
    }

    public interface ILogOnline {
        void newLog(String date, String title, String body);
    }
}
