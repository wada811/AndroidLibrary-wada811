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
package at.wada811.view;

import java.util.EventListener;
import android.view.MotionEvent;
import android.view.View;
import at.wada811.utils.LogUtils;

public class SwipeTouchListener implements View.OnTouchListener {

    /** x coordinate on MotionEvent.ACTION_DOWN */
    private float                mDownX;
    /** y coordinate on MotionEvent.ACTION_DOWN */
    private float                mDownY;
    /** x coordinate on MotionEvent.ACTION_UP */
    private float                mUpX;
    /** y coordinate on MotionEvent.ACTION_UP */
    private float                mUpY;
    /** 最低限移動しないといけない距離 */
    protected static final float MIN_DISTANCE  = 300;
    /** event time on MotionEvent.ACTION_DOWN */
    private long                 mDownTime;
    /** event time on MotionEvent.ACTION_UP */
    private long                 mUpTime;
    /** 最大移動時間 */
    protected static final long  MAX_MOVE_TIME = 300;

    private OnSwipeListener      mListener;

    public interface OnSwipeListener extends EventListener {

        public void onSwipeUp();

        public void onSwipeDown();

        public void onSwipeRight();

        public void onSwipeLeft();
    }

    public SwipeTouchListener(OnSwipeListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        // タッチされている指の本数
        LogUtils.v("motionEvent", "touch_count = " + event.getPointerCount());

        // タッチされている座標
        LogUtils.v("X", Float.toString(event.getX()));
        LogUtils.v("Y", Float.toString(event.getY()));

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: // タッチ
                LogUtils.v("motionEvent: ACTION_DOWN");
                mDownX = event.getX();
                mDownY = event.getY();
                mDownTime = event.getEventTime();
                return false;
            case MotionEvent.ACTION_POINTER_DOWN: // タッチ中に追加でタッチした場合
                LogUtils.v("motionEvent: ACTION_POINTER_DOWN");
                return true;
            case MotionEvent.ACTION_MOVE: // スライド
                LogUtils.v("motionEvent: ACTION_MOVE");
                return true;
            case MotionEvent.ACTION_UP: // タッチが離れた
                LogUtils.v("motionEvent: ACTION_UP");
                mUpX = event.getX();
                mUpY = event.getY();
                mUpTime = event.getEventTime();
                return detectSwipeDirection();
            case MotionEvent.ACTION_POINTER_UP: // アップ後にほかの指がタッチ中の場合
                LogUtils.v("motionEvent: ACTION_POINTER_UP");
                return true;
            case MotionEvent.ACTION_CANCEL: // UP+DOWNの同時発生(タッチのキャンセル）
                LogUtils.v("motionEvent: ACTION_CANCEL");
                return true;
            case MotionEvent.ACTION_OUTSIDE: // ターゲットとするUIの範囲外を押下
                LogUtils.v("motionEvent: ACTION_OUTSIDE");
                return true;
            default:
                return false;
        }
    }

    /**
     * スワイプ方向を検出してリスナーに通知する
     * 
     * @return スワイプ方向を検出できなかった場合はfalse
     */
    private boolean detectSwipeDirection(){
        // 左上を原点とし、右下が正
        float moveX = mDownX - mUpX; // 正: 左, 負: 右
        float moveY = mDownY - mUpY; // 正: 上, 負: 下
        LogUtils.d("moveX: " + moveX);
        LogUtils.d("moveY: " + moveY);
        // 移動量のチェック
        float absMoveX = Math.abs(moveX);
        float absMoveY = Math.abs(moveY);
        if(absMoveX < MIN_DISTANCE && absMoveY < MIN_DISTANCE){
            LogUtils.d("absMoveX < MIN_DISTANCE: " + absMoveX + " < " + MIN_DISTANCE);
            LogUtils.d("absMoveY < MIN_DISTANCE: " + absMoveY + " < " + MIN_DISTANCE);
            return false;
        }
        // 移動時間のチェック
        long moveTime = mUpTime - mDownTime;
        LogUtils.d("moveTime: " + moveTime);
        if(moveTime > MAX_MOVE_TIME){
            LogUtils.d("moveTime > MAX_MOVE_TIME: " + moveTime + " > " + MAX_MOVE_TIME);
            return false;
        }
        // 方向の決定
        if(absMoveX > absMoveY){ // 左右
            if(moveX > 0){ // 左
                mListener.onSwipeLeft();
            }else{ // 右
                mListener.onSwipeRight();
            }
        }else{ // 上下
            if(moveY > 0){ // 上
                mListener.onSwipeUp();
            }else{ // 下
                mListener.onSwipeDown();
            }
        }
        return true;
    }

}
