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
package at.wada811.android.library.demos.loader;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import at.wada811.android.library.demos.R;
import at.wada811.utils.AndroidUtils;

public class LoaderListActivity extends FragmentActivity {

    private LoaderListFragment mLoaderListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader_list);

        mLoaderListFragment = (LoaderListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        initSearchView(menu.findItem(R.id.search));
        return true;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initSearchView(final MenuItem menuItem){
        if(AndroidUtils.isMoreThanBuildVersion(Build.VERSION_CODES.ICE_CREAM_SANDWICH)){
            final SearchView searchView = (SearchView)menuItem.getActionView();
            searchView.setOnQueryTextListener(new OnQueryTextListener(){
                @Override
                public boolean onQueryTextSubmit(String query){
                    mLoaderListFragment.init();
                    mLoaderListFragment.searchImageWithKeyword(query);
                    menuItem.collapseActionView();
                    getActionBar().setTitle(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText){
                    return false;
                }
            });
            searchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener(){
                @Override
                public void onFocusChange(View v, boolean hasFocus){
                    if(!hasFocus){
                        menuItem.collapseActionView();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // TODO Fragment と Loader を併用した際に onBackPressed で以下の例外が発生するのを防ぐ
        // java.lang.IllegalStateException: Can not perform this action inside of onLoadFinished
    }
}
