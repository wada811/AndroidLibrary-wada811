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

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import at.wada811.app.fragment.ExpandableListFragment;

public class IndexingListFragment extends ExpandableListFragment {

    private static final String KEY_EMPTY_TEXT = "KEY_EMPTY_TEXT";

    public static IndexingListFragment newInstance(String emptyText){
        IndexingListFragment fragment = new IndexingListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_EMPTY_TEXT, emptyText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

//        if(activity instanceof ExpandableListListenerProvider == false){
//            throw new ClassCastException(activity.getLocalClassName() + " must implements " + ExpandableListListenerProvider.class.getSimpleName());
//        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getArguments().getString(KEY_EMPTY_TEXT));
        getExpandableListView().setIndicatorBounds(0, 0);
        getExpandableListView().setFastScrollEnabled(true);
        getExpandableListView().setFastScrollAlwaysVisible(true);
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id){
        return true;
    }
}
