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

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import at.wada811.android.library.demos.R;
import at.wada811.utils.LogUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ExecutorServiceでマルチスレッドで実行可能なタスクキューを使う | DevAchieve
 * http://wada811.blogspot.com/2013/11/executor-service-that-multi-thread-task-queue-in-java.html
 */
public class ExecutorActivity extends FragmentActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executor);

        ((Button)findViewById(R.id.button1)).setText("newSingleThreadExecutor");
        ((Button)findViewById(R.id.button1)).setOnClickListener(this);
        ((Button)findViewById(R.id.button2)).setText("newFixedThreadPool");
        ((Button)findViewById(R.id.button2)).setOnClickListener(this);
        ((Button)findViewById(R.id.button3)).setText("newCachedThreadPool");
        ((Button)findViewById(R.id.button3)).setOnClickListener(this);
        ((Button)findViewById(R.id.button4)).setText("newScheduledThreadPool");
        ((Button)findViewById(R.id.button4)).setOnClickListener(this);
        ((Button)findViewById(R.id.button5)).setText("newSingleThreadScheduledExecutor(1)");
        ((Button)findViewById(R.id.button5)).setOnClickListener(this);
        ((Button)findViewById(R.id.button6)).setText("newSingleThreadScheduledExecutor(2)");
        ((Button)findViewById(R.id.button6)).setOnClickListener(this);
        ((Button)findViewById(R.id.button7)).setText("AtFixedRate(1)");
        ((Button)findViewById(R.id.button7)).setOnClickListener(this);
        ((Button)findViewById(R.id.button8)).setText("AtFixedRate(2)");
        ((Button)findViewById(R.id.button8)).setOnClickListener(this);
        ((Button)findViewById(R.id.button9)).setText("WithFixedDelay(1)");
        ((Button)findViewById(R.id.button9)).setOnClickListener(this);
        ((Button)findViewById(R.id.button10)).setText("WithFixedDelay(2)");
        ((Button)findViewById(R.id.button10)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        LogUtils.d();
        switch(v.getId()){
            case R.id.button1:
                newSingleThreadExecutorTest();
                break;
            case R.id.button2:
                newFixedThreadPoolTest();
                break;
            case R.id.button3:
                newCachedThreadPoolTest();
                break;
            case R.id.button4:
                newScheduledThreadPoolTest();
                break;
            case R.id.button5:
                newSingleThreadScheduledExecutorTest();
                break;
            case R.id.button6:
                newSingleThreadScheduledExecutorDuringExecutionTest();
                break;
            case R.id.button7:
                newSingleThreadScheduledExecutorAtFixedRateTest();
                break;
            case R.id.button8:
                newSingleThreadScheduledExecutorAtFixedRateDuringExecutionTest();
                break;
            case R.id.button9:
                newSingleThreadScheduledExecutorWithFixedDelayTest();
                break;
            case R.id.button10:
                newSingleThreadScheduledExecutorWithFixedDelayDuringExecutionTest();
                break;
            default:
                break;
        }
        LogUtils.d();
    }

    /**
     * {@link Executors#newSingleThreadExecutor} のテストメソッド
     * 
     * <p>
     * SingleThread なのでタスクをいくつ追加しても一つのスレッドで実行します。
     * </p>
     */
    public void newSingleThreadExecutorTest(){
        LogUtils.d();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new ExecutorRunnable("A", 1));
        executorService.submit(new ExecutorRunnable("B", 1));
        executorService.submit(new ExecutorRunnable("C", 1));
        executorService.submit(new ExecutorRunnable("D", 1));
    }

    /**
     * {@link Executors#newFixedThreadPool(int)} のテストメソッド
     * 
     * <p>
     * 引数のスレッド数で実行します。
     * </p>
     */
    public void newFixedThreadPoolTest(){
        LogUtils.d();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(new ExecutorRunnable("A", 1));
        executorService.submit(new ExecutorRunnable("B", 1));
        executorService.submit(new ExecutorRunnable("C", 1));
        executorService.submit(new ExecutorRunnable("D", 1));
    }

    /**
     * {@link Executors#newCachedThreadPool} のテストメソッド
     * 
     * <p>
     * 必要な分だけスレッドを生成して実行します。
     * </p>
     */
    public void newCachedThreadPoolTest(){
        LogUtils.d();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new ExecutorRunnable("A", 1));
        executorService.submit(new ExecutorRunnable("B", 1));
        executorService.submit(new ExecutorRunnable("C", 1));
        executorService.submit(new ExecutorRunnable("D", 1));
    }

    /**
     * {@link Executors#newScheduledThreadPool(int)} のテストメソッド
     * 
     * <p>
     * 引数のスレッド数で実行します。実行タイミングを設定できます。設定のスレッド数に達したら待たされます。
     * </p>
     */
    public void newScheduledThreadPoolTest(){
        LogUtils.d();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        executorService.schedule(new ExecutorRunnable("A", 1), 1, TimeUnit.SECONDS);
        executorService.schedule(new ExecutorRunnable("B", 1), 1, TimeUnit.SECONDS);
        executorService.schedule(new ExecutorRunnable("C", 1), 1, TimeUnit.SECONDS);
        executorService.schedule(new ExecutorRunnable("D", 1), 1, TimeUnit.SECONDS);
    }

    /**
     * {@link ScheduledExecutorService#schedule(Runnable, long, TimeUnit)} の実行タイミングが被らないパターンのテストメソッド
     * 
     * <p>
     * 一つのスレッドで実行します。実行タイミングを設定できます。
     * </p>
     */
    public void newSingleThreadScheduledExecutorTest(){
        LogUtils.d();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(new ExecutorRunnable("A", 1), 8, TimeUnit.SECONDS);
        executorService.schedule(new ExecutorRunnable("B", 1), 4, TimeUnit.SECONDS);
        executorService.schedule(new ExecutorRunnable("C", 1), 0, TimeUnit.SECONDS);
        executorService.schedule(new ExecutorRunnable("D", 1), 12, TimeUnit.SECONDS);
    }

    /**
     * {@link ScheduledExecutorService#schedule(Runnable, long, TimeUnit)} の実行タイミングが被るパターンのテストメソッド
     * 
     * <p>
     * 一つのスレッドで実行します。実行タイミングを設定できます。
     * </p>
     */
    public void newSingleThreadScheduledExecutorDuringExecutionTest(){
        LogUtils.d();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(new ExecutorRunnable("A", 1), 1, TimeUnit.SECONDS);
        executorService.schedule(new ExecutorRunnable("B", 1), 1, TimeUnit.SECONDS);
        executorService.schedule(new ExecutorRunnable("C", 1), 1, TimeUnit.SECONDS);
        executorService.schedule(new ExecutorRunnable("D", 1), 1, TimeUnit.SECONDS);
    }

    /**
     * {@link ScheduledExecutorService#scheduleAtFixedRate(Runnable, long, long, TimeUnit)}
     * の実行タイミングをを過ぎないパターンのテストメソッド
     * 
     * <p>
     * 一つのスレッドで実行します。実行タイミングを設定できます。繰り返し実行でき、実行間隔を設定できます。
     * </p>
     */
    public void newSingleThreadScheduledExecutorAtFixedRateTest(){
        LogUtils.d();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new ExecutorRunnable("A", 1), 1, 2, TimeUnit.SECONDS);
        shutdown(executorService);
    }

    /**
     * {@link ScheduledExecutorService#scheduleAtFixedRate(Runnable, long, long, TimeUnit)}
     * の実行タイミングを過ぎるパターンのテストメソッド
     * 
     * <p>
     * 一つのスレッドで実行します。実行タイミングを設定できます。繰り返し実行でき、実行間隔を設定できます。 <br>
     * 待たされ、次回実行タイミングが過ぎていた場合はすぐに実行します。
     * </p>
     */
    public void newSingleThreadScheduledExecutorAtFixedRateDuringExecutionTest(){
        LogUtils.d();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new ExecutorRunnable("A", 3), 1, 2, TimeUnit.SECONDS);
        shutdown(executorService);
    }

    /**
     * {@link ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)}
     * の実行タイミングが被らないパターンのテストメソッド
     * 
     * <p>
     * 一つのスレッドで実行します。実行タイミングを設定できます。繰り返し実行できます。実行間隔を設定できます。
     * </p>
     */
    public void newSingleThreadScheduledExecutorWithFixedDelayTest(){
        LogUtils.d();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(new ExecutorRunnable("A", 1), 1, 2, TimeUnit.SECONDS);
        shutdown(executorService);
    }

    /**
     * {@link ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)}
     * の実行タイミングが被るパターンのテストメソッド
     * 
     * <p>
     * 一つのスレッドで実行します。実行タイミングを設定できます。繰り返し実行できます。実行間隔を設定できます。 <br>
     * 待たされ、次回実行タイミングが過ぎていた場合でも実行間隔の分だけ待ってから実行します。
     * </p>
     */
    public void newSingleThreadScheduledExecutorWithFixedDelayDuringExecutionTest(){
        LogUtils.d();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(new ExecutorRunnable("A", 3), 1, 2, TimeUnit.SECONDS);
        shutdown(executorService);
    }

    /**
     * ExecutorService をシャットダウンします。
     * 
     * <p>
     * このサンプルでは繰り返し実行するタイプのテストメソッドを停止するために使っています。停止命令を出してから1秒間だけ待って強制終了します。<br>
     * UIスレッドで8秒待つのは問題があるので Handler を使っています。実際のシャットダウンには必要ありません。
     * </p>
     */
    private void shutdown(final ExecutorService executorService){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                try{
                    LogUtils.d("shutdown");
                    executorService.shutdown();
                    if(!executorService.awaitTermination(1, TimeUnit.SECONDS)){
                        LogUtils.d("shutdownNow");
                        executorService.shutdownNow();
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                    LogUtils.d("shutdownNow: " + e.getMessage());
                    executorService.shutdownNow();
                }
            }
        }, 8000);
    }

    public class ExecutorRunnable implements Runnable {
        private String mName;
        private int mSeconds;

        public ExecutorRunnable(String name, int seconds) {
            mName = name;
            mSeconds = seconds;
        }

        @Override
        public void run(){
            LogUtils.d(mName + ": ThreadId: " + Thread.currentThread().getId());
            LogUtils.d(mName + ": start");
            try{
                TimeUnit.SECONDS.sleep(mSeconds);
            }catch(InterruptedException e){
                e.printStackTrace();
                LogUtils.w(mName, e);
            }
            LogUtils.d(mName + ": end");
        }
    }

}
