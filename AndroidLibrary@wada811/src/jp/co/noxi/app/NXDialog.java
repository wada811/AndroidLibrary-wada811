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

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

/**
 * @author noxi
 * @see android.app.Dialog
 * @see android.app.DialogFragment
 */
public class NXDialog extends DialogFragment implements NXDialogInterface {

    static final String         TAG_ACTIVITY            = "activity";
    static final String         TAG_FRAGMENT            = "fragment:";

    /**
     * Value for {@code null} or not set.
     */
    static final int            VALUE_NULL              = 0;
    /**
     * Value for {@code true}.
     */
    static final int            VALUE_TRUE              = 1;
    /**
     * Value for {@code false}.
     */
    static final int            VALUE_FALSE             = 2;

    private static final String SAVED_KEY_LISTENER      = "nx:dialog.keyListener";
    private static final String SAVED_SHOW_LISTENER     = "nx:dialog.showListener";
    private static final String SAVED_CANCEL_LISTENER   = "nx:dialog.cancelListener";
    private static final String SAVED_DISMISS_LISTENER  = "nx:dialog.dismissListener";
    private static final String SAVED_TOUCH_OUTSIDE     = "nx:dialog.touchOutside";
    private static final String SAVED_EXTRA             = "nx:dialog.extra";

    OnKeyListener               mKeyListener;
    OnShowListener              mShowListener;
    OnCancelListener            mCancelListener;
    OnDismissListener           mDismissListener;

    int                         mCanceledOnTouchOutside = VALUE_NULL;
    Bundle                      mExtra;

    public NXDialog() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        // Load date from savedState
        if(savedInstanceState != null){
            mKeyListener = findListenerByTag(OnKeyListener.class, savedInstanceState, SAVED_KEY_LISTENER);
            mShowListener = findListenerByTag(OnShowListener.class, savedInstanceState, SAVED_SHOW_LISTENER);
            mCancelListener = findListenerByTag(OnCancelListener.class, savedInstanceState, SAVED_CANCEL_LISTENER);
            mDismissListener = findListenerByTag(OnDismissListener.class, savedInstanceState, SAVED_DISMISS_LISTENER);

            mCanceledOnTouchOutside = savedInstanceState.getInt(SAVED_TOUCH_OUTSIDE, VALUE_NULL);
            mExtra = savedInstanceState.getBundle(SAVED_EXTRA);
        }

        setOnKeyListenerInternal(mKeyListener);
        setOnShowListenerInternal(mShowListener);
        setOnCancelListenerInternal(mCancelListener);
        setOnDismissListenerInternal(mDismissListener);

        if(mCanceledOnTouchOutside != VALUE_NULL){
            getDialog().setCanceledOnTouchOutside(mCanceledOnTouchOutside == VALUE_TRUE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        // Listener
        saveState(outState, SAVED_KEY_LISTENER, mKeyListener);
        saveState(outState, SAVED_SHOW_LISTENER, mShowListener);
        saveState(outState, SAVED_CANCEL_LISTENER, mCancelListener);
        saveState(outState, SAVED_DISMISS_LISTENER, mDismissListener);

        // Other
        if(mCanceledOnTouchOutside != VALUE_NULL){
            outState.putInt(SAVED_TOUCH_OUTSIDE, mCanceledOnTouchOutside);
        }
        if(mExtra != null){
            outState.putBundle(SAVED_EXTRA, mExtra);
        }
    }

    @Override
    public void onDestroy(){
        mKeyListener = null;
        mShowListener = null;
        mCancelListener = null;
        mDismissListener = null;
        mExtra = null;

        super.onDestroy();
    }

    @Override
    public void onCancel(DialogInterface dialog){
        super.onCancel(dialog);
        if(mCancelListener != null){
            mCancelListener.onCancel(this);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        if(mDismissListener != null){
            mDismissListener.onDismiss(this);
        }
    }

    /**
     * Sets whether this dialog is canceled when touched outside the window's bounds.
     * If setting to true, the dialog is set to be cancelable if not already set.
     * 
     * @param cancel Whether the dialog should be canceled when touched outside the window.
     */
    public void setCanceledOnTouchOutside(boolean cancel){
        mCanceledOnTouchOutside = cancel ? VALUE_TRUE : VALUE_FALSE;
    }

    /**
     * Sets the callback that will be called if a key is dispatched to the dialog.
     * 
     * @param listener The {@link OnKeyListener} to use.
     * @param <T> A class which extends {@link Fragment} and implements {@link OnKeyListener}.
     */
    public <T extends Fragment & OnKeyListener>void setOnKeyListener(T listener){
        mKeyListener = listener;
        setOnKeyListenerInternal(listener);
    }

    /**
     * Sets the callback that will be called if a key is dispatched to the dialog.
     * 
     * @param listener The {@link OnKeyListener} to use.
     * @param <T> A class which extends {@link Activity} and implements {@link OnKeyListener}.
     */
    public <T extends Activity & OnKeyListener>void setOnKeyListener(T listener){
        mKeyListener = listener;
        setOnKeyListenerInternal(listener);
    }

    void setOnKeyListenerInternal(final OnKeyListener listener){
        final Dialog dialog = getDialog();
        if((listener == null) || (dialog == null)){
            return;
        }

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener(){
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event){
                return listener.onKey(NXDialog.this, keyCode, event);
            }
        });
    }

