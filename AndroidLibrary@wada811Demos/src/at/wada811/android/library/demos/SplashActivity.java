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

import java.io.File;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ScrollView;
import android.widget.TextView;
import at.wada811.utils.ResourceUtils;

public class SplashActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // catch UncaughtException
        Thread.setDefaultUncaughtExceptionHandler(new CrashExceptionHandler(getApplicationContext()));
        super.onCreate(savedInstanceState);
        // 前回起動時にエラーがあればエラーテキストを送信する
        File file = ResourceUtils.getFile(this, CrashExceptionHandler.FILE_NAME);
        if(file.exists()){
            String report = ResourceUtils.readFileString(this, CrashExceptionHandler.FILE_NAME);
            ScrollView scrollView = new ScrollView(this);
            TextView textView = new TextView(this);
            textView.setText(report);
            scrollView.addView(textView);
            setContentView(scrollView);
            file.delete();
        }else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
