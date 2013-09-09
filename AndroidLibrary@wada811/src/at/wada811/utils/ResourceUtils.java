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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources.NotFoundException;

public class ResourceUtils {

    /**
     * アプリ領域内のファイルのFileオブジェクトを取得する
     * 
     * @param context
     * @param fileName
     * @return
     */
    public static File getFile(Context context, String fileName){
        return context.getFileStreamPath(fileName);
    }

    /**
     * アプリ領域内にファイルが存在するかを返す
     * 
     * @param context
     * @param fileName
     * @return
     */
    public static boolean exists(Context context, String fileName){
        return ResourceUtils.getFile(context, fileName).exists();
    }

    /**
     * アプリ領域内のファイルから文字列を取得する
     * 
     * @param context
     * @param fileName
     * @return
     */
    public static String readFileString(Context context, String fileName){
        FileInputStream fis = null;
        BufferedReader br = null;
        StringBuilder builder = new StringBuilder();
        try{
            fis = context.openFileInput(fileName);
            br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while((line = br.readLine()) != null){
                builder.append(line);
                builder.append("\n");
            }
        }catch(NotFoundException e){
            e.printStackTrace();
            return null;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }finally{
            if(fis != null){
                try{
                    fis.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(br != null){
                try{
                    br.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

    /**
     * res/raw フォルダのファイルから文字列を取得する
     * 
     * @param context
     * @param resId
     * @return
     */
    public static String readRawString(Context context, int resId){
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder builder = new StringBuilder();
        try{
            is = context.getResources().openRawResource(resId);
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = br.readLine()) != null){
                builder.append(line);
                builder.append("\n");
            }
        }catch(NotFoundException e){
            e.printStackTrace();
            return null;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }finally{
            if(is != null){
                try{
                    is.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(br != null){
                try{
                    br.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

    /**
     * assets フォルダのファイルから文字列を取得する
     * 
     * @param context
     * @param fileName
     * @return
     */
    public static String readAssetsString(Context context, String fileName){
        AssetManager am = context.getAssets();
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder builder = new StringBuilder();
        try{
            is = am.open(fileName);
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = br.readLine()) != null){
                builder.append(line);
                builder.append("\n");
            }
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }finally{
            if(is != null){
                try{
                    is.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(br != null){
                try{
                    br.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

}
