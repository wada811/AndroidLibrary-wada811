/*
 * Copyright 2014 wada811<at.wada811@gmail.com>
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
package at.wada811.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * <h1>Usage</h1>
 * 
 * <h2>Java</h2>
 * 
 * <pre>
 * public class YourBootCompletedReceiver extends BootCompletedReceiver {
 *     &#064;Override
 *     public void onBootCOmpleted(){
 *         // Your code here
 *     }
 * }
 * </pre>
 * 
 * <h2>AndroidManifest.xml</h2>
 * 
 * <pre>
 * &lt;receiver
 *      android:name=".YourBootCompletedReceiver"
 *      android:permission="android.permission.RECEIVE_BOOT_COMPLETED" &gt;
 *      &lt;intent-filter&gt;
 *          &lt;action android:name="android.intent.action.BOOT_COMPLETED" /&gt;
 *      &lt;/intent-filter&gt;
 * &lt;/receiver&gt;
 * </pre>
 */
public abstract class BootCompletedReceiver extends BroadcastReceiver {

    /**
     * Do not override
     */
    @Override
    public void onReceive(Context context, Intent intent){
        if(isBootCompleted(context, intent)){
            onBootCompleted(context);
        }
    }

    /**
     * This method is called when the BroadcastReceiver is receiving
     * {@link Intent#ACTION_BOOT_COMPLETED}.
     */
    public abstract void onBootCompleted(Context context);

    /**
     * @param context
     * @param intent
     * @return true if intent's action equals {@link Intent#ACTION_BOOT_COMPLETED}, otherwise false
     */
    public static boolean isBootCompleted(Context context, Intent intent){
        if(context == null){
            throw new IllegalArgumentException("Context must not be null.");
        }
        if(intent == null){
            throw new IllegalArgumentException("Intent must not be null.");
        }
        String action = intent.getAction();
        return Intent.ACTION_BOOT_COMPLETED.equals(action);
    }
}
