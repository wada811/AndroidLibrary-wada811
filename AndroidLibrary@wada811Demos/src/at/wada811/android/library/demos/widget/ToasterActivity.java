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
package at.wada811.android.library.demos.widget;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import at.wada811.android.library.demos.R;
import at.wada811.utils.ColorUtils;
import at.wada811.utils.LogUtils;
import at.wada811.widget.Toaster;
import at.wada811.widget.Toaster.OnDismissListener;
import at.wada811.widget.Toaster.ToastBread;

public class ToasterActivity extends Activity implements OnDismissListener {

    public static final String TAG = ToasterActivity.class.getSimpleName();
    private Toaster mToaster;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_toaster);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mToaster = new Toaster(this);
        showToast();
        final ToastBread toast = mToaster.newToast();
        toast.makeText(this, "test", 3000);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                LogUtils.d();
                toast.show();
            }
        }, 1000);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                LogUtils.d();
                toast.setText("1");
                toast.show();
            }
        }, 2000);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                LogUtils.d();
                toast.setText("2");
                toast.show();
            }
        }, 3000);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                LogUtils.d();
                toast.setText("3");
                toast.show();
            }
        }, 4000);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mToaster.unplug();
    }

    private void showToast(){
        final ToastBread toast = mToaster.newToast(this);
        TextView view = new TextView(this);
        view.setBackgroundColor(ColorUtils.getColor(this, R.color.green_light));
        view.setText("onResume");
        toast.setView(view);
        toast.setDuration(3000);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                LogUtils.d();
                toast.show();
            }
        }, 4000);
    }

    @Override
    public void onDismiss(){
        final ToastBread toast = mToaster.newToast();
        toast.makeText(this, "Dismiss!", 3000);
        toast.show();
    }
}
