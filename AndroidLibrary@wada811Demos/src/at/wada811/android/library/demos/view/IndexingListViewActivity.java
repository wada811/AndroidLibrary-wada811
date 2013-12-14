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
import android.support.v4.app.FragmentManager;
import at.wada811.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

public class IndexingListViewActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_indexing_listview);

        LogUtils.d();
        FragmentManager fragmentManager = getSupportFragmentManager();
        IndexingListFragment listFragment = IndexingListFragment.newInstance("Not Found!");
        fragmentManager.beginTransaction().replace(android.R.id.content, listFragment).commit();

        List<String> groups = new ArrayList<String>();
        ArrayList<ArrayList<String>> children = new ArrayList<ArrayList<String>>();
        for(int i = (int)'a', count = i + 26; i < count; i++){
            String group = new String(new char[]{
                (char)i
            });
            LogUtils.d(group);
            groups.add(group);
            ArrayList<String> child = new ArrayList<String>();
            for(int j = 0; j < 15; j++){
                child.add(String.valueOf(j));
            }
            children.add(child);
        }
        listFragment.setListAdapter(new IndexingListAdapter(this, groups, children));
        LogUtils.d();
    }
}
