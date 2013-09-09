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

package jp.co.noxi.app;

import java.lang.ref.WeakReference;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * {@link android.support.v4.app.DialogFragment} like {@link ProgressDialog}.
 * 
 * @author noxi
 */
public class NXProgressDialog extends NXDialog {

    public static final int       STYLE_SPINNER                  = ProgressDialog.STYLE_SPINNER;
    public static final int       STYLE_HORIZONTAL               = ProgressDialog.STYLE_HORIZONTAL;

    protected static final String COMMIT_TAG                     = "nx:progressDialog";
    private static final String   SAVED_PROGRESS_STYLE           = "nx:progressDialog.style";
    private static final String   SAVED_MAX                      = "nx:progressDialog.max";
    private static final String   SAVED_PROGRESS_VALUE           = "nx:progressDialog.value";
    private static final String   SAVED_SECONDARY_PROGRESS_VALUE = "nx:progressDialog.secondaryValue";
    private static final String   SAVED_MESSAGE                  = "nx:progressDialog.message";
    private static final String   SAVED_INDETERMINATE            = "nx:progressDialog.indeterminate";
    private static final String   SAVED_FORMAT                   = "nx:progressDialog.format";

    /**
     * Find NXProgressDialog instance from saved fragments.
     * 
     * @return NXProgressDialog instance or {@code null}
     */
    public static NXProgressDialog find(FragmentManager fm){
        return (NXProgressDialog)fm.findFragmentByTag(COMMIT_TAG);
    }

    /**
     * Create a new NXProgressDialog instance with the selected style.
     * 
     * @param style The progress style {@link #STYLE_SPINNER} or {@link #STYLE_HORIZONTAL}
     */
    public static NXProgressDialog newInstance(int style){
        NXProgressDialog f = new NXProgressDialog();
        f.mStyle = style;
        return f;
    }

    private static class MessageUpdateHandler extends Handler {

        final WeakReference<NXProgressDialog> mParent;

        protected MessageUpdateHandler(NXProgressDialog dialog) {
            mParent = new WeakReference<NXProgressDialog>(dialog);
        }

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            final NXProgressDialog dialog = mParent.get();
            if((dialog == null) || ((dialog.mProgressDialog == null) && (dialog.mProgressDialogGB == null))){
                return;
            }

