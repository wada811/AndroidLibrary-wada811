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

        Button syncMethodExecuteButton = (Button)findViewById(R.id.syncMethod);
        syncMethodExecuteButton.setText("SyncMethodExecute");
        syncMethodExecuteButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                LogUtils.d();
                syncMethodExecute();
                LogUtils.d();
            }
        });
        Button asyncMethodExecuteButton = (Button)findViewById(R.id.asyncMethod);
        asyncMethodExecuteButton.setText("AsyncMethodExecute");
        asyncMethodExecuteButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                LogUtils.d();
                asyncMethodExecute();
                LogUtils.d();
            }
        });
        Button syncVariableExecuteButton = (Button)findViewById(R.id.syncVariable);
        syncVariableExecuteButton.setText("SyncVariableExecute");
        syncVariableExecuteButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                LogUtils.d();
                syncVariableExecute();
                LogUtils.d();
            }
        });
        Button asyncVariableExecuteButton = (Button)findViewById(R.id.asyncVariable);
        asyncVariableExecuteButton.setText("AsyncVariableExecute");
        asyncVariableExecuteButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                LogUtils.d();
                asyncVariableExecute();
                LogUtils.d();
            }
        });
    }

    public void syncMethodExecute(){
        LogUtils.d();
        PreferenceUtils.putString(this, SynchronizedMethodExecutor.SyncExecutor.KEY, null);
        new Thread(new SyncExecutorRunnable("A")).start();
        new Thread(new SyncExecutorRunnable("B")).start();
        new Thread(new SyncExecutorRunnable("C")).start();
    }

    public class SyncExecutorRunnable implements Runnable {
        String threadName;

        public SyncExecutorRunnable(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run(){
            SynchronizedMethodExecutor.SyncExecutor.execute(self, threadName);
        }
    }

    public void asyncMethodExecute(){
        LogUtils.d();
        PreferenceUtils.putString(this, SynchronizedMethodExecutor.Async.KEY, null);
        new Thread(new AsyncExecutorRunnable("A")).start();
        new Thread(new AsyncExecutorRunnable("B")).start();
        new Thread(new AsyncExecutorRunnable("C")).start();
    }

    public class AsyncExecutorRunnable implements Runnable {
        String threadName;

        public AsyncExecutorRunnable(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run(){
            SynchronizedMethodExecutor.Async.execute(self, threadName);
        }
    }

    public void syncVariableExecute(){
        LogUtils.d();
        mCount = 0;
        SyncVariableExecutor executor = new SyncVariableExecutor();
        new Thread(new SyncVariableExecutorRunnable(executor, "A")).start();
        new Thread(new SyncVariableExecutorRunnable(executor, "B")).start();
        new Thread(new SyncVariableExecutorRunnable(executor, "C")).start();
    }

    public class SyncVariableExecutorRunnable implements Runnable {
        private SyncVariableExecutor mExecutor;
        private String               mThreadNmae;

        public SyncVariableExecutorRunnable(SyncVariableExecutor executor, String threadNmae) {
            mExecutor = executor;
            mThreadNmae = threadNmae;
        }

        @Override
        public void run(){
            mExecutor.execute(mThreadNmae);
        }

    }

    public class SyncVariableExecutor {
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

    public void asyncVariableExecute(){
        LogUtils.d();
        mCount = 0;
        AsyncVariableExecutor executor = new AsyncVariableExecutor();
        new Thread(new AsyncVariableExecutorRunnable(executor, "A")).start();
        new Thread(new AsyncVariableExecutorRunnable(executor, "B")).start();
        new Thread(new AsyncVariableExecutorRunnable(executor, "C")).start();
    }

    public class AsyncVariableExecutorRunnable implements Runnable {
        private AsyncVariableExecutor mExecutor;
        private String                mThreadNmae;

        public AsyncVariableExecutorRunnable(AsyncVariableExecutor executor, String threadNmae) {
            mExecutor = executor;
            mThreadNmae = threadNmae;
        }

        @Override
        public void run(){
            mExecutor.execute(mThreadNmae);
        }

    }

    public class AsyncVariableExecutor {
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
