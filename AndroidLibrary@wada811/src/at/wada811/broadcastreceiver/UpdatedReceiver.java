/*
 * Copyright 2014 ssl001<at.wada811@gmail.com>
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
 * public class YourUpdatedReceiver extends UpdatedReceiver {
 *     &#064;Override
 *     public void onUpdated(){
 *         // Your code here
 *     }
 * }
 * </pre>
 * 
 * <h2>AndroidManifest.xml</h2>
 * 
 * <pre>
 * &lt;receiver android:name=".YourUpdatedReceiver"&gt;
 *     &lt;intent-filter&gt;
 *         &lt;action android:name="android.intent.action.PACKAGE_REPLACED" /&gt;
 *         &lt;data android:scheme="package" /&gt;
 *     &lt;/intent-filter&gt;
 * &lt;/receiver&gt;
 * </pre>
 */
public abstract class UpdatedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        String action = intent.getAction();
        String packagePath = intent.getDataString(); // package:app.package.name
        if(Intent.ACTION_PACKAGE_REPLACED.equals(action) && packagePath.equals("package:" + context.getPackageName())){
            onUpdated(context);
        }
    }

    /**
     * This method is called when the BroadcastReceiver is receiving
     * my package's {@link Intent#ACTION_PACKAGE_REPLACED}.
     */
    public abstract void onUpdated(Context context);
}
