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
package at.wada811.android.library.demos.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import at.wada811.android.library.demos.R;
import at.wada811.utils.AndroidUtils;
import at.wada811.utils.LogUtils;
import at.wada811.view.FlickTouchListener;
import at.wada811.view.FlickTouchListener.OnFlickListener;

public class FlickActivity extends FragmentActivity implements OnFlickListener {

    final FlickActivity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flick);

        FrameLayout container = (FrameLayout)findViewById(R.id.container);
        container.setOnTouchListener(new FlickTouchListener(this));
        container.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                LogUtils.d();
                AndroidUtils.showToast(self, "onClick");
            }
        });
    }

    @Override
    public void onFlickUp(){
        LogUtils.d();
        AndroidUtils.showToast(this, "onFlickUp");
    }

    @Override
    public void onFlickDown(){
        LogUtils.d();
        AndroidUtils.showToast(this, "onFlickDown");
    }

    @Override
    public void onFlickRight(){
        LogUtils.d();
        AndroidUtils.showToast(this, "onFlickRight");
    }

    @Override
    public void onFlickLeft(){
        LogUtils.d();
        AndroidUtils.showToast(this, "onFlickLeft");
    }
}
