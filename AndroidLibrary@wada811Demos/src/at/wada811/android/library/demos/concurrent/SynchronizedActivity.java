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

import java.util.concurrent.TimeUnit;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import at.wada811.android.library.demos.R;
import at.wada811.utils.LogUtils;
import at.wada811.utils.PreferenceUtils;

public class SynchronizedActivity extends FragmentActivity {

    final SynchronizedActivity self   = this;
    private static int         mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronized);

        Button syncClassMethodExecuteButton = (Button)findViewById(R.id.syncClassMethod);
        syncClassMethodExecuteButton.setText("SyncClassMethodExecute");
        syncClassMethodExecuteButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                LogUtils.d();
                syncClassMethodExecute();
                LogUtils.d();
            }
        });
        Button asyncClassMethodExecuteButton = (Button)findViewById(R.id.asyncClassMethod);
        asyncClassMethodExecuteButton.setText("AsyncClassMethodExecute");
        asyncClassMethodExecuteButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                LogUtils.d();
                asyncClassMethodExecute();
                LogUtils.d();
            }
        });
        Button syncInstanceMethodExecuteButton = (Button)findViewById(R.id.syncInstanceMethod);
        syncInstanceMethodExecuteButton.setText("SyncInstanceMethodExecute");
        syncInstanceMethodExecuteButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                LogUtils.d();
                syncInstanceMethodExecute();
                LogUtils.d();
            }
        });
        Button asyncInstanceMethodExecuteButton = (Button)findViewById(R.id.asyncInstanceMethod);
        asyncInstanceMethodExecuteButton.setText("AsyncInstanceMethodExecute");
        asyncInstanceMethodExecuteButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                LogUtils.d();
                asyncInstanceMethodExecute();
                LogUtils.d();
            }
        });
    }

    public void syncClassMethodExecute(){
        LogUtils.d();
        PreferenceUtils.putString(this, ClassMethodExecutor.SyncExecutor.KEY, null);
        new Thread(new SyncClassMethodExecutorRunnable("A")).start();
        new Thread(new SyncClassMethodExecutorRunnable("B")).start();
        new Thread(new SyncClassMethodExecutorRunnable("C")).start();
    }

    public class SyncClassMethodExecutorRunnable implements Runnable {
        String threadName;

        public SyncClassMethodExecutorRunnable(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run(){
            ClassMethodExecutor.SyncExecutor.execute(self, threadName);
        }
    }

    public void asyncClassMethodExecute(){
        LogUtils.d();
        PreferenceUtils.putString(this, ClassMethodExecutor.Async.KEY, null);
        new Thread(new AsyncClassMethodExecutorRunnable("A")).start();
        new Thread(new AsyncClassMethodExecutorRunnable("B")).start();
        new Thread(new AsyncClassMethodExecutorRunnable("C")).start();
    }

    public class AsyncClassMethodExecutorRunnable implements Runnable {
        String threadName;

        public AsyncClassMethodExecutorRunnable(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run(){
            ClassMethodExecutor.Async.execute(self, threadName);
        }
    }

    public void syncInstanceMethodExecute(){
        LogUtils.d();
        mCount = 0;
        SyncInstanceMethodExecutor executor = new SyncInstanceMethodExecutor();
        new Thread(new SyncInstanceMethodExecutorRunnable(executor, "A")).start();
        new Thread(new SyncInstanceMethodExecutorRunnable(executor, "B")).start();
        new Thread(new SyncInstanceMethodExecutorRunnable(executor, "C")).start();
    }

    public class SyncInstanceMethodExecutorRunnable implements Runnable {
        private SyncInstanceMethodExecutor mExecutor;
        private String                     mThreadNmae;

        public SyncInstanceMethodExecutorRunnable(SyncInstanceMethodExecutor executor, String threadNmae) {
            mExecutor = executor;
            mThreadNmae = threadNmae;
        }

        @Override
        public void run(){
            mExecutor.execute(mThreadNmae);
        }

    }

    public class SyncInstanceMethodExecutor {
        private final Object    lock = new Object();
        public static final int N    = 100;

        public void execute(String threadName){
            synchronized(lock){
                if(mCount == N){
                    LogUtils.d(threadName + ": do nothing");
                }else{
                    for(int i = 0; i < N; i++){
                        LogUtils.d(threadName + ": " + ++mCount);
                    }
                    try{
                        TimeUnit.SECONDS.sleep(1);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void asyncInstanceMethodExecute(){
        LogUtils.d();
        mCount = 0;
        AsyncInstanceMethodExecutor executor = new AsyncInstanceMethodExecutor();
        new Thread(new AsyncInstanceMethodExecutorRunnable(executor, "A")).start();
        new Thread(new AsyncInstanceMethodExecutorRunnable(executor, "B")).start();
        new Thread(new AsyncInstanceMethodExecutorRunnable(executor, "C")).start();
    }

    public class AsyncInstanceMethodExecutorRunnable implements Runnable {
        private AsyncInstanceMethodExecutor mExecutor;
        private String                      mThreadNmae;

        public AsyncInstanceMethodExecutorRunnable(AsyncInstanceMethodExecutor executor, String threadNmae) {
            mExecutor = executor;
            mThreadNmae = threadNmae;
        }

        @Override
        public void run(){
            mExecutor.execute(mThreadNmae);
        }

    }

    public class AsyncInstanceMethodExecutor {
        public static final int N = 100;

        public final void execute(String threadId){
            if(mCount == N){
                LogUtils.d(threadId + ": do nothing");
            }else{
                for(int i = 0; i < N; i++){
                    LogUtils.d(threadId + ": " + ++mCount);
                }
                try{
                    TimeUnit.SECONDS.sleep(1);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
