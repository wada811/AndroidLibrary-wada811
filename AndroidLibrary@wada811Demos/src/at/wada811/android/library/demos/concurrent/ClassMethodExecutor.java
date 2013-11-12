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
import at.wada811.utils.LogUtils;
import at.wada811.utils.PreferenceUtils;

public class ClassMethodExecutor {

    public static class Async {

        public static final String KEY = ClassMethodExecutor.Async.class.getSimpleName();

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

    public static class SyncExecutor {

        public static final String KEY = ClassMethodExecutor.SyncExecutor.class.getSimpleName();

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
}
