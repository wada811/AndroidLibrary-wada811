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
package at.wada811.android.library.demos.daydream;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.service.dreams.DreamService;
import at.wada811.utils.LogUtils;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class DayDreamLauncher extends DreamService {

    @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();
        LogUtils.d();
        Intent intent = new Intent(this, DayDreamActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDreamingStarted(){
        super.onDreamingStarted();
        LogUtils.d();
    }

    @Override
    public void onDreamingStopped(){
        super.onDreamingStopped();
        LogUtils.d();
    }

    @Override
    public void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        LogUtils.d();
    }
}
