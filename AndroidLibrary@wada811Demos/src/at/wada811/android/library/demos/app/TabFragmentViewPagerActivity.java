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
package at.wada811.android.library.demos.app;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import at.wada811.android.library.demos.R;
import at.wada811.utils.LogUtils;

public class TabFragmentViewPagerActivity extends ActionBarActivity {

    private ActionBar mActionBar;
    private static final String SELECTED_TAB_POSITION = "SelectedTabPosition";
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(new TabViewPagerAdapter(this));
        mViewPager.setOnPageChangeListener(new OnPageChangeListener(){
            @Override
            public void onPageSelected(int position){
                mActionBar.selectTab(mActionBar.getTabAt(position));
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

            }

            @Override
            public void onPageScrollStateChanged(int state){

            }
        });

        mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        addTab("TAB 0");
        addTab("TAB 1");
        addTab("TAB 2");
        addTab("TAB 3");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        Tab selectedTab = mActionBar.getSelectedTab();
        int position = selectedTab.getPosition();
        outState.putInt(SELECTED_TAB_POSITION, position);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        int position = savedInstanceState.getInt(SELECTED_TAB_POSITION);
        LogUtils.i(String.valueOf(position));
        mActionBar.selectTab(mActionBar.getTabAt(position));
    }

    private void addTab(String tag){
        Tab tab = mActionBar.newTab();
        tab.setTag(tag);
        tab.setText(tag);
        tab.setTabListener(new TabListener(){
            @Override
            public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction){

            }

            @Override
            public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction){
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction){

            }

        });
        mActionBar.addTab(tab);
    }

}