    /**
     * Sets a listener to be invoked when the dialog is shown.
     * 
     * @param listener The {@link OnShowListener} to use.
     * @param <T> A class which extends {@link Fragment} and implements {@link OnShowListener}.
     */
    public <T extends Fragment & OnShowListener>void setOnShowListener(T listener){
        mShowListener = listener;
        setOnShowListenerInternal(listener);
    }

    /**
     * Sets a listener to be invoked when the dialog is shown.
     * 
     * @param listener The {@link OnShowListener} to use.
     * @param <T> A class which extends {@link Activity} and implements {@link OnShowListener}.
     */
    public <T extends Activity & OnShowListener>void setOnShowListener(T listener){
        mShowListener = listener;
        setOnShowListenerInternal(listener);
    }

    void setOnShowListenerInternal(final OnShowListener listener){
        final Dialog dialog = getDialog();
        if((listener == null) || (dialog == null)){
            return;
        }

        dialog.setOnShowListener(new DialogInterface.OnShowListener(){
            @Override
            public void onShow(DialogInterface dialog){
                listener.onShow(NXDialog.this);
            }
        });
    }

    /**
     * Set a listener to be invoked when the dialog is canceled.
     * 
     * @param listener The {@link OnCancelListener} to use.
     * @param <T> A class which extends {@link Fragment} and implements {@link OnCancelListener}.
     */
    public <T extends Fragment & OnCancelListener>void setOnCancelListener(T listener){
        mCancelListener = listener;
        setOnCancelListenerInternal(listener);
    }

    /**
     * Set a listener to be invoked when the dialog is canceled.
     * 
     * @param listener The {@link OnCancelListener} to use.
     * @param <T> A class which extends {@link Activity} and implements {@link OnCancelListener}.
     */
    public <T extends Activity & OnCancelListener>void setOnCancelListener(T listener){
        mCancelListener = listener;
        setOnCancelListenerInternal(listener);
    }

    void setOnCancelListenerInternal(final OnCancelListener listener){
        final Dialog dialog = getDialog();
        if((listener == null) || (dialog == null)){
            return;
        }

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog){
                listener.onCancel(NXDialog.this);
            }
        });
    }

    /**
     * Set a listener to be invoked when the dialog is dismissed.
     * 
     * @param listener The {@link OnDismissListener} to use.
     * @param <T> A class which extends {@link Fragment} and implements {@link OnDismissListener}.
     */
    public <T extends Fragment & OnDismissListener>void setOnDismissListener(T listener){
        mDismissListener = listener;
        setOnDismissListenerInternal(listener);
    }

    /**
     * Set a listener to be invoked when the dialog is dismissed.
     * 
     * @param listener The {@link OnDismissListener} to use.
     * @param <T> A class which extends {@link Activity} and implements {@link OnDismissListener}.
     */
    public <T extends Activity & OnDismissListener>void setOnDismissListener(T listener){
        mDismissListener = listener;
        setOnDismissListenerInternal(listener);
    }

    void setOnDismissListenerInternal(final OnDismissListener listener){
        final Dialog dialog = getDialog();
        if((listener == null) || (dialog == null)){
            return;
        }

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog){
                listener.onDismiss(NXDialog.this);
            }
        });
    }

    /**
     * Set an extra object. This has no effect to dialog behavior.
     */
    public void setExtra(Bundle extra){
        mExtra = extra;
    }

    /**
     * Get an extra object you set before.
     */
    @Override
    public Bundle getExtra(){
        return mExtra;
    }

    final <T>T findListenerByTag(Class<T> clss, Bundle args, String argName){
        final String target = args.getString(argName);
        if(target == null){
            return null;
        }else if(TAG_ACTIVITY.equals(target)){
            return findListener(clss, getActivity());
        }else if(target.startsWith(TAG_FRAGMENT)){
            return findListener(clss, getFragmentManager().findFragmentByTag(target.substring(TAG_FRAGMENT.length())));
        }else{
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    final <T>T findListener(Class<T> clss, Object object){
        if((object != null) && clss.isInstance(object)){
            return (T)object;
        }
        return null;
    }

    static void saveState(Bundle state, String key, Object object){
        if(object == null){
            return;
        }
        if(object instanceof Activity){
            state.putString(key, TAG_ACTIVITY);
        }else if(object instanceof Fragment){
            final String tag = ((Fragment)object).getTag();
            if((tag != null) && !tag.isEmpty()){
                state.putString(key, TAG_FRAGMENT + tag);
            }
        }
    }

}
