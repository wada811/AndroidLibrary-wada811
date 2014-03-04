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

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import at.wada811.utils.AndroidUtils;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * <h1>Usage</h1>
 * 
 * <h2>Java</h2>
 * 
 * <pre>
 * public class YourDateTimeChangedReceiver extends DateTimeChangedReceiver {
 *     &#064;Override
 *     public void onDateTimeChanged(){
 *         // Your code here
 *     }
 * }
 * </pre>
 * 
 * <h2>AndroidManifest.xml</h2>
 * 
 * <pre>
 * <receiver
 *     android:name=".YourDateTimeChangedReceiver"
 *     >
 *     <intent-filter>
 *         <action android:name="your.package.name.action.DATE_CHANGED" />
 *     </intent-filter>
 *     <intent-filter>
 *         <action android:name="android.intent.action.TIMEZONE_CHANGED" />
 *         <action android:name="android.intent.action.TIME_SET" />
 *     </intent-filter>
 * </receiver>
 * </pre>
 */
public abstract class DateTimeChangedReceiver extends BroadcastReceiver {

    public static String getDateChangedAction(Context context){
        return context.getPackageName() + ".action.DATE_CHANGED";
    }

    /**
     * Do not override
     */
    @Override
    public void onReceive(Context context, Intent intent){
        if(isTimeChanged(context, intent)){
            onTimeChanged(context);
        }
        if(isDateChanged(context, intent)){
            onDateChanged(context);
            registerDateChangeReceiver(context);
        }
    }

    /**
     * This method is called when the BroadcastReceiver is receiving
     * date change event.
     */
    public abstract void onDateChanged(Context context);

    /**
     * This method is called when the BroadcastReceiver is receiving
     * time change event.
     */
    public abstract void onTimeChanged(Context context);

    /**
     * <p>
     * タイムゾーン変更または時間変更の検知
     * </p>
     * <p>
     * ユーザの設定で自動設定(ネットワークから提供されたタイムゾーン/日時を使用する設定)になっている場合は、 接続するネットワークが変わると変更イベントが飛ぶので注意
     * </p>
     * 
     * @param context
     * @param intent
     * @return true if intent's action equals {@link Intent#ACTION_TIME_CHANGED} or
     *         {@link Intent#ACTION_TIMEZONE_CHANGED}, otherwise false
     */
    public static boolean isTimeChanged(Context context, Intent intent){
        if(context == null){
            throw new IllegalArgumentException("Context must not be null.");
        }
        if(intent == null){
            throw new IllegalArgumentException("Intent must not be null.");
        }
        String action = intent.getAction();
        return Intent.ACTION_TIME_CHANGED.equals(action) || Intent.ACTION_TIMEZONE_CHANGED.equals(action);
    }

    /**
     * @param context
     * @param intent
     * @return true if intent's action equals {@link DateTimeChangedReceiver#ACTION_DATE_CHANGED},
     *         otherwise false
     */
    public static boolean isDateChanged(Context context, Intent intent){
        if(context == null){
            throw new IllegalArgumentException("Context must not be null.");
        }
        if(intent == null){
            throw new IllegalArgumentException("Intent must not be null.");
        }
        String action = intent.getAction();
        return getDateChangedAction(context).equals(action);
    }

    /**
     * 翌日の0時に {@link AlarmManager} をセットする
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void registerDateChangeReceiver(Context context){
        unregisterDateChangeReceiver(context);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = createDateChangePendingIntent(context);
        if(AndroidUtils.isLessThanBuildVersion(Build.VERSION_CODES.KITKAT)){
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }else{
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    /**
     * すでにセットされた Alarm を解除する
     */
    public static void unregisterDateChangeReceiver(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createDateChangePendingIntent(context));
    }

    /**
     * 日付変更の PendingIntent を生成する
     */
    private static PendingIntent createDateChangePendingIntent(Context context){
        Intent intent = new Intent(getDateChangedAction(context));
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
