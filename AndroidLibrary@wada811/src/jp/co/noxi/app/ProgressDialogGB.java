/*
 * Copyright (C) 2013 noxi
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

/*
 * Modified from: android.app.ProgressDialog
 *  (The Android Open Source Project: android-2.3.7_r1)
 */

package jp.co.noxi.app;

import java.text.NumberFormat;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * <p>
 * A dialog showing a progress indicator and an optional text message or view. Only a text message
 * or a view can be used at the same time.
 * </p>
 * <p>
 * The dialog can be made cancelable on back key press.
 * </p>
 * <p>
 * The progress range is 0..10000.
 * </p>
 */
final class ProgressDialogGB extends AlertDialog {

    private ProgressBar  mProgress;
    private TextView     mMessageView;

    private int          mProgressStyle        = NXProgressDialog.STYLE_SPINNER;
    private TextView     mProgressNumber;
    private String       mProgressNumberFormat = "%d/%d";
    private TextView     mProgressPercent;
    private NumberFormat mProgressPercentFormat;

    private int          mMax;
    private int          mProgressVal;
    private int          mSecondaryProgressVal;
    private int          mIncrementBy;
    private int          mIncrementSecondaryBy;
    private Drawable     mProgressDrawable;
    private Drawable     mIndeterminateDrawable;
    private CharSequence mMessage;
    private boolean      mIndeterminate;

    private boolean      mHasStarted;
    private Handler      mViewUpdateHandler;

