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
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;

public class DisplayUtils {

    /**
     * ディスプレイを取得する
     * 
     * @param context
     * @return
     */
    private static Display getDisplay(Context context){
        return ((Activity)context).getWindowManager().getDefaultDisplay();
    }

    /**
     * ディスプレイの幅を返す
     * 
     * @param context
     * @return dispalyWidth
     */
    public static int getWidth(Context context){
        Display display = DisplayUtils.getDisplay(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        if(AndroidUtils.isMoreThanBuildVersion(Build.VERSION_CODES.HONEYCOMB_MR2)){
            return DisplayUtils.getWidth_HONEYCOMB_MR2(display);
        }else{
            return DisplayUtils.getWidth(display);
        }
    }

    @SuppressWarnings("deprecation")
    private static int getWidth(Display display){
        return display.getWidth();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private static int getWidth_HONEYCOMB_MR2(Display display){
        Point point = new Point();
        display.getSize(point);
        return point.x;
    }

    /**
     * ディスプレイの高さを返す
     * 
     * @param context
     * @return dispalyHeight
     */
    public static int getHeight(Context context){
        Display display = DisplayUtils.getDisplay(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        if(AndroidUtils.isMoreThanBuildVersion(Build.VERSION_CODES.HONEYCOMB_MR2)){
            return DisplayUtils.getHeight_HONEYCOMB_MR2(display);
        }else{
            return DisplayUtils.getHeight(display);
        }
    }

    @SuppressWarnings("deprecation")
    private static int getHeight(Display display){
        return display.getHeight();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private static int getHeight_HONEYCOMB_MR2(Display display){
        Point point = new Point();
        display.getSize(point);
        return point.y;
    }

    /**
     * ディスプレイのアスペクト比を返す
     * 
     * @param context
     * @return
     */
    public static double getDisplayRatio(Context context){
        int w = DisplayUtils.getWidth(context);
        int h = DisplayUtils.getHeight(context);
        return DisplayUtils.isPortrait(context) ? (double)h / w : (double)w / h;
    }

    /**
     * 画面の向きから回転角度を返す
     * 
     * @param context
     * @return degrees 回転角度
     */
    public static int getRotationDegrees(Context context){
        int rotation = DisplayUtils.getDisplay(context).getRotation();
        int degrees = 0;
        switch(rotation){
            case Surface.ROTATION_0: // Portrait :縦向き
                degrees = 0;
                break;
            case Surface.ROTATION_90: // Landscape:横向き
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        LogUtils.i("degrees: " + degrees);
        return degrees;
    }

    /**
     * ポートレートかどうかを返す
     * 
     * @param context
     * @return true if display orientation is portrait, otherwise false
     */
    public static boolean isPortrait(Context context){
        Configuration configuration = context.getResources().getConfiguration();
        return configuration.orientation == Configuration.ORIENTATION_PORTRAIT;
        // 画面角度は横向きタブレットで横向きが0度になるため
//        return DisplayUtil.getRotationDegrees(context) % 180 == 0;
    }

    /**
     * ディスプレイの幅(px)を返す
     * 
     * @param context
     * @return
     */
    public static int getWidthPixels(Context context){
        Display display = DisplayUtils.getDisplay(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * ディスプレイの高さ(px)を返す
     * 
     * @param context
     * @return
     */
    public static int getHeightPixels(Context context){
        Display display = DisplayUtils.getDisplay(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * ディスプレイのX方向 pixel per inch を返す
     * 
     * @param context
     * @return
     */
    public static float getXdpi(Context context){
        Display display = DisplayUtils.getDisplay(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics.xdpi;
    }

    /**
     * ディスプレイのY方向 pixel per inch を返す
     * 
     * @param context
     * @return
     */
    public static float getYdpi(Context context){
        Display display = DisplayUtils.getDisplay(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics.ydpi;
    }

    /**
     * ディスプレイの density を返す
     * 
     * @param context
     * @return
     */
    public static float getDensity(Context context){
        Display display = DisplayUtils.getDisplay(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics.density;
    }

    /**
     * ディスプレイの DPI({@link DisplayMetrics#DENSITY_HIGH}など) を返す
     * 
     * @param context
     * @return
     */
    public static int getDensityDpi(Context context){
        Display display = DisplayUtils.getDisplay(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics.densityDpi;
    }

}
