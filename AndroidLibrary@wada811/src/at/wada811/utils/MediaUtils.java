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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.webkit.MimeTypeMap;
import at.wada811.constant.MediaConstant;

public class MediaUtils {

    /**
     * 保存するディレクトリのパスを取得する
     * 
     * @param context アプリ内部領域を除外したい場合は null を渡す
     * @return save dir path, or null
     */
    public static String getSaveDirPath(Context context){
        String saveDirPath = MediaUtils.getExternalSdCardPath();
        LogUtils.v(saveDirPath);
        if(saveDirPath == null){
            saveDirPath = MediaUtils.getInternalSdCardParh();
            LogUtils.v(saveDirPath);
        }
        if(saveDirPath == null && context != null){
            saveDirPath = context.getFilesDir().getAbsolutePath();
            LogUtils.v(saveDirPath);
        }
        return saveDirPath;
    }

    /**
     * 外部SDカードのパスを取得する
     * 
     * @return external SD card path, or null
     */
    public static String getExternalSdCardPath(){
        HashSet<String> paths = new HashSet<String>();
        Scanner scanner = null;
        try{
            // システム設定ファイルを読み込み
            File vold_fstab = new File("/system/etc/vold.fstab");
            scanner = new Scanner(new FileInputStream(vold_fstab));
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                LogUtils.e(line);
                // dev_mountまたはfuse_mountで始まる行
                if(line.startsWith("dev_mount") || line.startsWith("fuse_mount")){
                    // 半角スペースではなくタブで区切られている機種もあるらしい
                    // 半角スペース区切り３つめ（path）を取得
                    String path = line.replaceAll("\t", " ").split(" ")[2];
                    paths.add(path);
                }
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
            LogUtils.e(e);
            return null;
        }finally{
            if(scanner != null){
                scanner.close();
            }
        }
        // Environment.getExternalStorageDirectory() が内部SDを返す場合は除外
        if(!Environment.isExternalStorageRemovable()){
            paths.remove(Environment.getExternalStorageDirectory().getPath());
        }
        // マウントされているSDカードのパスを追加 
        List<String> mountSdCardPaths = new ArrayList<String>();
        for(String path : paths){
            if(isMounted(path)){
                LogUtils.v(path);
                mountSdCardPaths.add(path);
            }
        }
        // マウントされているSDカードのパス
        String mountSdCardPath = null;
        if(mountSdCardPaths.size() > 0){
            mountSdCardPath = mountSdCardPaths.get(0);
        }
        return mountSdCardPath;

    }

    /**
     * パスがマウントされているSDカードのパスかチェックする
     * 
     * @param path SD card path
     * @return true if path is the mounted SD card's path, otherwise false
     */
    public static boolean isMounted(String path){
        boolean isMounted = false;
        Scanner scanner = null;
        try{
            // マウントポイントを取得する
            File mounts = new File("/proc/mounts");
            scanner = new Scanner(new FileInputStream(mounts));
            while(scanner.hasNextLine()){
                if(scanner.nextLine().contains(path)){
                    isMounted = true;
                    break;
                }
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
            LogUtils.e(e);
            return false;
        }finally{
            if(scanner != null){
                scanner.close();
            }
        }
        return isMounted;
    }

    /**
     * 内部SDカードのパスを取得する
     * 
     * @return internal SD card path, or null
     */
    public static String getInternalSdCardParh(){
        if(Environment.isExternalStorageRemovable()){
            return null;
        }else if(MediaUtils.checkSdCardMounted()){
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }else{
            return null;
        }
    }

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

}
