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
package at.wada811.widget;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import at.wada811.android.library.R;
import at.wada811.utils.LogUtils;

public class Toaster {

    public static final String TAG = Toaster.class.getSimpleName();

    private ToasterService mToasterService;
    private ToasterServiceConnection mConnection = new ToasterServiceConnection();

    private Context mContext;

    private class ToasterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            LogUtils.d();
            mToasterService = ((ToasterService.ToasterServiceBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name){
            LogUtils.d();
            mToasterService = null;
        }
    };

    public Toaster(Context context) {
        LogUtils.d();
        mContext = context;
        if(mToasterService == null){
            LogUtils.d();
            boolean bindService = context.bindService(new Intent(context, ToasterService.class), mConnection, Context.BIND_AUTO_CREATE);
            LogUtils.d("bindService: " + bindService);
        }
    }

    public void unplug(){
        LogUtils.d();
        if(mConnection != null){
            LogUtils.d();
            mToasterService.cancelAllBread();
            mContext.unbindService(mConnection);
            mToasterService = null;
            mConnection = null;
        }
    }

    public ToastBread newToast(){
        return new ToastBread(mContext);
    }

    public ToastBread newToast(OnDismissListener listener){
        return new ToastBread(mContext, listener);
    }

    public class ToastBread {
        private Bread mBread;
        private int mDuration;
        private View mNextView;
        private OnDismissListener mDismissListener;

        private ToastBread(Context context) {
            mBread = new Bread();
            mBread.mY = context.getResources().getDimensionPixelSize(R.dimen.toasterBaselineHeight);
        }

        private ToastBread(Context context, OnDismissListener listener) {
            mDismissListener = listener;
            mBread = new Bread();
            mBread.mY = context.getResources().getDimensionPixelSize(R.dimen.toasterBaselineHeight);
        }

        /**
         * Show the view for the specified duration.
         */
        public void show(){
            if(mNextView == null){
                throw new RuntimeException("setView must have been called");
            }

            Bread bread = mBread;
            bread.mNextView = mNextView;

            if(mToasterService != null){
                mToasterService.enqueueBread(bread, mDuration);
            }
        }

        /**
         * Close the view if it's showing, or don't show it if it isn't showing yet.
         * You do not normally have to call this. Normally view will disappear on its own
         * after the appropriate duration.
         */
        public void cancel(){
            mBread.hide();
            if(mToasterService != null){
                mToasterService.cancelBread(mBread);
            }
        }

        /**
         * Set the view to show.
         * 
         * @see #getView
         */
        public void setView(View view){
            mNextView = view;
        }

        /**
         * Return the view.
         * 
         * @see #setView
         */
        public View getView(){
            return mNextView;
        }

        /**
         * Set how long to show the view for.
         * 
         * @see #LENGTH_SHORT
         * @see #LENGTH_LONG
         */
        public void setDuration(int duration){
            mDuration = duration;
        }

        /**
         * Return the duration.
         * 
         * @see #setDuration
         */
        public int getDuration(){
            return mDuration;
        }

        /**
         * Set the margins of the view.
         * 
         * @param horizontalMargin The horizontal margin, in percentage of the
         *        container width, between the container's edges and the
         *        notification
         * @param verticalMargin The vertical margin, in percentage of the
         *        container height, between the container's edges and the
         *        notification
         */
        public void setMargin(float horizontalMargin, float verticalMargin){
            mBread.mHorizontalMargin = horizontalMargin;
            mBread.mVerticalMargin = verticalMargin;
        }

        /**
         * Return the horizontal margin.
         */
        public float getHorizontalMargin(){
            return mBread.mHorizontalMargin;
        }

        /**
         * Return the vertical margin.
         */
        public float getVerticalMargin(){
            return mBread.mVerticalMargin;
        }

        /**
         * Set the location at which the notification should appear on the screen.
         * 
         * @see android.view.Gravity
         * @see #getGravity
         */
        public void setGravity(int gravity, int xOffset, int yOffset){
            mBread.mGravity = gravity;
            mBread.mX = xOffset;
            mBread.mY = yOffset;
        }

        /**
         * Get the location at which the notification should appear on the screen.
         * 
         * @see android.view.Gravity
         * @see #getGravity
         */
        public int getGravity(){
            return mBread.mGravity;
        }

        /**
         * Return the X offset in pixels to apply to the gravity's location.
         */
        public int getXOffset(){
            return mBread.mX;
        }

        /**
         * Return the Y offset in pixels to apply to the gravity's location.
         */
        public int getYOffset(){
            return mBread.mY;
        }

        /**
         * Make a standard toast that just contains a text view.
         * 
         * @param context The context to use. Usually your {@link android.app.Application} or
         *        {@link android.app.Activity} object.
         * @param text The text to show. Can be formatted text.
         * @param duration How long to display the message. Either {@link #LENGTH_SHORT} or
         *        {@link #LENGTH_LONG}
         * 
         */
        public ToastBread makeText(Context context, CharSequence text, int duration){
            LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflate.inflate(R.layout.toast, null);
            TextView tv = (TextView)v.findViewById(R.id.message);
            tv.setText(text);

            mNextView = v;
            mDuration = duration;
            return this;
        }

