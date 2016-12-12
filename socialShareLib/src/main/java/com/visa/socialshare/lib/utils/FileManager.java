package com.visa.socialshare.lib.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Created by qxie on 10/11/16.
 */

public class FileManager {

    private static final String TAG = FileManager.class.getSimpleName();

    private static int bitmapQuality = 80;

    public static void setBitmapQuality(int bitmapQuality) {
        FileManager.bitmapQuality = bitmapQuality;
    }

    public static Uri getBitmapUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, bitmapQuality, bytes);
        String path = insertImage(inContext.getContentResolver(), inImage, "Title", null); //store the bitmap and return the URL for newly created image
        Log.v("Bitmap path: ", path);
        return Uri.parse(path);
    }


    private static String insertImage(ContentResolver cr, Bitmap source,
                                     String title, String description) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis()); //put current timestamp to values

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id,
                        MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
//                Bitmap microThumb = MediaStore.Images.Media.(cr, miniThumb, id, 50F, 50F,
//                        MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                Log.e(TAG, "Failed to create thumbnail, removing original");
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to insert image", e);
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }
}
