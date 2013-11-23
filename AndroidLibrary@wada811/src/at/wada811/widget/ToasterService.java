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

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import at.wada811.utils.LogUtils;
import at.wada811.widget.Toaster.ToasterCallback;

public class ToasterService extends Service {

    public static final String TAG = ToasterService.class.getSimpleName();

    private static final int MAX_BREADS = 50;
    private static final int MESSAGE_TIMEOUT = 2;
    final IBinder mForegroundToken = new Binder();
    private WorkerHandler mHandler;
    private ArrayList<BreadRecord> mBreadQueue;

    //サービスに接続するためのBinder
    public class ToasterServiceBinder extends Binder {
        //サービスの取得
        ToasterService getService(){
            return ToasterService.this;
        }
    }
    //Binderの生成
    private final IBinder mBinder = new ToasterServiceBinder();

    @Override
    public IBinder onBind(Intent intent){
        mBreadQueue = new ArrayList<BreadRecord>();
        mHandler = new WorkerHandler();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        LogUtils.d();
        return super.onUnbind(intent);
    }

    public static final class BreadRecord {
        final ToasterCallback callback;
        int duration;

        public BreadRecord(ToasterCallback callback, int duration) {
            this.callback = callback;
            this.duration = duration;
        }

        public void update(int duration){
            this.duration = duration;
        }

        @Override
        public final String toString(){
            return "BreadRecord{" + Integer.toHexString(System.identityHashCode(this)) + " callback=" + callback + " duration=" + duration;
        }
    }

    public void enqueueBread(ToasterCallback callback, int duration){
        LogUtils.i(TAG, "enqueueBread callback=" + callback + " duration=" + duration);

        if(callback == null){
            LogUtils.e(TAG, "Not doing toast. callback=" + callback);
            return;
        }

        synchronized(mBreadQueue){
            long callingId = Binder.clearCallingIdentity();
            try{
                BreadRecord record;
                int index = indexOfBreadLocked(callback);
                // If it's already in the queue, we update it in place, we don't
                // move it to the end of the queue.
                if(index >= 0){
                    record = mBreadQueue.get(index);
                    record.update(duration);
                }else{
                    // Limit the number of toasts that any given package except the android
                    // package can enqueue.  Prevents DOS attacks and deals with leaks.
                    int count = 0;
                    final int N = mBreadQueue.size();
                    for(int i = 0; i < N; i++){
                        count++;
                        if(count >= MAX_BREADS){
                            LogUtils.e(TAG, "Package has already posted " + count + " toasts. Not showing more.");
                            return;
                        }
                    }

                    record = new BreadRecord(callback, duration);
                    mBreadQueue.add(record);
                    index = mBreadQueue.size() - 1;
                }
                // If it's at index 0, it's the current toast.  It doesn't matter if it's
                // new or just been updated.  Call back and tell it to show itself.
                // If the callback fails, this will remove it from the list, so don't
                // assume that it's valid after this.
                if(index == 0){
                    showNextBreadLocked();
                }
            }finally{
                Binder.restoreCallingIdentity(callingId);
            }
        }
    }

    public void cancelAllBread(){
        LogUtils.i(TAG, "cancelAllBread");
        synchronized(mBreadQueue){
            for(BreadRecord record : mBreadQueue){
                cancelBread(record.callback);
            }
        }
    }

    public void cancelBread(ToasterCallback callback){
        LogUtils.i(TAG, "cancelBread callback=" + callback);

        if(callback == null){
            LogUtils.e(TAG, "Not cancelling notification. callback=" + callback);
            return;
        }

        synchronized(mBreadQueue){
            long callingId = Binder.clearCallingIdentity();
            try{
                int index = indexOfBreadLocked(callback);
                if(index >= 0){
                    cancelBreadLocked(index);
                }else{
                    LogUtils.w(TAG, "Bread already cancelled. callback=" + callback);
                }
            }finally{
                Binder.restoreCallingIdentity(callingId);
            }
        }
    }

    private void showNextBreadLocked(){
        BreadRecord record = mBreadQueue.get(0);
        while(record != null){
            LogUtils.d(TAG, "Show callback=" + record.callback);
            try{
                record.callback.show();
                scheduleTimeoutLocked(record, false);
                return;
            }catch(/* Remote */Exception e){
                LogUtils.w(TAG, "Object died trying to show notification " + record.callback);
                // remove it from the list and let the process die
                int index = mBreadQueue.indexOf(record);
                if(index >= 0){
                    mBreadQueue.remove(index);
                }
                if(mBreadQueue.size() > 0){
                    record = mBreadQueue.get(0);
                }else{
                    record = null;
                }
            }
        }
    }

    private void cancelBreadLocked(int index){
        BreadRecord record = mBreadQueue.get(index);
        try{
            record.callback.hide();
        }catch(/* Remote */Exception e){
            LogUtils.w(TAG, "Object died trying to hide notification " + record.callback);
            // don't worry about this, we're about to remove it from the list anyway
        }
        mBreadQueue.remove(index);
        if(mBreadQueue.size() > 0){
            // Show the next one. If the callback fails, this will remove
            // it from the list, so don't assume that the list hasn't changed
            // after this point.
            showNextBreadLocked();
        }
    }

    private void scheduleTimeoutLocked(BreadRecord record, boolean immediate){
        Message m = Message.obtain(mHandler, MESSAGE_TIMEOUT, record);
        long delay = immediate ? 0 : record.duration;
        mHandler.removeCallbacksAndMessages(record);
        mHandler.sendMessageDelayed(m, delay);
    }

    private void handleTimeout(BreadRecord record){
        LogUtils.d(TAG, "Timeout  callback=" + record.callback);
        synchronized(mBreadQueue){
            int index = indexOfBreadLocked(record.callback);
            if(index >= 0){
                cancelBreadLocked(index);
            }
        }
    }

    // lock on mBreadQueue
    private int indexOfBreadLocked(ToasterCallback callback){
        ArrayList<BreadRecord> list = mBreadQueue;
        int len = list.size();
        for(int i = 0; i < len; i++){
            BreadRecord record = list.get(i);
            if(record.callback.equals(callback)){
                return i;
            }
        }
        return -1;
    }

    @SuppressLint("HandlerLeak")
    private final class WorkerHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case MESSAGE_TIMEOUT:
                    handleTimeout((BreadRecord)msg.obj);
                    break;
            }
        }
    }

}
