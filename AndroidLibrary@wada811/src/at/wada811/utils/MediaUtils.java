/*
 * Copyright 2013 wada811<at.wada811@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.wada811.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MediaUtils {

    /** デフォルトの保存品質 */
    public static int DEFAULT_COMPRESS_QUALITY = 100;
    public static final String SAVE_VIDEO_EXTENSION_3GP = "3gp";
    public static final String SAVE_VIDEO_EXTENSION_MP4 = "mp4";

    /**
     * Specifies the known formats and those's extensions that a bitmap can be compressed into
     */
    public static enum ImageFormat {
        /**
         * {@link CompressFormat#JPEG}
         */
        JPEG(CompressFormat.JPEG, "jpg"),
        /**
         * {@link CompressFormat#PNG}
         */
        PNG(CompressFormat.PNG, "png"),
        /**
         * {@link CompressFormat#WEBP}
         */
        WEBP(CompressFormat.WEBP, "webp");

        private CompressFormat mCompressFormat;
        private String mExtension;

        ImageFormat(CompressFormat compressFormat, String extension) {
            mCompressFormat = compressFormat;
            mExtension = extension;
        }

        public CompressFormat getCompressFormat(){
            return mCompressFormat;
        }

        public String getExtension(){
            return mExtension;
        }
    }

    /**
     * Return the known formats a bitmap can be compressed into
     * 
     * @param extension jpg|png|webp
     * @return {@link CompressFormat}
     */
    public static CompressFormat getCompressFormat(String extension){
        for(ImageFormat imageFormat : ImageFormat.values()){
            if(imageFormat.getExtension().equals(extension)){
                return imageFormat.getCompressFormat();
            }
        }
        return ImageFormat.JPEG.getCompressFormat();
    }

    /**
     * Return the extension of known formats a bitmap can be compressed into
     * 
     * @param {@link CompressFormat}
     * @return extension jpg|png|webp
     */
    public static String getExtension(CompressFormat compressFormat){
        for(ImageFormat imageFormat : ImageFormat.values()){
            if(imageFormat.getCompressFormat() == compressFormat){
                return imageFormat.getExtension();
            }
        }
        return ImageFormat.JPEG.getExtension();
    }

    public static File getExternalStorageDirectory(String dirName){
        return new File(Environment.getExternalStorageDirectory(), dirName);
    }

    /**
     * SDカードがマウント中かどうか調べる
     * 
     * @return マウントしていれば true
     */
    public static boolean isSdCardMounted(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 十分な空き容量があるか調べる
     * 
     * @return 十分な空き容量があれば true
     */
    public static boolean isAvailableSpace(File file, double limitMinimumSpace){
        double availableSpaceInSdCard = file.getUsableSpace() / 1024; // (MB)
        return availableSpaceInSdCard >= limitMinimumSpace;
    }

    public static boolean saveBitmap(Context context, Bitmap bitmap, String filePath, int orientation){
        LogUtils.d(filePath + ", orientaion: " + orientation);
        return MediaUtils.saveBitmap(context, BitmapUtils.rotate(bitmap, orientation), filePath);
    }

    /**
     * Bitmapを指定のパスに保存する
     * 
     * @param context
     * @param bitmap
     * @param filePath
     * @return
     */
    public static boolean saveBitmap(Context context, Bitmap bitmap, String filePath){
        File file = new File(filePath);
        String extension = FileNameUtils.getExtension(filePath);
        LogUtils.d(extension);
        CompressFormat compressFormat = MediaUtils.getCompressFormat(extension);
        LogUtils.d(compressFormat.name());
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(file);
            bitmap.compress(compressFormat, MediaUtils.DEFAULT_COMPRESS_QUALITY, fos);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return false;
        }catch(Exception e){
            LogUtils.e(filePath, e);
            return false;
        }finally{
            if(fos != null){
                try{
                    fos.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            bitmap.recycle();
            bitmap = null;
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        return true;
    }

    public static void saveBitmapInMediaStore(Context context, Bitmap bitmap, String filePath, int orientation){
        File file = new File(filePath);
        long date = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        String mimeType = MediaUtils.getMimeType(file);
        contentValues.put(MediaColumns.MIME_TYPE, mimeType);
        contentValues.put(MediaColumns.DATA, filePath);
        contentValues.put(MediaColumns.SIZE, file.length());
        contentValues.put(MediaColumns.TITLE, file.getName());
        contentValues.put(ImageColumns.ORIENTATION, orientation);
        contentValues.put(MediaColumns.DATE_ADDED, date);
        contentValues.put(ImageColumns.DATE_TAKEN, date);
        contentValues.put(MediaColumns.DATE_MODIFIED, date);
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        MediaStore.Images.Media.insertImage(contentResolver, bitmap, file.getName(), null);
    }

    public static boolean saveBitmap(Context context, byte[] data, String filePath){
        File file = new File(filePath);
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(file);
            fos.write(data, 0, data.length);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return false;
        }catch(Exception e){
            LogUtils.e(filePath, e);
        }finally{
            if(fos != null){
                try{
                    fos.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        return true;
    }

    /**
     * ファイルから MIME Type を取得する
     * 
     * @param file
     * @return
     */
    public static String getMimeType(File file){
        return MediaUtils.getMimeType(file.getAbsolutePath());
    }

    /**
     * ファイルパスの拡張子から MIME Type を取得する
     * 
     * @param filePath
     * @return
     */
    public static String getMimeType(String filePath){
        String mimeType = null;
        String extension = FileNameUtils.getExtension(filePath);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        if(extension != null){
            mimeType = mime.getMimeTypeFromExtension(extension);
        }
        return mimeType;
    }
}
