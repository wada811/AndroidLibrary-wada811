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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.webkit.MimeTypeMap;
import at.wada811.constant.MediaConstant;

public class MediaUtils {

    /**
     * SDカードにアプリ用ディレクトリを作成する
     * 
     * @param context
     * @return
     */
    public static File getExternalStorageAppDir(Context context, String dirName){
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), dirName);
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    /**
     * SDカードのパスを取得する
     * 
     * @return path of SD Card
     */
    public static String getExternalStoragePath(){
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * ディレクトリ名を指定してSDカードのパスを取得する
     * 
     * @param dirName
     * @return
     */
    public static String getExternalStoragePath(String dirName){
        return MediaUtils.getExternalStoragePath() + File.separator + dirName;
    }

    /**
     * 指定したパスが有効なディレクトリパスか判定する
     * 
     * @param path
     * @return 存在するディレクトリなら true
     */
    public static boolean isValidDirectoryPath(String path){
        if(path == null){
            return false;
        }
        File file = new File(path);
        if(file.exists() && file.isDirectory()){
            return true;
        }
        return false;
    }

    /**
     * SDカードがマウント中かどうか調べる
     * 
     * @return マウントしていれば true
     */
    public static boolean checkSdCardMounted(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * SDカードに十分な空き容量があるか調べる
     * 
     * @return SDカードに十分な空き容量があれば true
     */
    public static boolean checkAvailableSpaceInSdCard(double limitMinimumSpace){
        String externalStoragePath = MediaUtils.getExternalStoragePath();
        File file = new File(externalStoragePath);
        double availableSpaceInSdCard = file.getUsableSpace() / 1024; // (MB)
        if(availableSpaceInSdCard >= limitMinimumSpace){
            return true;
        }else{
            return false;
        }
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
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(file);
            bitmap.compress(MediaConstant.DEFAULT_CONPRESS_FORMAT, MediaConstant.DEFAULT_COMPRESS_QUALITY, fos);
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
        String extension = null;
        int index = filePath.lastIndexOf(".");
        if(index > 0){
            extension = filePath.substring(index + 1);
        }
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        if(extension != null){
            mimeType = mime.getMimeTypeFromExtension(extension);
        }
        return mimeType;
    }

    /**
     * サウンドを再生する
     * 
     * @param context
     * @param resId
     */
    public static void playSound(Context context, int resId){
        MediaPlayer mediaPlayer = MediaPlayer.create(context, resId);
        try{
            mediaPlayer.prepare();
        }catch(IllegalStateException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mediaPlayer){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });
    }

    /**
     * サウンドを再生する
     * 
     * @param context
     * @param uri
     */
    public static void playSound(Context context, Uri uri){
        MediaPlayer mediaPlayer = MediaPlayer.create(context, uri);
        try{
            mediaPlayer.prepare();
        }catch(IllegalStateException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mediaPlayer){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });
    }
}