            if(dialog.mProgressDialog != null){
                dialog.mProgressDialog.setMessage(dialog.mMessage);
            }else{
                dialog.mProgressDialogGB.setMessage(dialog.mMessage);
            }
        }
    }

    private Handler          mHandler;
    private ProgressDialogGB mProgressDialogGB;
    private ProgressDialog   mProgressDialog;
    protected int            mStyle = NXProgressDialog.STYLE_SPINNER;
    protected int            mMax   = 100;
    protected int            mProgressVal;
    protected int            mSecondaryProgressVal;
    private CharSequence     mMessage;
    protected boolean        mIndeterminate;
    private String           mFormat;

    public NXProgressDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            mStyle = savedInstanceState.getInt(SAVED_PROGRESS_STYLE, NXProgressDialog.STYLE_SPINNER);
            mMax = savedInstanceState.getInt(SAVED_MAX, 100);
            mProgressVal = savedInstanceState.getInt(SAVED_PROGRESS_VALUE, 0);
            mSecondaryProgressVal = savedInstanceState.getInt(SAVED_SECONDARY_PROGRESS_VALUE, 0);
            mMessage = savedInstanceState.getCharSequence(SAVED_MESSAGE);
            mIndeterminate = savedInstanceState.getBoolean(SAVED_INDETERMINATE, false);
            mFormat = savedInstanceState.getString(SAVED_FORMAT);
        }

        mHandler = new MessageUpdateHandler(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            final ProgressDialog dialog = mProgressDialog = new ProgressDialog(getActivity());
            dialog.setProgressStyle(mStyle);
            dialog.setIndeterminate(mIndeterminate);
            if(mMax > 0){
                dialog.setMax(mMax);
            }
            if(mProgressVal > 0){
                dialog.setProgress(mProgressVal);
            }
            if(mSecondaryProgressVal > 0){
                dialog.setSecondaryProgress(mSecondaryProgressVal);
            }
            if(mMessage != null){
                dialog.setMessage(mMessage);
            }
            if(mFormat != null){
                dialog.setProgressNumberFormat(mFormat);
            }
            return dialog;
        }else{
            final ProgressDialogGB dialog = mProgressDialogGB = new ProgressDialogGB(getActivity());
            dialog.setProgressStyle(mStyle);
            dialog.setIndeterminate(mIndeterminate);
            if(mMax > 0){
                dialog.setMax(mMax);
            }
            if(mProgressVal > 0){
                dialog.setProgress(mProgressVal);
            }
            if(mSecondaryProgressVal > 0){
                dialog.setSecondaryProgress(mSecondaryProgressVal);
            }
            if(mMessage != null){
                dialog.setMessage(mMessage);
            }
            if(mFormat != null){
                dialog.setProgressNumberFormat(mFormat);
            }
            return dialog;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS_STYLE, mStyle);
        outState.putInt(SAVED_MAX, mMax);
        outState.putInt(SAVED_PROGRESS_VALUE, mProgressVal);
        outState.putInt(SAVED_SECONDARY_PROGRESS_VALUE, mSecondaryProgressVal);
        outState.putBoolean(SAVED_INDETERMINATE, mIndeterminate);
        if(mMessage != null){
            outState.putCharSequence(SAVED_MESSAGE, mMessage);
        }
        if(mFormat != null){
            outState.putString(SAVED_FORMAT, mFormat);
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mProgressDialog = null;
    }

    public void show(FragmentManager manager){
        super.show(manager, COMMIT_TAG);
    }

    public int show(FragmentTransaction transaction){
        return super.show(transaction, COMMIT_TAG);
    }

    @Override
    public void show(FragmentManager manager, String tag){
        super.show(manager, COMMIT_TAG);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag){
        return super.show(transaction, COMMIT_TAG);
    }

    public void setMax(int max){
        mMax = max;
        if(mProgressDialog != null){
            mProgressDialog.setMax(max);
        }else if(mProgressDialogGB != null){
            mProgressDialogGB.setMax(max);
        }
    }

    public void setProgress(int value){
        mProgressVal = value;
        if(mProgressDialog != null){
            mProgressDialog.setProgress(value);
        }else if(mProgressDialogGB != null){
            mProgressDialogGB.setProgress(value);
        }
    }

    public void setSecondaryProgress(final int secondaryProgress){
        mSecondaryProgressVal = secondaryProgress;
        if(mProgressDialog != null){
            mProgressDialog.setSecondaryProgress(secondaryProgress);
        }else if(mProgressDialogGB != null){
            mProgressDialogGB.setSecondaryProgress(secondaryProgress);
        }
    }

    public void setMessage(CharSequence message){
        mMessage = message;
        if((mHandler != null) && ((mProgressDialog != null) || (mProgressDialogGB != null))){
            mHandler.sendEmptyMessage(0);
        }
    }

    public void setIndeterminate(final boolean indeterminate){
        mIndeterminate = indeterminate;
        if(mProgressDialog != null){
            mProgressDialog.setIndeterminate(indeterminate);
        }else if(mProgressDialogGB != null){
            mProgressDialogGB.setIndeterminate(indeterminate);
        }
    }

    /**
     * Change the format of the small text showing current and maximum units
     * of progress. The default is "%1d/%2d".
     * Should not be called during the number is progressing.
     * 
     * @param format A string passed to {@link String#format String.format()};
     *        use "%1d" for the current number and "%2d" for the maximum.
     *        If null, nothing will be shown.
     */
    public void setProgressNumberFormat(String format){
        mFormat = format;
        if(mProgressDialog != null){
            mProgressDialog.setProgressNumberFormat(format);
        }else if(mProgressDialogGB != null){
            mProgressDialogGB.setProgressNumberFormat(format);
        }
    }

}
