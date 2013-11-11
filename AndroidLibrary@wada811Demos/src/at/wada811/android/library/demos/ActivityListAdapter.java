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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import at.wada811.android.library.demos.R;

public class ActivityListAdapter extends BaseExpandableListAdapter {

    private LayoutInflater                           mLayoutInflater;
    private List<String>                             mPackages;
    private HashMap<String, ArrayList<ActivityInfo>> mActivities;

    public ActivityListAdapter(Context context, List<String> packages, HashMap<String, ArrayList<ActivityInfo>> activities) {
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPackages = packages;
        mActivities = activities;
    }

    @Override
    public int getGroupCount(){
        return mPackages.size();
    }

    @Override
    public int getChildrenCount(int groupPosition){
        return mActivities.get(mPackages.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition){
        return mPackages.get(groupPosition);
    }

    @Override
    public ActivityInfo getChild(int groupPosition, int childPosition){
        return mActivities.get(mPackages.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition){
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition){
        return childPosition;
    }

    @Override
    public boolean hasStableIds(){
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
        View view;
        if(convertView == null){
            view = mLayoutInflater.inflate(R.layout.list_group, parent, false);
        }else{
            view = convertView;
        }
        String packageName = getGroup(groupPosition);
        TextView packageNameView = (TextView)view.findViewById(R.id.text);
        packageNameView.setText(packageName);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
        View view;
        if(convertView == null){
            view = mLayoutInflater.inflate(R.layout.list_child, parent, false);
        }else{
            view = convertView;
        }
        ActivityInfo activityInfo = getChild(groupPosition, childPosition);
        String[] splitActivityInfo = activityInfo.name.split("\\.");
        TextView activityNameView = (TextView)view.findViewById(R.id.text);
        activityNameView.setText(splitActivityInfo[splitActivityInfo.length - 1]);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition){
        return true;
    }

}
