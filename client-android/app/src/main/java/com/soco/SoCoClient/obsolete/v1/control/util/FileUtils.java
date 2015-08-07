package com.soco.SoCoClient.obsolete.v1.control.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;

import com.soco.SoCoClient.obsolete.v1.control.config.GeneralConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static String tag = "FileUtils";

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    public static String getPath(final Context context, final Uri uri) {
//
//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//
//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//
//                // TODO handle non-primary volumes
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[] {
//                        split[1]
//                };
//
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
//    public static String getDataColumn(Context context, Uri uri, String selection,
//                                       String[] selectionArgs) {
//
//        Cursor cursor = null;
//        final String column = "_data";
//        final String[] projection = {
//                column
//        };
//
//        try {
//            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
//                    null);
//            if (cursor != null && cursor.moveToFirst()) {
//                final int column_index = cursor.getColumnIndexOrThrow(column);
//                return cursor.getString(column_index);
//            }
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return null;
//    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
//    public static boolean isExternalStorageDocument(Uri uri) {
//        return "com.android.externalstorage.documents".equals(uri.getAuthority());
//    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
//    public static boolean isDownloadsDocument(Uri uri) {
//        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
//    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
//    public static boolean isMediaDocument(Uri uri) {
//        return "com.android.providers.media.documents".equals(uri.getAuthority());
//    }

    public static String checkUriSize(ContentResolver cr, Uri uri) {

        Log.d(tag, "Check uri size for: " + uri);
        String size = "0";
        Cursor cursor = cr.query(uri, null, null, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (!cursor.isNull(sizeIndex))
                    size = cursor.getString(sizeIndex);
            }
        } finally {
            if (cursor != null)
               cursor.close();
        }

        Log.d(tag, "Size is: " + size);
        return size;
    }

    public static String getDisplayName(ContentResolver cr, Uri uri) {
        Log.d(tag, "Get display name from uri: " + uri);

        String name = "name_not_found";
        Cursor cursor = cr.query(uri, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.v(tag, "Found display Name: " + name);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        Log.d(tag, "Display name to return: " + name);
        return name;
    }

//    public static void checkUriMeta(ContentResolver cr, Uri uri) {
//        Log.d(tag, "Check uri meta for: " + uri);
//        Cursor cursor = cr.query(uri, null, null, null, null, null);
//
//        try {
//            if (cursor != null && cursor.moveToFirst()) {
//                String displayName = cursor.getString(
//                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                Log.d(tag, "Display Name: " + displayName);
//
//                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
//                String size = null;
//                if (!cursor.isNull(sizeIndex)) {
//                    size = cursor.getString(sizeIndex);
//                } else {
//                    size = "Unknown";
//                }
//                Log.d(tag, "Size: " + size);
//            }
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//    }

    public static String copyFileToLocal(Uri uri, ContentResolver cr){
        Log.i(tag, "Copy file to local start, uri: " + uri);
        String displayName = getDisplayName(cr, uri);

        String destinationFilename = Environment.getExternalStorageDirectory().getPath()
                + File.separatorChar + GeneralConfig.APP_FOLDER_NAME
                + File.separator + displayName;
        Log.i(tag, "Copy to: " + destinationFilename);

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            InputStream is = cr.openInputStream(uri);
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
        } catch (IOException e) {
            Log.e(tag, "Cannot copy file: " + e.toString());
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                Log.e(tag, "Cannot close file stream: " + e.toString());
            }
        }

        return destinationFilename;
    }

}
