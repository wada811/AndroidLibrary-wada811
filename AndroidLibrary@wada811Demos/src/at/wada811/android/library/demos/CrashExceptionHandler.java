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
package at.wada811.android.library.demos;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import at.wada811.utils.AndroidUtils;
import at.wada811.utils.LogUtils;
import at.wada811.utils.PreferenceUtils;

/**
 * キャッチされなかったExceptionが発生した場合にログを記録する
 * 
 * @author wada
 * 
 */
public final class CrashExceptionHandler implements UncaughtExceptionHandler {

    public static final String             FILE_NAME = "report.txt";

    private final Context                  mContext;
    private final UncaughtExceptionHandler mHandler;

    public CrashExceptionHandler(Context context) {
        mContext = context;
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    /**
     * キャッチされなかった例外発生時に各種情報をJSONでテキストファイルに書き出す
     */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable){
        PrintWriter writer = null;
        JSONObject json = new JSONObject();
        try{
            FileOutputStream outputStream = mContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            writer = new PrintWriter(outputStream);
            json.put("Build", getBuildInfo());
            json.put("PackageInfo", getPackageInfo(mContext));
            json.put("Exception", getExceptionInfo(throwable));
            json.put("SharedPreferences", getPreferencesInfo(mContext));
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }finally{
            LogUtils.e(json.toString());
            writer.print(json.toString());
            if(writer != null){
                writer.close();
            }
        }
        mHandler.uncaughtException(thread, throwable);
    }

    /**
     * ビルド情報をJSONで返す
     * 
     * @return
     * @throws JSONException
     */
    private static JSONObject getBuildInfo(){
        JSONObject json = new JSONObject();
        try{
            json.put("BRAND", Build.BRAND); // キャリア、メーカー名など(docomo)
            json.put("MODEL", Build.MODEL); // ユーザーに表示するモデル名(SO-01C)
            json.put("DEVICE", Build.DEVICE); // デバイス名(SO-01C)
            json.put("MANUFACTURER", Build.MANUFACTURER); // 製造者名(Sony Ericsson)
            json.put("VERSION.SDK_INT", Build.VERSION.SDK_INT); // フレームワークのバージョン情報(10)
            json.put("VERSION.RELEASE", Build.VERSION.RELEASE); // ユーザーに表示するバージョン番号(2.3.4)
        }catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    }

    /**
     * パッケージ情報を返す
     * 
     * @return
     * @throws JSONException
     */
    private static JSONObject getPackageInfo(Context context){
        JSONObject json = new JSONObject();
        try{
            json.put("packageName", context.getPackageName());
            json.put("versionCode", AndroidUtils.getVersionCode(context));
            json.put("versionName", AndroidUtils.getVersionName(context));
        }catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 例外情報を返す
     * 
     * @param throwable
     * @return
     * @throws JSONException
     */
    private JSONObject getExceptionInfo(Throwable throwable) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("name", throwable.getClass().getName());
        json.put("message", throwable.getMessage());
        if(throwable.getStackTrace() != null){
            // ExceptionStacktrace
            JSONArray exceptionStacktrace = new JSONArray();
            for(StackTraceElement element : throwable.getStackTrace()){
                exceptionStacktrace.put("at " + LogUtils.getMetaInfo(element));
            }
            json.put("ExceptionStacktrace", exceptionStacktrace);
        }
        if(throwable.getCause() != null){
            json.put("cause", throwable.getCause());
            // CausedStacktrace
            if(throwable.getCause().getStackTrace() != null){
                JSONArray causedStacktrace = new JSONArray();
                for(StackTraceElement element : throwable.getCause().getStackTrace()){
                    causedStacktrace.put("at " + LogUtils.getMetaInfo(element));
                }
                json.put("CausedStacktrace", causedStacktrace);
            }
        }
        return json;
    }

    /**
     * Preferencesを返す
     * 
     * @return
     * @throws JSONException
     */
    private static JSONObject getPreferencesInfo(Context context){
        SharedPreferences preferences = PreferenceUtils.getDefaultSharedPreferences(context);
        JSONObject json = new JSONObject();
        Map<String, ?> map = preferences.getAll();
        for(Entry<String, ?> entry : map.entrySet()){
            try{
                json.put(entry.getKey(), entry.getValue());
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return json;
    }

}
