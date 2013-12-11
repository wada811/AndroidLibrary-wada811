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
package at.wada811.android.library.demos.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import at.wada811.android.library.demos.R;
import at.wada811.utils.LogUtils;

public class ViewPagerActivity extends FragmentActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(new ViewPagerAdapter(this));
        mViewPager.setOffscreenPageLimit(6);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener(){
            @Override
            public void onPageSelected(int position){
                LogUtils.d("position: " + position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
//                LogUtils.d("position: " + position);
//                LogUtils.d("positionOffset: " + positionOffset);
//                LogUtils.d("positionOffsetPixels: " + positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state){
                switch(state){
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        LogUtils.d("state: ViewPager.SCROLL_STATE_DRAGGING");
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        LogUtils.d("state: ViewPager.SCROLL_STATE_IDLE");
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        LogUtils.d("state: ViewPager.SCROLL_STATE_SETTLING");
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
