package com.nextsense.nsutils.storage;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import com.nextsense.nsutils.UtilBase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings("unused")
public class FileUtil {

    /**
     * Get a file name without its extension from an url
     * @param url String file url or path
     * @return File name without its extension from an url
     */
    public static String fileNameFromUrl(String url) {
        try {
            return url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.')).trim();
        } catch (Exception e) {
            return fullFileNameFromUrl(url);
        }
    }

    /**
     * Get a full file name from an url
     * @param url String file url or path
     * @return The Full file name from an url
     */
    public static String fullFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1).replaceFirst("/*$", "").trim();
    }

    /**
     * Get the extension of a file from an url
     * @param url String file url or path
     * @return The extension of a file from an url
     */
    public static String extensionFromUrl(String url) {
        return MimeTypeMap.getFileExtensionFromUrl(url);
    }

    /**
     * Get the extension of a file from an uri
     * @param uri String file uri
     * @return The extension of a file from an uri
     */
    public static String extensionFromUri(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeTypeOf(uri));
    }

    /**
     * Get the mime type of a file from an url
     * @param url String file url, path or content uri
     * @return The mime type of a file from an url
     */
    public static String mimeTypeOf(String url) {
        return mimeTypeOf(Uri.parse(url));
    }

    /**
     * Get the mime type of a file from an uri
     * @param uri String file uri
     * @return The mime type of a file from an url
     */
    public static String mimeTypeOf(Uri uri) {
        ContentResolver contentResolver = UtilBase.getContext().getContentResolver();
        if(uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            return contentResolver.getType(uri);
        }

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extensionFromUrl(uri.toString()));
    }

    /**
     * Open any accessible file with an Implicit intent
     * @param file Object instance of a file
     */
    public static void openFile(File file) {
        Uri uri = FileProvider.getUriForFile(UtilBase.getContext(), UtilBase.getFileProviderAuthority(), file);
        openUri(uri);
    }

    /**
     * Open any accessible uri with an Implicit intent
     * @param uri Accessible file address or service
     */
    public static void openUri(Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeTypeOf(uri));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
        UtilBase.getContext().startActivity(intent);
    }

    /**
     * Get an File from an accessible uri
     * @param uri Accessible file address
     * @return File object
     */
    public static File fileFromUri(Uri uri) {
        File destinationFilename = new File(UtilBase.getContext().getFilesDir().getPath() + File.separatorChar + queryName(uri) + "." + extensionFromUri(uri));
        try (InputStream ins = UtilBase.getContext().getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    /**
     * Create file from an input stream
     * @param ins Instance of the InputStream
     * @param destination Destination of the output file
     */
    private static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get a file name without extension from a content uri
     * @param uri content uri
     * @return File name without extension from a content uri
     */
    private static String queryName(Uri uri) {
        Cursor returnCursor = UtilBase.getContext().getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }
}
