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
package at.wada811.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * {@link PreferenceUtils} is wrapper class of {@link SharedPreferences}.
 * {@link PreferenceManager#createPreferenceScreen(Context)} should pass application context.
 * 
 * @author wada811
 * 
 */
public class PreferenceUtils {

    /**
     * {@link PreferenceManager#getDefaultSharedPreferences(Context)} should pass application
     * context.
     * 
     * @param context
     * @return
     */
    public static SharedPreferences getDefaultSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    /**
     * All SharedPreference to String
     * 
     * @param context
     * @return
     */
    public static String toString(Context context){
        return PreferenceUtils.getDefaultSharedPreferences(context).getAll().toString();
    }

    /**
     * All SharedPreference to JSON String
     * 
     * @param context
     * @return jsonString|null
     */
    public static String toJsonString(Context context){
        SharedPreferences preferences = PreferenceUtils.getDefaultSharedPreferences(context);
        JSONObject jsonObject = new JSONObject();
        Map<String, ?> map = preferences.getAll();
        try{
            for(Entry<String, ?> entry : map.entrySet()){
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }
        return jsonObject.toString();
    }

    /**
     * keyを持っているかを返す
     * 
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key){
        return PreferenceUtils.getDefaultSharedPreferences(context).contains(key);
    }

    /**
     * SharedPreferences に保存する
     * 
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value){
        PreferenceUtils.getDefaultSharedPreferences(context).edit().putString(key, value).commit();
    }

    /**
     * SharedPreferences から値を取得する
     * 
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Context context, String key, String defaultValue){
        return PreferenceUtils.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }

    /**
     * SharedPreferences に保存する
     * 
     * @param context
     * @param key
     * @param value
     */
    public static void putInt(Context context, String key, int value){
        PreferenceUtils.getDefaultSharedPreferences(context).edit().putInt(key, value).commit();
    }

    /**
     * SharedPreferences から値を取得する
     * 
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Context context, String key, int defaultValue){
        return PreferenceUtils.getDefaultSharedPreferences(context).getInt(key, defaultValue);
    }

    /**
     * SharedPreferences に保存する
     * 
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, Boolean value){
        PreferenceUtils.getDefaultSharedPreferences(context).edit().putBoolean(key, value).commit();
    }

    /**
     * SharedPreferences から値を取得する
     * 
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Context context, String key, Boolean defaultValue){
        return PreferenceUtils.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    /**
     * SharedPreferences に保存する
     * 
     * @param context
     * @param key
     * @param value
     */
    public static void putFloat(Context context, String key, float value){
        PreferenceUtils.getDefaultSharedPreferences(context).edit().putFloat(key, value).commit();
    }

    /**
     * SharedPreferences から値を取得する
     * 
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static float getFloat(Context context, String key, float defaultValue){
        return PreferenceUtils.getDefaultSharedPreferences(context).getFloat(key, defaultValue);
    }

    /**
     * SharedPreferences に保存する
     * 
     * @param context
     * @param key
     * @param value
     */
    public static void putLong(Context context, String key, long value){
        PreferenceUtils.getDefaultSharedPreferences(context).edit().putLong(key, value).commit();
    }

    /**
     * SharedPreferences から値を取得する
     * 
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLong(Context context, String key, long defaultValue){
        return PreferenceUtils.getDefaultSharedPreferences(context).getLong(key, defaultValue);
    }

    /**
     * SharedPreferences に保存する
     * 
     * @param context
     * @param key
     * @param values
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void putStringSet(Context context, String key, Set<String> values){
        PreferenceUtils.getDefaultSharedPreferences(context).edit().putStringSet(key, values).commit();
    }

    /**
     * SharedPreferences から値を取得する
     * 
     * @param context
     * @param key
     * @param defaultValues
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Set<String> getStringSet(Context context, String key, Set<String> defaultValues){
        return PreferenceUtils.getDefaultSharedPreferences(context).getStringSet(key, defaultValues);
    }
}
