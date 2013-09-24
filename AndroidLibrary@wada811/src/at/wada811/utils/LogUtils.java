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

import android.util.Log;
import at.wada811.android.library.BuildConfig;

public class LogUtils {

    private static final String  TAG     = "LogUtil";
    private static final boolean isDebug = true;

    public static void v(){
        if(BuildConfig.DEBUG && isDebug){
            Log.v(TAG, getMetaInfo());
        }
    }

    public static void v(String message){
        if(BuildConfig.DEBUG && isDebug){
            Log.v(TAG, getMetaInfo() + null2str(message));
        }
    }

    public static void v(String tag, String message){
        if(BuildConfig.DEBUG && isDebug){
            Log.v(tag, getMetaInfo() + null2str(message));
        }
    }

    public static void d(){
        if(BuildConfig.DEBUG && isDebug){
            Log.d(TAG, getMetaInfo());
        }
    }

    public static void d(String message){
        if(BuildConfig.DEBUG && isDebug){
            Log.d(TAG, getMetaInfo() + null2str(message));
        }
    }

    public static void d(String tag, String message){
        if(BuildConfig.DEBUG && isDebug){
            Log.d(tag, getMetaInfo() + null2str(message));
        }
    }

    public static void i(){
        if(BuildConfig.DEBUG && isDebug){
            Log.i(TAG, getMetaInfo());
        }
    }

    public static void i(String message){
        if(BuildConfig.DEBUG && isDebug){
            Log.i(TAG, getMetaInfo() + null2str(message));
        }
    }

    public static void i(String tag, String message){
        if(BuildConfig.DEBUG && isDebug){
            Log.i(tag, getMetaInfo() + null2str(message));
        }
    }

    public static void w(String message){
        if(BuildConfig.DEBUG && isDebug){
            Log.w(TAG, getMetaInfo() + null2str(message));
        }
    }

    public static void w(String tag, String message){
        if(BuildConfig.DEBUG && isDebug){
            Log.w(tag, getMetaInfo() + null2str(message));
        }
    }

    public static void w(String message, Throwable e){
        if(BuildConfig.DEBUG && isDebug){
            Log.w(TAG, getMetaInfo() + null2str(message), e);
            printThrowable(e);
            if(e.getCause() != null){
                printThrowable(e.getCause());
            }
        }
    }

    public static void e(String message){
        if(BuildConfig.DEBUG && isDebug){
            Log.e(TAG, getMetaInfo() + null2str(message));
        }
    }

    public static void e(String tag, String message){
        if(BuildConfig.DEBUG && isDebug){
            Log.e(tag, getMetaInfo() + null2str(message));
        }
    }

    public static void e(String message, Throwable e){
        if(BuildConfig.DEBUG && isDebug){
            Log.e(TAG, getMetaInfo() + null2str(message), e);
            printThrowable(e);
            if(e.getCause() != null){
                printThrowable(e.getCause());
            }
        }
    }

    public static void e(Throwable e){
        if(BuildConfig.DEBUG && isDebug){
            printThrowable(e);
            if(e.getCause() != null){
                printThrowable(e.getCause());
            }
        }
    }

    private static String null2str(String string){
        if(string == null){
            return "(null)";
        }
        return string;
    }

    /**
     * 例外のスタックトレースをログに出力する
     * 
     * @param e
     */
    private static void printThrowable(Throwable e){
        Log.e(TAG, e.getClass().getName() + ": " + e.getMessage());
        for(StackTraceElement element : e.getStackTrace()){
            Log.e(TAG, "  at " + LogUtils.getMetaInfo(element));
        }
    }

    /**
     * ログ呼び出し元のメタ情報を取得する
     * 
     * @return [className#methodName:line]
     */
    private static String getMetaInfo(){
        // スタックトレースから情報を取得 // 0: VM, 1: Thread, 2: LogUtil#getMetaInfo, 3: LogUtil#d など, 4: 呼び出し元
        final StackTraceElement element = Thread.currentThread().getStackTrace()[4];
        return LogUtils.getMetaInfo(element);
    }

    /**
     * スタックトレースからクラス名、メソッド名、行数を取得する
     * 
     * @return [className#methodName:line]
     */
    public static String getMetaInfo(StackTraceElement element){
        // クラス名、メソッド名、行数を取得
        final String fullClassName = element.getClassName();
        final String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        final String methodName = element.getMethodName();
        final int lineNumber = element.getLineNumber();
        // メタ情報
        final String metaInfo = "[" + simpleClassName + "#" + methodName + ":" + lineNumber + "]";
        return metaInfo;
    }

}
