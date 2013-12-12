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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import at.wada811.android.library.demos.R;
import at.wada811.android.library.demos.app.TabFragment.TabFragmentCallback;
import at.wada811.android.library.demos.app.TabFragment.TabFragmentCallbackProvider;
import at.wada811.utils.LogUtils;

public class TabActionBarActivity extends ActionBarActivity implements TabFragmentCallbackProvider {

    private ActionBar mActionBar;
    private static final String SELECTED_TAB_POSITION = "SelectedTabPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionbar_tab);

        mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        addTab("TAB 0");
        addTab("TAB 1");
        addTab("TAB 2");
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
        tab.setTabListener(new TabFragmentListener<TabFragment>(this, tag, TabFragment.class));
        mActionBar.addTab(tab);
    }

    public class TabFragmentListener<T extends Fragment> implements TabListener {
        private Fragment fragment;
        private final Activity activity;
        private final String tag;
        private final Class<T> clazz;

        public TabFragmentListener(Activity activity, String tag, Class<T> clazz) {
            this.activity = activity;
            this.tag = tag;
            this.clazz = clazz;
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction){

        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction){
            if(fragment == null){
                fragment = Fragment.instantiate(activity, clazz.getName());
                fragmentTransaction.replace(android.R.id.content, fragment, tag);
            }else{
                fragmentTransaction.attach(fragment);
            }
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction){
            if(fragment != null){
                fragmentTransaction.detach(fragment);
            }
        }
    }

    @Override
    public TabFragmentCallback getTabCallback(){
        return new TabFragmentCallback(){
            @Override
            public void onAttach(TabFragment tabFragment){
                LogUtils.d(tabFragment.getTag());
            }

            @Override
            public void onDetach(TabFragment tabFragment){
                LogUtils.v(tabFragment.getTag());
            }
        };
    }

}
