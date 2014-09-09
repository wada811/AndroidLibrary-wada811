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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
        if(array == null){
            return "null";
        }

        JSONArray json = new JSONArray();
        for(Object object : array){
            json.put(DumpUtils.toString(object));
        }
        return json.toString();
    }

    public static String toString(List<?> list){
        if(list == null){
            return "null";
        }
        JSONArray json = new JSONArray();
        for(Object object : list){
            json.put(DumpUtils.toString(object));
        }
        return json.toString();
    }

    public static String toString(Map<?, ?> map){
        if(map == null){
            return "null";
        }
        JSONObject json = new JSONObject();
        for(Entry<?, ?> entry : map.entrySet()){
            try{
                json.put(DumpUtils.toString(entry.getKey()), DumpUtils.toString(entry.getValue()));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return json.toString();
    }

    public static String toString(Bundle bundle){
        if(bundle == null){
            return "null";
        }
        JSONObject json = new JSONObject();
        for(String key : bundle.keySet()){
            try{
                Object value = bundle.get(key);
                json.put(key, DumpUtils.toString(value));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return json.toString();
    }

    public static String toString(SharedPreferences preferences){
        if(preferences == null){
            return "null";
        }
        JSONObject json = new JSONObject();
        Map<String, ?> map = preferences.getAll();
        for(Entry<String, ?> entry : map.entrySet()){
            try{
                json.put(entry.getKey(), DumpUtils.toString(entry.getValue()));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return json.toString();
    }
}
