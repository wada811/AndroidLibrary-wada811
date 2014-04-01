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
package at.wada811.utils;

import android.content.SharedPreferences;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DumpUtils {

    private DumpUtils() {

    }

    public static String toString(boolean value){
        return String.valueOf(value);
    }

    public static String toString(char value){
        return String.valueOf(value);
    }

    public static String toString(char[] value){
        return String.valueOf(value);
    }

    public static String toString(double value){
        return String.valueOf(value);
    }

    public static String toString(float value){
        return String.valueOf(value);
    }

    public static String toString(int value){
        return String.valueOf(value);
    }

    public static String toString(long value){
        return String.valueOf(value);
    }

    public static String toString(Object value){
        return String.valueOf(value);
    }

    public static String toString(Object[] array){
        final String fSTART_CHAR = "[";
        final String fEND_CHAR = "]";
        final String fSEPARATOR = ", ";
        final String fNULL = "null";

        if(array == null){
            return fNULL;
        }

        StringBuilder result = new StringBuilder(fSTART_CHAR);
        int length = Array.getLength(array);
        for(int index = 0; index < length; ++index){
            Object item = Array.get(array, index);
            if(isNonNullArray(item)){
                //recursive call!
                result.append(DumpUtils.toString((Object[])item));
            }else{
                result.append(item);
            }
            if(!isLastItem(index, length)){
                result.append(fSEPARATOR);
            }
        }
        result.append(fEND_CHAR);
        return result.toString();
    }

    private static boolean isNonNullArray(Object object){
        return object != null && object.getClass().isArray();
    }

    private static boolean isLastItem(int index, int length){
        return index == length - 1;
    }

    @SuppressWarnings("unchecked")
    public static String toString(List<Object> list){
        final String fSTART_CHAR = "[";
        final String fEND_CHAR = "]";
        final String fSEPARATOR = ", ";
        final String fNULL = "null";

        if(list == null){
            return fNULL;
        }

        StringBuilder result = new StringBuilder(fSTART_CHAR);
        int length = list.size();
        for(int index = 0; index < length; ++index){
            Object item = list.get(index);
            if(isNonNullList(item)){
                //recursive call!
                result.append(DumpUtils.toString((List<Object>)item));
            }else{
                result.append(item);
            }
            if(!isLastItem(index, length)){
                result.append(fSEPARATOR);
            }
        }
        result.append(fEND_CHAR);
        return result.toString();
    }

    private static boolean isNonNullList(Object object){
        return object != null && object instanceof List;
    }

    public static String toString(Map<Object, Object> map){
        final String fSTART_CHAR = "{";
        final String fEND_CHAR = "}";
        final String fKEY_VALUE_SEPARATOR = ": ";
        final String fENTRY_SEPARATOR = ", ";
        final String fNULL = "null";

        if(map == null){
            return fNULL;
        }

        StringBuilder result = new StringBuilder(fSTART_CHAR);
        int index = 0;
        int length = map.size();
        for(Entry<Object, Object> item : map.entrySet()){
            result.append(DumpUtils.toString(item.getKey()));
            result.append(fKEY_VALUE_SEPARATOR);
            result.append(DumpUtils.toString(item.getValue()));
            if(!isLastItem(index, length)){
                result.append(fENTRY_SEPARATOR);
            }
            index++;
        }
        result.append(fEND_CHAR);
        return result.toString();
    }

    public static String toString(Bundle bundle){
        HashMap<String, String> dumpBundle = new HashMap<String, String>();
        for(String key : bundle.keySet()){
            Object value = bundle.get(key);
            String valueStr = DumpUtils.toString(value);
            dumpBundle.put(key, valueStr);
        }
        return DumpUtils.toString(dumpBundle);
    }

    public static String toString(SharedPreferences preferences){
        JSONObject preferencesJson = new JSONObject();
        Map<String, ?> map = preferences.getAll();
        try{
            for(Entry<String, ?> entry : map.entrySet()){
                preferencesJson.put(entry.getKey(), entry.getValue());
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return preferencesJson.toString();
    }
}
