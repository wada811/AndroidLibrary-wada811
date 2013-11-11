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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import at.wada811.app.fragment.ExpandableListFragment;

public class ActivityListFragment extends ExpandableListFragment {

    private ExpandableListListener mListener;
    private static final String    KEY_EMPTY_TEXT = "KEY_EMPTY_TEXT";

    public static interface ExpandableListListenerProvider {
        public ExpandableListListener getExpandableListListener();
    }

    public static interface ExpandableListListener {

        /**
         * Callback method to be invoked when a group in this expandable list has
         * been clicked.
         * 
         * @param parent The ExpandableListConnector where the click happened
         * @param v The view within the expandable list/ListView that was clicked
         * @param groupPosition The group position that was clicked
         * @param id The row id of the group that was clicked
         * @return True if the click was handled
         */
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id);

        /**
         * Callback method to be invoked when a group in this expandable list has
         * been collapsed.
         * 
         * @param groupPosition The group position that was collapsed
         */
        public void onGroupCollapse(int groupPosition);

        /**
         * Callback method to be invoked when a group in this expandable list has
         * been expanded.
         * 
         * @param groupPosition The group position that was expanded
         */
        public void onGroupExpand(int groupPosition);

        /**
         * Callback method to be invoked when a child in this expandable list has
         * been clicked.
         * 
         * @param parent The ExpandableListView where the click happened
         * @param v The view within the expandable list/ListView that was clicked
         * @param groupPosition The group position that contains the child that
         *        was clicked
         * @param childPosition The child position within the group
         * @param id The row id of the child that was clicked
         * @return True if the click was handled
         */
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id);
    }

    public static ActivityListFragment newInstance(String emptyText){
        ActivityListFragment activityListFragment = new ActivityListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_EMPTY_TEXT, emptyText);
        activityListFragment.setArguments(args);
        return activityListFragment;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        if(activity instanceof ExpandableListListenerProvider == false){
            throw new ClassCastException(activity.getLocalClassName() + " must implements " + ExpandableListListenerProvider.class.getSimpleName());
        }
        ExpandableListListenerProvider provider = (ExpandableListListenerProvider)activity;
        mListener = provider.getExpandableListListener();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getArguments().getString(KEY_EMPTY_TEXT));
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id){
        return mListener.onGroupClick(parent, v, groupPosition, id);
    }

    @Override
    public void onGroupCollapse(int groupPosition){
        super.onGroupCollapse(groupPosition);
        mListener.onGroupCollapse(groupPosition);
    }

    @Override
    public void onGroupExpand(int groupPosition){
        super.onGroupExpand(groupPosition);
        mListener.onGroupExpand(groupPosition);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id){
        return mListener.onChildClick(parent, v, groupPosition, childPosition, id);
    }

}
