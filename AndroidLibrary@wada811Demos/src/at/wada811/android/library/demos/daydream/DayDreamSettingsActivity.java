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
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import at.wada811.android.library.demos.R;
import at.wada811.utils.LogUtils;
import at.wada811.utils.PreferenceUtils;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class DayDreamSettingsActivity extends FragmentActivity {

    final DayDreamSettingsActivity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daydream_settings);

        Switch switchInteractive = (Switch)findViewById(R.id.setInteractive);
        switchInteractive.setChecked(PreferenceUtils.getBoolean(self, getString(R.string.setInteractive), true));
        switchInteractive.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                LogUtils.d(buttonView.getText().toString() + ": " + isChecked);
                PreferenceUtils.putBoolean(self, getString(R.string.setInteractive), isChecked);
            }
        });
        Switch switchFullscreen = (Switch)findViewById(R.id.setFullscreen);
        switchFullscreen.setChecked(PreferenceUtils.getBoolean(self, getString(R.string.setFullscreen), true));
        switchFullscreen.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                LogUtils.d(buttonView.getText().toString() + ": " + isChecked);
                PreferenceUtils.putBoolean(self, getString(R.string.setFullscreen), isChecked);
            }
        });
    }
}