    public ProgressDialogGB(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if(mProgressStyle == NXProgressDialog.STYLE_HORIZONTAL){

            /*
             * Use a separate handler to update the text views as they
             * must be updated on the same thread that created them.
             */
            mViewUpdateHandler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    super.handleMessage(msg);

                    /* Update the number and percent */
                    int progress = mProgress.getProgress();
                    int max = mProgress.getMax();
                    double percent = (double)progress / (double)max;
                    String format = mProgressNumberFormat;
                    mProgressNumber.setText(String.format(format, progress, max));
                    SpannableString tmp = new SpannableString(mProgressPercentFormat.format(percent));
                    tmp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, tmp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mProgressPercent.setText(tmp);
                }
            };
            mProgressPercentFormat = NumberFormat.getPercentInstance();
            mProgressPercentFormat.setMaximumFractionDigits(0);
            setView(getHorizontalView());
        }else{
            setView(getSpinnerView());
        }
        if(mMax > 0){
            setMax(mMax);
        }
        if(mProgressVal > 0){
            setProgress(mProgressVal);
        }
        if(mSecondaryProgressVal > 0){
            setSecondaryProgress(mSecondaryProgressVal);
        }
        if(mIncrementBy > 0){
            incrementProgressBy(mIncrementBy);
        }
        if(mIncrementSecondaryBy > 0){
            incrementSecondaryProgressBy(mIncrementSecondaryBy);
        }
        if(mProgressDrawable != null){
            setProgressDrawable(mProgressDrawable);
        }
        if(mIndeterminateDrawable != null){
            setIndeterminateDrawable(mIndeterminateDrawable);
        }
        if(mMessage != null){
            setMessage(mMessage);
        }
        setIndeterminate(mIndeterminate);
        onProgressChanged();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();
        mHasStarted = true;
    }

    @Override
    protected void onStop(){
        super.onStop();
        mHasStarted = false;
    }

    public void setProgress(int value){
        if(mHasStarted){
            mProgress.setProgress(value);
            onProgressChanged();
        }else{
            mProgressVal = value;
        }
    }

    public void setSecondaryProgress(int secondaryProgress){
        if(mProgress != null){
            mProgress.setSecondaryProgress(secondaryProgress);
            onProgressChanged();
        }else{
            mSecondaryProgressVal = secondaryProgress;
        }
    }

    public int getProgress(){
        if(mProgress != null){
            return mProgress.getProgress();
        }
        return mProgressVal;
    }

    public int getSecondaryProgress(){
        if(mProgress != null){
            return mProgress.getSecondaryProgress();
        }
        return mSecondaryProgressVal;
    }

    public int getMax(){
        if(mProgress != null){
            return mProgress.getMax();
        }
        return mMax;
    }

    public void setMax(int max){
        if(mProgress != null){
            mProgress.setMax(max);
            onProgressChanged();
        }else{
            mMax = max;
        }
    }

    public void incrementProgressBy(int diff){
        if(mProgress != null){
            mProgress.incrementProgressBy(diff);
            onProgressChanged();
        }else{
            mIncrementBy += diff;
        }
    }

    public void incrementSecondaryProgressBy(int diff){
        if(mProgress != null){
            mProgress.incrementSecondaryProgressBy(diff);
            onProgressChanged();
        }else{
            mIncrementSecondaryBy += diff;
        }
    }

    public void setProgressDrawable(Drawable d){
        if(mProgress != null){
            mProgress.setProgressDrawable(d);
        }else{
            mProgressDrawable = d;
        }
    }

    public void setIndeterminateDrawable(Drawable d){
        if(mProgress != null){
            mProgress.setIndeterminateDrawable(d);
        }else{
            mIndeterminateDrawable = d;
        }
    }

    public void setIndeterminate(boolean indeterminate){
        if(mProgress != null){
            mProgress.setIndeterminate(indeterminate);
        }else{
            mIndeterminate = indeterminate;
        }
    }

    public boolean isIndeterminate(){
        if(mProgress != null){
            return mProgress.isIndeterminate();
        }
        return mIndeterminate;
    }

    @Override
    public void setMessage(CharSequence message){
        if(mProgress != null){
            if(mProgressStyle == NXProgressDialog.STYLE_HORIZONTAL){
                super.setMessage(message);
            }else{
                mMessageView.setText(message);
            }
        }else{
            mMessage = message;
        }
    }

    public void setProgressStyle(int style){
        mProgressStyle = style;
    }

    /**
     * Change the format of Progress Number. The default is "current/max".
     * Should not be called during the number is progressing.
     * 
     * @param format Should contain two "%d". The first is used for current number
     *        and the second is used for the maximum.
     */
    public void setProgressNumberFormat(String format){
        mProgressNumberFormat = format;
    }

    private void onProgressChanged(){
        if(mProgressStyle == NXProgressDialog.STYLE_HORIZONTAL){
            mViewUpdateHandler.sendEmptyMessage(0);
        }
    }

    private View getSpinnerView(){
        final Context context = getContext();
        final float density = context.getResources().getDisplayMetrics().density;

        final FrameLayout root = new FrameLayout(context);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        final int paddingH = (int)(8 * density);
        final int paddingV = (int)(10 * density);
        final LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setBaselineAligned(false);
        container.setPadding(paddingH, paddingV, paddingH, paddingV);
        root.addView(container);

        final LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressParams.rightMargin = (int)(12 * density);
        final ProgressBar progress = mProgress = new ProgressBar(context, null, android.R.attr.progressBarStyle);
        progress.setLayoutParams(progressParams);
        progress.setId(android.R.id.progress);
        progress.setMax(10000);
        container.addView(progress);

        final LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        messageParams.gravity = Gravity.CENTER_VERTICAL;
        final TextView message = mMessageView = new TextView(context);
        message.setLayoutParams(messageParams);
        message.setId(android.R.id.message);
        container.addView(message);

        return root;
    }

    private View getHorizontalView(){
        final Context context = getContext();
        final float density = context.getResources().getDisplayMetrics().density;

        final RelativeLayout root = new RelativeLayout(context);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        final int marginH = (int)(10 * density);
        final int marginT = (int)(12 * density);
        final RelativeLayout.LayoutParams progressParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        progressParams.topMargin = marginT;
        progressParams.bottomMargin = (int)(density + 0.5f);
        progressParams.leftMargin = marginH;
        progressParams.rightMargin = marginH;
        final ProgressBar progress = mProgress = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progress.setLayoutParams(progressParams);
        progress.setId(android.R.id.progress);
        root.addView(progress);

        final RelativeLayout.LayoutParams percentParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        percentParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        percentParams.addRule(RelativeLayout.BELOW, android.R.id.progress);
        percentParams.leftMargin = marginH;
        percentParams.rightMargin = marginH;
        final TextView percent = mProgressPercent = new TextView(context);
        percent.setLayoutParams(percentParams);
        root.addView(percent);

        final RelativeLayout.LayoutParams numberParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        numberParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        numberParams.addRule(RelativeLayout.BELOW, android.R.id.progress);
        numberParams.leftMargin = marginH;
        numberParams.rightMargin = marginH;
        final TextView number = mProgressNumber = new TextView(context);
        number.setLayoutParams(numberParams);
        root.addView(number);

        return root;
    }
}
