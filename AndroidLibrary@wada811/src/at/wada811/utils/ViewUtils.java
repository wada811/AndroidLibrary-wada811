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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
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
}
