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
package at.wada811.constant;

import android.graphics.Bitmap;

public class MediaConstant {

    /** デフォルトの保存形式 */
    public static Bitmap.CompressFormat DEFAULT_CONPRESS_FORMAT   = Bitmap.CompressFormat.JPEG;
    /** デフォルトの保存品質 */
    public static int                   DEFAULT_COMPRESS_QUALITY  = 100;
    /** 保存する画像の拡張子 */
    public static final String          SAVE_IMAGE_EXTENSION_JPEG = ".jpg";
    public static final String          SAVE_IMAGE_EXTENSION_PNG  = ".png";
//    public static final String                SAVE_IMAGE_EXTENSION_WEBP = ".webp";
    public static final String          SAVE_VIDEO_EXTENSION_3GP  = ".3gp";
    public static final String          SAVE_VIDEO_EXTENSION_MP4  = ".mp4";

    /**
     * 保存する画像の拡張子を取得する
     * 
     * @param format
     * @return
     */
    public static String getSaveExtention(Bitmap.CompressFormat format){
        if(format == Bitmap.CompressFormat.JPEG){
            return MediaConstant.SAVE_IMAGE_EXTENSION_JPEG;
        }else if(format == Bitmap.CompressFormat.PNG){
            return MediaConstant.SAVE_IMAGE_EXTENSION_PNG;
//        }else if(format == Bitmap.CompressFormat.WEBP){
//            return CameraConstant.SAVE_IMAGE_EXTENSION_WEBP;
        }else{
            return MediaConstant.SAVE_IMAGE_EXTENSION_JPEG;
        }
    }

}
