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
package at.wada811.android.library.demos.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import at.wada811.android.library.demos.R;
import at.wada811.utils.LogUtils;
import at.wada811.view.Tooltip;

public class ExecutorActivity extends FragmentActivity {

    final ExecutorActivity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executor);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                execute();
                LogUtils.d();
            }
        });

    }

    public void execute(){
        LogUtils.d();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable1());
        executorService.submit(new Runnable2());
        executorService.submit(new Runnable1());
        executorService.submit(new Runnable2());
        LogUtils.d("Terminated: " + executorService.isTerminated());
    }

    public class Runnable1 implements Runnable {
        @Override
        public void run(){
            LogUtils.d("Runnable1: " + System.currentTimeMillis());
            runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    LogUtils.i("Runnable1: " + System.currentTimeMillis());
                    TextView mTextView = new TextView(self);
                    mTextView.setBackgroundColor(Color.GREEN);
                    mTextView.setText("Runnable1: " + System.currentTimeMillis());
                    Tooltip mTooltip = new Tooltip(self);
                    mTooltip.setContentView(mTextView);
                    mTooltip.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                    mTooltip.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                    mTooltip.setAnimationStyle(R.style.tooltipAnimation);
                    mTooltip.setDuration(3000);
                    mTooltip.showAtLocation(Gravity.CENTER, 0, 0);
                    LogUtils.i("Runnable1: " + System.currentTimeMillis());
                }
            });
            try{
                Thread.sleep(3000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            LogUtils.d("Runnable1: " + System.currentTimeMillis());
        }
    }

    public class Runnable2 implements Runnable {
        @Override
        public void run(){
            LogUtils.d("Runnable2: " + System.currentTimeMillis());
            runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    LogUtils.i("Runnable2: " + System.currentTimeMillis());
                    TextView mTextView = new TextView(self);
                    mTextView.setBackgroundColor(Color.YELLOW);
                    mTextView.setText("Runnable2: " + System.currentTimeMillis());
                    Tooltip mTooltip = new Tooltip(self);
                    mTooltip.setContentView(mTextView);
                    mTooltip.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                    mTooltip.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                    mTooltip.setAnimationStyle(R.style.tooltipAnimation);
                    mTooltip.setDuration(3000);
                    mTooltip.showAtLocation(Gravity.CENTER, 0, 30);
                    LogUtils.i("Runnable2: " + System.currentTimeMillis());
                }
            });
            try{
                Thread.sleep(3000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            LogUtils.d("Runnable2: " + System.currentTimeMillis());
        }
    }
}
