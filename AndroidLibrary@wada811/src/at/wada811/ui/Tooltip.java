package at.wada811.ui;

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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;
import at.wada811.android.library.R;

public class Tooltip extends PopupWindow implements Runnable {

    private Context mContext;
    private Handler mHandler        = new Handler();
    private int     mDuration       = -1;
    private int     mAnimationStyle = R.style.tooltipAnimation;

    public Tooltip(Context context) {
        this(context, null);
    }

    public Tooltip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Tooltip(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Tooltip(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
    }

    public Tooltip() {
        this(null, 0, 0);
    }

    public Tooltip(View contentView) {
        this(contentView, 0, 0);
    }

    public Tooltip(int width, int height) {
        this(null, width, height);
    }

    public Tooltip(View contentView, int width, int height) {
        this(contentView, width, height, false);
    }

    public Tooltip(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    @Override
    public void run(){
        dismiss();
    }

    @Override
    public void setContentView(View contentView){
        super.setContentView(contentView);
    }

    @Override
    public void setAnimationStyle(int animationStyle){
        mAnimationStyle = animationStyle;
        super.setAnimationStyle(animationStyle);
    }

    /**
     * @return 自動非表示されるまでの時間（アニメーション時間含まず）
     */
    public int getDuration(){
        return mDuration;
    }

    /**
     * @param duration 自動非表示されるまでの時間（アニメーション時間含まず）。-1で自動非表示無効。
     */
    public void setDuration(int duration){
        mDuration = duration;
    }

    public void show(){
        this.showAtLocation(Gravity.CENTER, 0, 0);
    }

    public void showAtLocation(int gravity, int x, int y){
        Window window = ((Activity)mContext).getWindow();
        View parent = window.findViewById(Window.ID_ANDROID_CONTENT);
        this.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y){
        // dismissImmediately() でアニメーションを無効にした場合に再設定する
        if(mAnimationStyle != -1){
            super.setAnimationStyle(mAnimationStyle);
        }
        if(mDuration > 0){ // durationが-1なら自動非表示しない
            mHandler.postDelayed(this, mDuration);
        }
        Window window = ((Activity)mContext).getWindow();
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        super.showAtLocation(parent, gravity, x, y + contentViewTop);
    }

    @Override
    public void dismiss(){
        mHandler.removeCallbacks(this);
        super.dismiss();
    }

    /**
     * アニメーションも無視して直ちに非表示にする
     */
    public void dismissImmediately(){
        super.setAnimationStyle(-1);
        update(); //これをしないとsuper.setAnimationStyle(-1);の効果が次のshow...まで反映されない
        dismiss();
    }

}
