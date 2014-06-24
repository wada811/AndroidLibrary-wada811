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

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import at.wada811.android.library.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {

    /**
     * width, height を指定して inSampleSize を計算する
     * 
     * @param options
     * @param width
     * @param height
     * @return inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int width, int height){
        // 画像の元サイズ
        final int originalHeight = options.outHeight;
        final int originalWidth = options.outWidth;
        int inSampleSize = 1;
        if((originalHeight > height) || (originalWidth > width)){
            if(originalWidth > originalHeight){
                inSampleSize = Math.round((float)originalHeight / (float)height);
            }else{
                inSampleSize = Math.round((float)originalWidth / (float)width);
            }
        }
        return inSampleSize;
    }

    /**
     * int[] rgbData = new int[previewSize.width * previewSize.height];
     * BitmapUtils.decodeYUV420SP(rgbData, data, previewSize.width, previewSize.height);
     * 
     * @param rgb
     * @param yuv420sp
     * @param width
     * @param height
     */
    public static void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height){
        final int frameSize = width * height;

        for(int j = 0, yp = 0; j < height; j++){
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for(int i = 0; i < width; i++, yp++){
                int y = (0xff & (yuv420sp[yp])) - 16;
                if(y < 0){
                    y = 0;
                }
                if((i & 1) == 0){
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if(r < 0){
                    r = 0;
                }else if(r > 262143){
                    r = 262143;
                }
                if(g < 0){
                    g = 0;
                }else if(g > 262143){
                    g = 262143;
                }
                if(b < 0){
                    b = 0;
                }else if(b > 262143){
                    b = 262143;
                }

                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
    }

    /**
     * YUVデータからBitmapを生成する
     * 
     * @param context
     * @param yuvData
     * @param imageFormat
     * @param width
     * @param height
     * @return bitmap
     */
    public static Bitmap createBitmapFromYuv(Context context, byte[] yuvData, int width, int height){
        String key = context.getString(R.string.keyPreviewFormat);
        int imageFormat = PreferenceUtils.getInt(context, key, ImageFormat.NV21);
        byte[] rgbData = BitmapUtils.decodeYuvData(yuvData, imageFormat, width, height);
        final Bitmap bitmap = BitmapUtils.createBitmapFromByteArray(rgbData, width, height);
        rgbData = null;
        return bitmap;
    }

    /**
     * YUV420SP(Androidのカメラで撮影された生データのbyte配列)を
     * 一旦JPEGデータに変換してBitmapに変換可能なbyte配列に変換する。
     * 
     * @param yuvData
     *        preview raw data
     * @param imageFormat
     *        ImageFormat.NV21 | ImageFormat.YUY2
     * @param width
     *        preview width
     * @param height
     *        preview height
     * @return rgbData
     */
    public static byte[] decodeYuvData(byte[] yuvData, int imageFormat, int width, int height){
        YuvImage yuvImage = new YuvImage(yuvData, imageFormat, width, height, null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int quality = 100; // 一旦JPEGデータを経由するので劣化軽減のために100にする(※100でも少しは劣化する)
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), quality, stream);
        byte[] rgbData = stream.toByteArray();
        try{
            stream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return rgbData;
    }

    /**
     * RGB配列からBitmapを生成する
     * 
     * @param colors
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmapFromRgb(int[] colors, int width, int height){
        return Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
    }

    /**
     * RGB配列からBitmapを生成する
     * 
     * @param colors
     * @param width
     * @param height
     * @param config
     * @return
     */
    public static Bitmap createBitmapFromRgb(int[] colors, int width, int height, Bitmap.Config config){
        return Bitmap.createBitmap(colors, width, height, config);
    }

    /**
     * Byte[] to Bitmap
     * 
     * @param data
     * @param width
     * @param height
     * @return bitmap
     */
    public static Bitmap createBitmapFromByteArray(byte[] data){
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    /**
     * Byte[] to Bitmap
     * 
     * @param data
     * @param width
     * @param height
     * @return bitmap
     */
    public static Bitmap createBitmapFromByteArray(byte[] data, int width, int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = BitmapUtils.calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * Base64 string to Bitmap
     * 
     * @param base64str
     */
    public static Bitmap createBitmapFromBase64(String base64str){
        byte[] decode = Base64.decode(base64str, Base64.DEFAULT);
        return BitmapUtils.createBitmapFromByteArray(decode);
    }

    /**
     * Resource to Bitmap
     * 
     * @param context
     * @param resId
     */
    public static Bitmap createBitmapFromResource(Context context, int resId){
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    /**
     * Drawable to Bitmap
     * 
     * @param drawable
     * @see <a href="http://d.hatena.ne.jp/hyoromo/20100612/1276305512">Bitmap, Drawableに変換 -
     *      hyoromoの日記</a>
     */
    public static Bitmap createBitmapFromDrawable(Drawable drawable){
        return ((BitmapDrawable)drawable).getBitmap();
    }

    /**
     * File to Bitmap
     * 
     * @param filePath
     * @return
     */
    public static Bitmap createBitmapFromFile(File file){
        return BitmapUtils.createBitmapFromFile(file.getAbsolutePath());
    }

    /**
     * File to Bitmap
     * 
     * @param filePath
     * @return
     */
    public static Bitmap createBitmapFromFile(String filePath){
        return BitmapFactory.decodeFile(filePath);
    }

    /**
     * File to Bitmap
     * 
     * @param filePath
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmapFromFile(String filePath, int width, int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = BitmapUtils.calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * InputStream to Bitmap
     * 
     * @param is
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmapFromStream(InputStream is, int width, int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inSampleSize = BitmapUtils.calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);
    }

    /**
     * SDカードから幅と高さを指定したサイズを基準にした値になるように縮小した画像を取得する
     * 
     * @param context
     * @param uri
     * @param width
     * @param height
     * @return
     */
    public static Bitmap loadBitmap(Context context, Uri uri, int width, int height){
        // 分割させる値の計算
        int orientation = BitmapUtils.getImageOrientation(uri, context);
        // 横になっている画像は90度回転させる
        if(Math.abs(orientation % 180) == 90){
            int tmp;
            tmp = width;
            width = height;
            height = tmp;
        }
        InputStream is = null;
        Bitmap bitmap = null;
        try{
            is = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapUtils.createBitmapFromStream(is, width, height);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }finally{
            if(is != null){
                try{
                    is.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        if(orientation != 0){
            // 画像を回転させて取ってくる。
            Bitmap rotateBitmap = BitmapUtils.rotate(bitmap, orientation);
            bitmap.recycle();
            bitmap = null;
            bitmap = rotateBitmap;
        }
        return bitmap;
    }

    /**
     * 画像の向きを取得する
     * 
     * @param uri
     * @param context
     * @return
     */
    public static int getImageOrientation(Uri uri, Context context){
        // CursorでSQLite(DB)を操作
        String[] columns = new String[]{
            MediaStore.Images.ImageColumns.ORIENTATION
        };
        Cursor cursor = context.getContentResolver().query(uri, columns, null, null, null);
        int orientaion = 0;
        if(cursor != null){
            int orientationColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
            if(cursor.moveToFirst()){
                orientaion = cursor.getInt(orientationColumnIndex);
            }
            cursor.close();
        }
        return orientaion;
    }

    /**
     * 新規Bitmapを生成する
     */
    public static Bitmap createNewBitmap(int width, int height){
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    /**
     * 画像をリサイズする(元の画像を破棄する)
     * 
     * @param bitmap
     * @param width
     * @param height
     */
    public static Bitmap resize(Bitmap bitmap, int width, int height){
        Bitmap scaledBitmap;
        if((bitmap.getWidth() == width) && (bitmap.getHeight() == height)){
            // Jelly Beans 以降はサイズが同じ場合は参照渡しになるので元画像をrecycleすると以降の参照で落ちるのでそのまま代入
            scaledBitmap = bitmap;
        }else{
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            bitmap.recycle();
            bitmap = null;
        }
        return scaledBitmap;
    }

    /**
     * 画像をコピーする
     * 
     * @param bitmap
     */
    public static Bitmap copy(Bitmap bitmap){
        return bitmap.copy(bitmap.getConfig(), true);
    }

    /**
     * 画像をリサイズしてコピーする
     * 
     * @param bitmap
     * @param width
     * @param height
     */
    public static Bitmap copy(Bitmap bitmap, int width, int height){
        return Bitmap.createScaledBitmap(bitmap.copy(bitmap.getConfig(), true), width, height, true);
    }

    /**
     * 画像を時計回りに回転する
     * 
     * @param bitmap
     * @param degrees
     * @return
     */
    public static Bitmap rotate(Bitmap bitmap, float degrees){
        if(degrees == 0.0f){
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees); // 回転角を設定する
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 画像を左右反転する
     * 
     * @param bitmap
     * @return
     */
    public static Bitmap invertHorizontal(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    /**
     * 画像を上下反転する
     * 
     * @param bitmap
     * @return
     */
    public static Bitmap invertVertical(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    /**
     * 画像を指定した座標とサイズで切り抜く
     * 
     * @param x 開始x座標
     * @param y 開始y座標
     * @param w 切り抜く幅
     * @param h 切り抜く高さ
     * @return 切り抜いた画像
     */
    public static Bitmap crop(Bitmap bitmap, int x, int y, int w, int h){
        return Bitmap.createBitmap(bitmap, x, y, w, h);
    }

    /**
     * 画像を合成する
     * 
     * @param base
     * @param overlay
     */
    public static Bitmap synthesize(Bitmap base, Bitmap overlay){
        Bitmap synthesized = BitmapUtils.createNewBitmap(base.getWidth(), base.getHeight());
        Canvas canvas = new Canvas(synthesized);
        canvas.drawBitmap(base, 0, 0, null);
        canvas.drawBitmap(overlay, 0, 0, null);
        base.recycle();
        overlay.recycle();
        return synthesized;
    }

    /**
     * 画像を合成する
     * 
     * @param base
     * @param overlay
     * @param mode
     */
    public static Bitmap synthesize(Bitmap base, Bitmap overlay, PorterDuff.Mode mode){
        Bitmap synthesized = BitmapUtils.createNewBitmap(base.getWidth(), base.getHeight());
        Canvas canvas = new Canvas(synthesized);
        Paint paint = new Paint();
        canvas.drawBitmap(base, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawBitmap(overlay, 0, 0, paint);
        base.recycle();
        overlay.recycle();
        return synthesized;
    }

    /**
     * 画像を BASE64 エンコードする
     * 
     * @param bitmap
     * @return
     */
    public static String toBase64(Bitmap bitmap){
        return Base64.encodeToString(BitmapUtils.toByteArray(bitmap), Base64.DEFAULT);
    }

    /**
     * Bitmap to byte[]
     * 
     * @param bitmap
     * @return
     */
    public static byte[] toByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, MediaUtils.DEFAULT_COMPRESS_QUALITY, stream);
        return stream.toByteArray();
    }

}
