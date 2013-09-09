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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;

public class FileUtils {

    public static boolean mkdir(File file){
        if(file.isDirectory()){
            LogUtils.d(file.getAbsolutePath() + " is direcctory");
            if(!file.exists()){
                LogUtils.d(file.getAbsolutePath() + " not exists");
                return file.mkdirs();
            }else{
                LogUtils.d();
                return true;
            }
        }else{
            LogUtils.d(file.getParentFile().getAbsolutePath() + " is directory");
            if(!file.getParentFile().exists()){
                LogUtils.d(file.getParentFile().getAbsolutePath() + " not exists");
                return file.getParentFile().mkdirs();
            }else{
                LogUtils.d();
                return true;
            }
        }
    }

    public static boolean copy(File srcFile, File distFile){
        if(!srcFile.exists()){
            return false;
        }
        if(!FileUtils.mkdir(distFile)){
            return false;
        }
        FileChannel srcChannel = null;
        FileChannel distChannel = null;
        try{
            srcChannel = new FileInputStream(srcFile).getChannel();
            distChannel = new FileOutputStream(distFile).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), distChannel);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return false;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }finally{
            if(srcChannel != null){
                try{
                    srcChannel.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(distChannel != null){
                try{
                    distChannel.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static boolean move(File srcFile, File distFile){
        boolean success = FileUtils.copy(srcFile, distFile);
        if(success){
            srcFile.delete();
        }
        return success;
    }

    public static String readFileString(String filePath){
        return FileUtils.readFileString(new File(filePath));
    }

    public static String readFileString(File file){
        BufferedReader br = null;
        StringBuilder builder = new StringBuilder();
        try{
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while((line = br.readLine()) != null){
                builder.append(line);
                builder.append("\n");
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }finally{
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
