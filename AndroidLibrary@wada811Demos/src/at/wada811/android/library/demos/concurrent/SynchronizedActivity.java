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
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import at.wada811.android.library.demos.R;
import at.wada811.utils.LogUtils;
import at.wada811.utils.PreferenceUtils;

public class SynchronizedActivity extends FragmentActivity implements OnClickListener {

    final SynchronizedActivity self = this;
    public static final int    N    = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronized);

        Button syncClassMethodExecuteButton = (Button)findViewById(R.id.button1);
        syncClassMethodExecuteButton.setText("SyncClassMethodExecute");
        syncClassMethodExecuteButton.setOnClickListener(this);
        Button asyncClassMethodExecuteButton = (Button)findViewById(R.id.button2);
        asyncClassMethodExecuteButton.setText("AsyncClassMethodExecute");
        asyncClassMethodExecuteButton.setOnClickListener(this);
        Button syncInstanceMethodExecuteButton = (Button)findViewById(R.id.button3);
        syncInstanceMethodExecuteButton.setText("SyncThisInstanceMethodExecute");
        syncInstanceMethodExecuteButton.setOnClickListener(this);
        Button asyncInstanceMethodExecuteButton = (Button)findViewById(R.id.button4);
        asyncInstanceMethodExecuteButton.setText("AsyncThisInstanceMethodExecute");
        asyncInstanceMethodExecuteButton.setOnClickListener(this);
        Button syncLockInstanceMethodExecuteButton = (Button)findViewById(R.id.button5);
        syncLockInstanceMethodExecuteButton.setText("SyncLockInstanceMethodExecute");
        syncLockInstanceMethodExecuteButton.setOnClickListener(this);
        Button asyncLockInstanceMethodExecuteButton = (Button)findViewById(R.id.button6);
        asyncLockInstanceMethodExecuteButton.setText("AsyncLockInstanceMethodExecute");
        asyncLockInstanceMethodExecuteButton.setOnClickListener(this);
        Button syncMultiClassMethodExecuteButton = (Button)findViewById(R.id.button7);
        syncMultiClassMethodExecuteButton.setText("SyncMultiClassMethodExecute");
        syncMultiClassMethodExecuteButton.setOnClickListener(this);
        Button asyncMultiClassMethodExecuteButton = (Button)findViewById(R.id.button8);
        asyncMultiClassMethodExecuteButton.setText("AsyncMultiClassMethodExecute");
        asyncMultiClassMethodExecuteButton.setOnClickListener(this);
        Button syncMultiInstanceMethodExecuteButton = (Button)findViewById(R.id.button9);
        syncMultiInstanceMethodExecuteButton.setText("SyncMultiInstanceMethodExecute");
        syncMultiInstanceMethodExecuteButton.setOnClickListener(this);
        Button asyncMultiInstanceMethodExecuteButton = (Button)findViewById(R.id.button10);
        asyncMultiInstanceMethodExecuteButton.setText("AsyncMultiInstanceMethodExecute");
        asyncMultiInstanceMethodExecuteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        LogUtils.d();
        switch(v.getId()){
            case R.id.button1:
                syncClassMethodExecute();
                break;
            case R.id.button2:
                asyncClassMethodExecute();
                break;
            case R.id.button3:
                syncThisInstanceMethodExecute();
                break;
            case R.id.button4:
                asyncThisInstanceMethodExecute();
                break;
            case R.id.button5:
                syncLockInstanceMethodExecute();
                break;
            case R.id.button6:
                asyncLockInstanceMethodExecute();
                break;
            case R.id.button7:
                syncMultiClassMethodExecute();
                break;
            case R.id.button8:
                asyncMultiClassMethodExecute();
                break;
            case R.id.button9:
                syncMultiInstanceMethodExecute();
                break;
            case R.id.button10:
                asyncMultiInstanceMethodExecute();
                break;
            default:
                break;
        }
        LogUtils.d();
    }

    /**
     * クラスメソッドを synchronized にした場合のテスト実行メソッド
     */
    public void syncClassMethodExecute(){
        LogUtils.d();
        PreferenceUtils.putString(this, SyncClassMethodExecuter.KEY, null);
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
            SyncClassMethodExecuter.execute(self, threadName);
        }
    }

    /**
     * クラスメソッドを synchronized にした場合のテストクラス
     */
    public static class SyncClassMethodExecuter {

        public static final String KEY = SyncClassMethodExecuter.class.getSimpleName();

        public static final synchronized void execute(Context context, String threadName){
            String lastThreadName = PreferenceUtils.getString(context, KEY, null);
            if(lastThreadName != null){
                LogUtils.d(threadName + ": do nothing. " + lastThreadName + " is executed.");
            }else{
                try{
                    TimeUnit.SECONDS.sleep(1);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                LogUtils.d(threadName + ": execute");
                PreferenceUtils.putString(context, KEY, threadName);
                try{
                    TimeUnit.SECONDS.sleep(1);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * クラスメソッドを synchronized にした場合の対照テスト実行メソッド
     */
    public void asyncClassMethodExecute(){
        LogUtils.d();
        PreferenceUtils.putString(this, AsyncClassMethodExecuter.KEY, null);
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
            AsyncClassMethodExecuter.execute(self, threadName);
        }
    }

    /**
     * クラスメソッドを synchronized にした場合の対照テストクラス
     */
    public static class AsyncClassMethodExecuter {

        public static final String KEY = AsyncClassMethodExecuter.class.getSimpleName();

        public static final void execute(Context context, String threadName){
            String lastThreadName = PreferenceUtils.getString(context, KEY, null);
            if(lastThreadName != null){
                LogUtils.d(threadName + ": do nothing. " + lastThreadName + " is executed.");
            }else{
                try{
                    TimeUnit.SECONDS.sleep(1);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                LogUtils.d(threadName + ": execute");
                PreferenceUtils.putString(context, KEY, threadName);
                try{
                    TimeUnit.SECONDS.sleep(1);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * インスタンスメソッドを synchronized にした場合のテスト実行メソッド
     */
    public void syncThisInstanceMethodExecute(){
        LogUtils.d();
        SyncThisInstanceMethodExecutor executor = new SyncThisInstanceMethodExecutor();
        new Thread(new SyncThisInstanceMethodExecutorRunnable(executor, "A")).start();
        new Thread(new SyncThisInstanceMethodExecutorRunnable(executor, "B")).start();
        new Thread(new SyncThisInstanceMethodExecutorRunnable(executor, "C")).start();
    }

    public class SyncThisInstanceMethodExecutorRunnable implements Runnable {
        private SyncThisInstanceMethodExecutor mExecutor;
        private String                         mThreadNmae;

        public SyncThisInstanceMethodExecutorRunnable(SyncThisInstanceMethodExecutor executor, String threadNmae) {
            mExecutor = executor;
            mThreadNmae = threadNmae;
        }

        @Override
        public void run(){
            mExecutor.execute(mThreadNmae);
        }

    }

    /**
     * インスタンスメソッドを synchronized にした場合のテストクラス
     */
    public class SyncThisInstanceMethodExecutor {
        private int mCount = 0;

        public synchronized void execute(String threadName){
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

    /**
     * インスタンスメソッドを synchronized にした場合の対照テスト実行メソッド
     */
    public void asyncThisInstanceMethodExecute(){
        LogUtils.d();
        AsyncThisInstanceMethodExecutor executor = new AsyncThisInstanceMethodExecutor();
        new Thread(new AsyncThisInstanceMethodExecutorRunnable(executor, "A")).start();
        new Thread(new AsyncThisInstanceMethodExecutorRunnable(executor, "B")).start();
        new Thread(new AsyncThisInstanceMethodExecutorRunnable(executor, "C")).start();
    }

    public class AsyncThisInstanceMethodExecutorRunnable implements Runnable {
        private AsyncThisInstanceMethodExecutor mExecutor;
        private String                          mThreadNmae;

        public AsyncThisInstanceMethodExecutorRunnable(AsyncThisInstanceMethodExecutor executor, String threadNmae) {
            mExecutor = executor;
            mThreadNmae = threadNmae;
        }

        @Override
        public void run(){
            mExecutor.execute(mThreadNmae);
        }

    }

    /**
     * インスタンスメソッドを synchronized にした場合の対照テストクラス
     */
    public class AsyncThisInstanceMethodExecutor {
        private int mCount = 0;

        public final void execute(String threadName){
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

    /**
     * インスタンスメソッドでロックオブジェクトを synchronized した場合のテスト実行メソッド
     */
    public void syncLockInstanceMethodExecute(){
        LogUtils.d();
        SyncLockInstanceMethodExecutor executor = new SyncLockInstanceMethodExecutor();
        new Thread(new SyncLockInstanceMethodExecutorRunnable(executor, "A")).start();
        new Thread(new SyncLockInstanceMethodExecutorRunnable(executor, "B")).start();
        new Thread(new SyncLockInstanceMethodExecutorRunnable(executor, "C")).start();
    }

    public class SyncLockInstanceMethodExecutorRunnable implements Runnable {
        private SyncLockInstanceMethodExecutor mExecutor;
        private String                         mThreadNmae;

        public SyncLockInstanceMethodExecutorRunnable(SyncLockInstanceMethodExecutor executor, String threadNmae) {
            mExecutor = executor;
            mThreadNmae = threadNmae;
        }

        @Override
        public void run(){
            mExecutor.execute(mThreadNmae);
        }

    }

    /**
     * インスタンスメソッドでロックオブジェクトを synchronized した場合のテストクラス
     */
    public class SyncLockInstanceMethodExecutor {
        private final Object lock   = new Object();
        private int          mCount = 0;

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

    /**
     * インスタンスメソッドでロックオブジェクトを synchronized した場合の対照テスト実行メソッド
     */
    public void asyncLockInstanceMethodExecute(){
        LogUtils.d();
        AsyncLockInstanceMethodExecutor executor = new AsyncLockInstanceMethodExecutor();
        new Thread(new AsyncLockInstanceMethodExecutorRunnable(executor, "A")).start();
        new Thread(new AsyncLockInstanceMethodExecutorRunnable(executor, "B")).start();
        new Thread(new AsyncLockInstanceMethodExecutorRunnable(executor, "C")).start();
    }

    public class AsyncLockInstanceMethodExecutorRunnable implements Runnable {
        private AsyncLockInstanceMethodExecutor mExecutor;
        private String                          mThreadNmae;

        public AsyncLockInstanceMethodExecutorRunnable(AsyncLockInstanceMethodExecutor executor, String threadNmae) {
            mExecutor = executor;
            mThreadNmae = threadNmae;
        }

        @Override
        public void run(){
            mExecutor.execute(mThreadNmae);
        }

    }

    /**
     * インスタンスメソッドでロックオブジェクトを synchronized した場合の対照テストクラス
     */
    public class AsyncLockInstanceMethodExecutor {
        private int mCount = 0;

        public final void execute(String threadName){
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

    /**
     * synchronized な複数のインスタンスメソッドを各スレッドで呼び出した場合のテスト実行メソッド
     */
    public void syncMultiClassMethodExecute(){
        LogUtils.d();
        SyncMultiClassMethodExecuter.count = 0;
        new Thread(new SyncMultiClassMethodExecutorRunnable("A")).start();
        new Thread(new SyncMultiClassMethodExecutorRunnable("B")).start();
        new Thread(new SyncMultiClassMethodExecutorRunnable("C")).start();
    }

    public class SyncMultiClassMethodExecutorRunnable implements Runnable {
        String threadName;

        public SyncMultiClassMethodExecutorRunnable(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run(){
            SyncMultiClassMethodExecuter.execute1(threadName);
            SyncMultiClassMethodExecuter.execute2(threadName);
            SyncMultiClassMethodExecuter.execute3(threadName);
        }
    }

    /**
     * synchronized な複数のインスタンスメソッドを各スレッドで呼び出した場合のテストクラス
     */
    public static class SyncMultiClassMethodExecuter {

        public static final String KEY   = SyncMultiClassMethodExecuter.class.getSimpleName();
        private static int         count = 0;

        public static final synchronized void execute1(String threadName){
            for(int i = 0; i < N; i++){
                LogUtils.d(threadName + "[1]: " + ++count);
            }
        }

        public static final synchronized void execute2(String threadName){
            for(int i = 0; i < N; i++){
                LogUtils.d(threadName + "[2]: " + ++count);
            }
        }

        public static final synchronized void execute3(String threadName){
            for(int i = 0; i < N; i++){
                LogUtils.d(threadName + "[3]: " + ++count);
            }
        }
    }

    /**
     * synchronized な複数のインスタンスメソッドを各スレッドで呼び出した場合の対照テスト実行メソッド
     */
    public void asyncMultiClassMethodExecute(){
        LogUtils.d();
        AsyncMultiClassMethodExecuter.count = 0;
        new Thread(new AsyncMultiClassMethodExecutorRunnable("A")).start();
        new Thread(new AsyncMultiClassMethodExecutorRunnable("B")).start();
        new Thread(new AsyncMultiClassMethodExecutorRunnable("C")).start();
    }

    public class AsyncMultiClassMethodExecutorRunnable implements Runnable {
        String threadName;

        public AsyncMultiClassMethodExecutorRunnable(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run(){
            AsyncMultiClassMethodExecuter.count = 0;
            AsyncMultiClassMethodExecuter.execute1(threadName);
            AsyncMultiClassMethodExecuter.execute2(threadName);
            AsyncMultiClassMethodExecuter.execute3(threadName);
        }
    }

    public static class AsyncMultiClassMethodExecuter {

        public static final String KEY   = AsyncMultiClassMethodExecuter.class.getSimpleName();
        private static int         count = 0;

        public static final void execute1(String threadName){
            for(int i = 0; i < N; i++){
                LogUtils.d(threadName + "[1]: " + ++count);
            }
        }

        public static final void execute2(String threadName){
            for(int i = 0; i < N; i++){
                LogUtils.d(threadName + "[2]: " + ++count);
            }
        }

        public static final void execute3(String threadName){
            for(int i = 0; i < N; i++){
                LogUtils.d(threadName + "[3]: " + ++count);
            }
        }
    }

    public void syncMultiInstanceMethodExecute(){
        LogUtils.d();
        SyncMultiInstanceMethodExecutor executor = new SyncMultiInstanceMethodExecutor();
        new Thread(new SyncMultiInstanceMethodExecutorRunnable(executor, "A")).start();
        new Thread(new SyncMultiInstanceMethodExecutorRunnable(executor, "B")).start();
        new Thread(new SyncMultiInstanceMethodExecutorRunnable(executor, "C")).start();
    }

    public class SyncMultiInstanceMethodExecutorRunnable implements Runnable {
        private SyncMultiInstanceMethodExecutor mExecutor;
        private String                          mThreadNmae;

        public SyncMultiInstanceMethodExecutorRunnable(SyncMultiInstanceMethodExecutor executor, String threadNmae) {
            mExecutor = executor;
            mThreadNmae = threadNmae;
        }

        @Override
        public void run(){
            mExecutor.execute1(mThreadNmae);
            mExecutor.execute2(mThreadNmae);
            mExecutor.execute3(mThreadNmae);
        }

    }

    public class SyncMultiInstanceMethodExecutor {
        private final Object lock   = new Object();
        private int          mCount = 0;

        public void execute1(String threadName){
            synchronized(lock){
                for(int i = 0; i < N; i++){
                    LogUtils.d(threadName + "[1]: " + ++mCount);
                }
            }
        }

        public void execute2(String threadName){
            synchronized(lock){
                for(int i = 0; i < N; i++){
                    LogUtils.d(threadName + "[2]: " + ++mCount);
                }
            }
        }

        public void execute3(String threadName){
            synchronized(lock){
                for(int i = 0; i < N; i++){
                    LogUtils.d(threadName + "[3]: " + ++mCount);
                }
            }
        }
    }

    public void asyncMultiInstanceMethodExecute(){
        LogUtils.d();
        AsyncMultiInstanceMethodExecutor executor = new AsyncMultiInstanceMethodExecutor();
        new Thread(new AsyncMultiInstanceMethodExecutorRunnable(executor, "A")).start();
        new Thread(new AsyncMultiInstanceMethodExecutorRunnable(executor, "B")).start();
        new Thread(new AsyncMultiInstanceMethodExecutorRunnable(executor, "C")).start();
    }

    public class AsyncMultiInstanceMethodExecutorRunnable implements Runnable {
        private AsyncMultiInstanceMethodExecutor mExecutor;
        private String                           mThreadNmae;

        public AsyncMultiInstanceMethodExecutorRunnable(AsyncMultiInstanceMethodExecutor executor, String threadNmae) {
            mExecutor = executor;
            mThreadNmae = threadNmae;
        }

        @Override
        public void run(){
            mExecutor.execute1(mThreadNmae);
            mExecutor.execute2(mThreadNmae);
            mExecutor.execute3(mThreadNmae);
        }

    }

    /**
     * synchronized な複数のインスタンスメソッドを各スレッドで呼び出した場合の対照テスト実行メソッド
     */
    public class AsyncMultiInstanceMethodExecutor {
        private int mCount = 0;

        public void execute1(String threadName){
            for(int i = 0; i < N; i++){
                LogUtils.d(threadName + "[1]: " + ++mCount);
            }
        }

        public void execute2(String threadName){
            for(int i = 0; i < N; i++){
                LogUtils.d(threadName + "[2]: " + ++mCount);
            }
        }

        public void execute3(String threadName){
            for(int i = 0; i < N; i++){
                LogUtils.d(threadName + "[3]: " + ++mCount);
            }
        }
    }
}
