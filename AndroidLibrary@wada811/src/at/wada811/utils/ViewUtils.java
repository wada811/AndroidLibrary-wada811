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

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.ImageView;

public class ViewUtils {

    /**
     * スクリーンショットを撮る
     * 
     * @param view 画面に attach された View
     * @return ステータスバーなどを含んだ画面全体の Bitmap を返す
     */
    public static Bitmap getScreenBitmap(View view){
        return ViewUtils.getViewBitmap(view.getRootView());
    }

    /**
     * View を Bitmap として取得する
     * 
     * @param view
     * @return bitmap
     */
    public static Bitmap getViewBitmap(View view){
        view.setDrawingCacheEnabled(true);
        Bitmap cache = view.getDrawingCache();
        if(cache == null){
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cache);
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * WebView を開放する。メモリリーク対策
     */
    public static void releaseWebView(WebView webview){
        webview.stopLoading();
        webview.setWebChromeClient(null);
        webview.setWebViewClient(null);
        webview.destroy();
        webview = null;
    }

    /**
     * WebView の キャッシュ、フォームデータ、履歴、Cookie を削除する
     */
    public static void clearLocalData(WebView webView){
        webView.clearCache(true);
        webView.clearFormData();
        webView.clearHistory();
        CookieManager.getInstance().removeAllCookie();
    }

    /**
     * ImageView を開放する。メモリリーク対策
     */
    public static void releaseImageView(ImageView imageView){
        if(imageView != null){
            BitmapDrawable bitmapDrawable = (BitmapDrawable)(imageView.getDrawable());
            if(bitmapDrawable != null){
                bitmapDrawable.setCallback(null);
            }
            imageView.setImageBitmap(null);
        }
    }

    /**
     * 画像の ColorFilter を設定する
     * 
     * @param context
     * @param imageView
     * @param resId
     */
    public static void setImageColorFilter(Context context, ImageView imageView, int resId){
        imageView.setColorFilter(context.getResources().getColor(resId));
    }

    /**
     * 画像の ColorFilter を設定する
     * 
     * @param imageView
     * @param hexColor ARGB
     */
    public static void setImageColorFilter(ImageView imageView, int hexColor){
        imageView.setColorFilter(hexColor);
    }

    /**
     * visible true: Viewの階層構造をダンプするスニペット http://visible-true.blogspot.jp/2012/02/view.html
     * 
     * @param view
     * @param padding
     */
    public static void dumpViewTree(View view, String padding){
        LogUtils.d(padding + view.getClass().getName());
        if(view instanceof ViewGroup){
            ViewGroup g = (ViewGroup)view;
            for(int i = 0; i < g.getChildCount(); i++){
                dumpViewTree(g.getChildAt(i), padding + " ");
            }
        }
    }

    /**
     * ステータスバーの高さを取得する
     * 
     * @param activity
     */
    public static int getStatusBarHeight(Activity activity){
        Rect rectgle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
        return rectgle.top;
    }

    /**
     * タイトルバーの高さを取得する
     * Androidの画面サイズを攻略して機種依存を吸収する(ナビゲーションバーとステータスバーのサイズを取得する) | Tech Booster
     * http://techbooster.org/android/hacks/16066/
     */
    public static int getTitleBarHeight(Activity activity){
        Rect rectgle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return contentViewTop - getStatusBarHeight(activity);
    }

    private void test(Activity activity){
        LogUtils.d("getStatusBarHeight: " + ViewUtils.getStatusBarHeight(activity));
        LogUtils.d("getTitleBarHeight: " + ViewUtils.getTitleBarHeight(activity));
        Rect rectgle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int contentViewLeft = window.findViewById(Window.ID_ANDROID_CONTENT).getLeft();
        int contentViewBottom = window.findViewById(Window.ID_ANDROID_CONTENT).getBottom();
        int contentViewRight = window.findViewById(Window.ID_ANDROID_CONTENT).getRight();
        LogUtils.d("contentViewTop: " + contentViewTop);
        LogUtils.d("contentViewLeft: " + contentViewLeft);
        LogUtils.d("contentViewBottom: " + contentViewBottom);
        LogUtils.d("contentViewRight: " + contentViewRight);
        int contentViewWidth = window.findViewById(Window.ID_ANDROID_CONTENT).getWidth();
        int contentViewHeight = window.findViewById(Window.ID_ANDROID_CONTENT).getHeight();
        LogUtils.d("contentViewWidth: " + contentViewWidth);
        LogUtils.d("contentViewHeight: " + contentViewHeight);
        int displayWidth = DisplayUtils.getWidth(activity);
        int displayHeight = DisplayUtils.getHeight(activity);
        LogUtils.d("displayWidth: " + displayWidth);
        LogUtils.d("displayHeight: " + displayHeight);
    }

    /**
     * コンテンツ部分の上部の座標を取得する
     */
    public static int getContentTop(Activity activity){
        return ViewUtils.getTitleBarHeight(activity) + ViewUtils.getStatusBarHeight(activity);
    }
}
