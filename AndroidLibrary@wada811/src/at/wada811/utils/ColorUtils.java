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

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

public class ColorUtils {

    /**
     * ResourceColor を取得する
     * 
     * @param context
     * @param resId
     */
    public static int getColor(Context context, int resId){
        return context.getResources().getColor(resId);
    }

    /**
     * ColorDrawable を取得する
     * 
     * @param context
     * @param resId
     */
    public static ColorDrawable getColorDrawable(Context context, int resId){
        return new ColorDrawable(ColorUtils.getColor(context, resId));
    }

}