        /**
         * Make a standard toast that just contains a text view with the text from a resource.
         * 
         * @param context The context to use. Usually your {@link android.app.Application} or
         *        {@link android.app.Activity} object.
         * @param resId The resource id of the string resource to use. Can be formatted text.
         * @param duration How long to display the message. Either {@link #LENGTH_SHORT} or
         *        {@link #LENGTH_LONG}
         * 
         * @throws Resources.NotFoundException if the resource can't be found.
         */
        public ToastBread makeText(Context context, int resId, int duration) throws Resources.NotFoundException{
            return makeText(context, context.getResources().getText(resId), duration);
        }

        /**
         * Update the text in a Toast that was previously created using one of the makeText()
         * methods.
         * 
         * @param resId The new text for the Toast.
         */
        public void setText(int resId){
            setText(mContext.getText(resId));
        }

        /**
         * Update the text in a Toast that was previously created using one of the makeText()
         * methods.
         * 
         * @param s The new text for the Toast.
         */
        public void setText(CharSequence s){
            if(mNextView == null){
                throw new RuntimeException("This Toast was not created with Toast.makeText()");
            }
            TextView tv = (TextView)mNextView.findViewById(R.id.message);
            if(tv == null){
                throw new RuntimeException("This Toast was not created with Toast.makeText()");
            }
            tv.setText(s);
        }

        public void setAnimationStyle(int animationStyle){
            mBread.mAnimationStyle = animationStyle;
        }

        public class Bread implements ToasterCallback {
            public Bread() {
                // XXX This should be changed to use a Dialog, with a Theme.
                // Toast defined that sets up the layout params appropriately.
                final WindowManager.LayoutParams params = mParams;
                params.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
                params.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
                params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                params.format = PixelFormat.TRANSLUCENT;
                params.windowAnimations = android.R.style.Animation_Toast;
                params.type = WindowManager.LayoutParams.TYPE_TOAST;
                params.setTitle("Toast");
            }
            final Runnable mShow = new Runnable(){
                @Override
                public void run(){
                    handleShow();
                }
            };

            final Runnable mHide = new Runnable(){
                @Override
                public void run(){
                    handleHide();
                    // Don't do this in handleHide() because it is also invoked by handleShow()
                    mNextView = null;
                }
            };

            private final LayoutParams mParams = new WindowManager.LayoutParams();
            final Handler mHandler = new Handler();

            int mGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            int mX, mY;
            float mHorizontalMargin;
            float mVerticalMargin;
            int mAnimationStyle = android.R.style.Animation_Toast;

            View mView;
            View mNextView;

            WindowManager mWindowManager;

            @Override
            public void show(){
                LogUtils.d();
                mHandler.post(mShow);
            }

            @Override
            public void hide(){
                LogUtils.d();
                mHandler.post(mHide);
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void handleShow(){
                if(mView != mNextView){
                    // remove the old view if necessary
                    handleHide();
                    mView = mNextView;
                    Context context = mView.getContext().getApplicationContext();
                    if(context == null){
                        context = mView.getContext();
                    }
                    mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
                    // We can resolve the Gravity here by using the Locale for getting
                    // the layout direction
                    final Configuration config = mView.getContext().getResources().getConfiguration();
                    final int gravity = Gravity.getAbsoluteGravity(mGravity, config.getLayoutDirection());
                    mParams.gravity = gravity;
                    if((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL){
                        mParams.horizontalWeight = 1.0f;
                    }
                    if((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL){
                        mParams.verticalWeight = 1.0f;
                    }
                    mParams.x = mX;
                    mParams.y = mY;
                    mParams.verticalMargin = mVerticalMargin;
                    mParams.horizontalMargin = mHorizontalMargin;
                    mParams.windowAnimations = mAnimationStyle;
                    if(mView.getParent() != null){
                        mWindowManager.removeView(mView);
                    }
                    mWindowManager.addView(mView, mParams);
                    trySendAccessibilityEvent();
                }
            }

            private void trySendAccessibilityEvent(){
                AccessibilityManager accessibilityManager = (AccessibilityManager)mView.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
                if(!accessibilityManager.isEnabled()){
                    return;
                }
                // treat toasts as notifications since they are used to
                // announce a transient piece of information to the user
                AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
                event.setClassName(getClass().getName());
                event.setPackageName(mView.getContext().getPackageName());
                mView.dispatchPopulateAccessibilityEvent(event);
                accessibilityManager.sendAccessibilityEvent(event);
            }

            public void handleHide(){
                if(mView != null){
                    // note: checking parent() just to make sure the view has
                    // been added...  i have seen cases where we get here when
                    // the view isn't yet added, so let's try not to crash.
                    if(mView.getParent() != null){
                        ;
                        mDismissListener = null;
                    }
                    if(mDismissListener != null){
                        mDismissListener.onDismiss();
                        mDismissListener = null;
                    }

                    mView = null;
                }
            }
        }
    }

    public static interface OnDismissListener {
        public void onDismiss();
    }

    protected static interface ToasterCallback {

        public void show();

        public void hide();

    }

}
