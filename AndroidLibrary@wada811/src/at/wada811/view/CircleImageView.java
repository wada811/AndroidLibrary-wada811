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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.util.AttributeSet;
import android.widget.ImageView;
import at.wada811.android.library.R;
import at.wada811.utils.LogUtils;

public class CircleImageView extends ImageView {

    private float   mX      = 0;
    private float   mY      = 0;
    private int     mR      = -1;
    private boolean mIsClip = true;

    /**
     * プログラムから動的に生成する場合に使われる<br>
     * 
     * CircleImageView circleImageView = new CircleImageView(this);
     */
    public CircleImageView(Context context) {
        super(context);
    }

    /**
     * レイアウトファイルから生成する場合に使われる<br>
     * 
     * <at.wada811.view.CircleImageView
     * android:layout_width="MATCH_PARENT"
     * android:layout_height="MATCH_PARENT"
     * />
     * 指定した属性値が attrs に入る
     */
    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * いまいち謎
     */
    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attrsArray = getContext().obtainStyledAttributes(attrs, R.styleable.circle_view, defStyle, 0);
        mX = attrsArray.getFloat(R.styleable.circle_view_center_x, mX);
        mY = attrsArray.getFloat(R.styleable.circle_view_center_y, mY);
        mR = attrsArray.getInt(R.styleable.circle_view_radius, mR);
        // StyledAttributes should be recycled! 
        attrsArray.recycle();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas){
        if(mIsClip){
            Path path = new Path();
            path.addCircle(getWidth() / 2 + mX, getHeight() / 2 + mY, mR, Direction.CCW);
            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        if(mR * mR * 4 > measuredWidth * measuredWidth + measuredHeight * measuredHeight){
            LogUtils.v("mR * mR * 4 > measuredWidth * measuredWidth + measuredHeight * measuredHeight: " + mR * mR * 4 + " > " + measuredWidth * measuredWidth + measuredHeight * measuredHeight);
            mIsClip = false;
        }
        LogUtils.v("measuredWidth: " + measuredWidth);
        LogUtils.v("measuredHeight: " + measuredHeight);
        LogUtils.v("mR: " + mR);
        setMeasuredDimension(measuredWidth, measuredHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
        LogUtils.v("changed: " + changed);
        LogUtils.v("left: " + left);
        LogUtils.v("top: " + top);
        LogUtils.v("right: " + right);
        LogUtils.v("bottom: " + bottom);
        LogUtils.v("width: " + (right - left));
        LogUtils.v("height: " + (bottom - top));
        if(changed && mR == -1){
            LogUtils.v();
            int width = right - left;
            int height = bottom - top;
            mR = Math.min(width, height) / 2;
            LogUtils.v("mR: " + mR);
        }
    }

    public void setRadius(int radius){
        mR = radius;
    }

    public int getRadius(){
        return mR;
    }

    public void setCenterX(float centerX){
        mX = centerX;
    }

    public float getCenterX(){
        return mX;
    }

    public void setCenterY(float centerY){
        mY = centerY;
    }

    public float getCenterY(){
        return mY;
    }

}
