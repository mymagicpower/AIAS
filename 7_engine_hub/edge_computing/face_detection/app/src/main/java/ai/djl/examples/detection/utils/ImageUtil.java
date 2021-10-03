/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package ai.djl.examples.detection.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import ai.djl.android.core.BitmapImageFactory;
import ai.djl.modality.cv.Image;

public class ImageUtil {
    /**
     * Get djl image
     *
     * @param mContext context
     * @param assetPath asset path
     */
    public static Image getImage(Context mContext, String assetPath) {
        Image img = null;
        try {
            AssetManager assetManager = mContext.getAssets();
            InputStream is = assetManager.open(assetPath);
            img = BitmapImageFactory.getInstance().fromInputStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    /**
     * Get bitmap
     *
     * @param mContext context
     * @param assetPath asset path
     */
    public static Bitmap getBitmap(Context mContext, String assetPath) {
        Bitmap bitmap = null;
        try {
            AssetManager assetManager = mContext.getAssets();
            InputStream is = assetManager.open(assetPath);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * Get real path for uri path
     * e.g.:
     * uri:  content://media/external/images/media/93289
     * path: /storage/emulated/0/DCIM/Camera/IMG_20220807_133403.jpg
     *
     * @param context context
     * @param contentUri uri path
     */
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
