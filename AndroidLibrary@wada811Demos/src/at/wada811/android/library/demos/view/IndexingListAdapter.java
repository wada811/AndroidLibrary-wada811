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

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import at.wada811.android.library.demos.R;
import at.wada811.utils.ColorUtils;
import at.wada811.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

public class IndexingListAdapter extends BaseExpandableListAdapter implements SectionIndexer {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> mGroups;
    private ArrayList<ArrayList<String>> mChildren;
    private SparseIntArray mIndexer;

    public IndexingListAdapter(Context context, List<String> groups, ArrayList<ArrayList<String>> children) {
        LogUtils.d();
        mContext = context;
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGroups = groups;
        mChildren = children;
        mIndexer = new SparseIntArray();
        int position = 0;
        int section = 0;
        for(ArrayList<String> child : children){
            for(String value : child){
                mIndexer.put(position++, section);
            }
            section++;
        }
    }

//     ____                 _____                            _       _     _      
//    | __ )  __ _ ___  ___| ____|_  ___ __   __ _ _ __   __| | __ _| |__ | | ___ 
//    |  _ \ / _` / __|/ _ \  _| \ \/ / '_ \ / _` | '_ \ / _` |/ _` | '_ \| |/ _ \
//    | |_) | (_| \__ \  __/ |___ >  <| |_) | (_| | | | | (_| | (_| | |_) | |  __/
//    |____/ \__,_|___/\___|_____/_/\_\ .__/ \__,_|_| |_|\__,_|\__,_|_.__/|_|\___|
//                                    |_|                                         
//     _     _     _      _       _             _            
//    | |   (_)___| |_   / \   __| | __ _ _ __ | |_ ___ _ __ 
//    | |   | / __| __| / _ \ / _` |/ _` | '_ \| __/ _ \ '__|
//    | |___| \__ \ |_ / ___ \ (_| | (_| | |_) | ||  __/ |   
//    |_____|_|___/\__/_/   \_\__,_|\__,_| .__/ \__\___|_|   
//                                       |_|                 
    @Override
    public int getGroupCount(){
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition){
        return mChildren.get(groupPosition).size();
    }

    @Override
    public String getGroup(int groupPosition){
        return mGroups.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition){
        return mChildren.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition){
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition){
        return groupPosition + childPosition;
    }

    @Override
    public boolean hasStableIds(){
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
        LogUtils.d();
        View view;
        if(convertView == null){
            view = mLayoutInflater.inflate(R.layout.list_group, parent, false);
        }else{
            view = convertView;
        }
        view.setBackgroundColor(ColorUtils.getColor(mContext, R.color.blue_light));
        String groupName = getGroup(groupPosition);
        TextView textView = (TextView)view.findViewById(R.id.text);
        textView.setText(groupName);
        if(!isExpanded){
            ((ExpandableListView)parent).expandGroup(groupPosition);
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
        LogUtils.d();
        View view;
        if(convertView == null){
            view = mLayoutInflater.inflate(R.layout.list_child, parent, false);
        }else{
            view = convertView;
        }
        String childName = getChild(groupPosition, childPosition);
        TextView textView = (TextView)view.findViewById(R.id.text);
        textView.setText(childName);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition){
        return false;
    }

//     ____            _   _             ___           _                    
//    / ___|  ___  ___| |_(_) ___  _ __ |_ _|_ __   __| | _____  _____ _ __ 
//    \___ \ / _ \/ __| __| |/ _ \| '_ \ | || '_ \ / _` |/ _ \ \/ / _ \ '__|
//     ___) |  __/ (__| |_| | (_) | | | || || | | | (_| |  __/>  <  __/ |   
//    |____/ \___|\___|\__|_|\___/|_| |_|___|_| |_|\__,_|\___/_/\_\___|_|      
    @Override
    public String[] getSections(){
        return mGroups.toArray(new String[mGroups.size()]);
    }

    @Override
    public int getPositionForSection(int section){
        LogUtils.d("section: " + section);
        LogUtils.v("mIndexer.get(section): " + mIndexer.get(section));
        LogUtils.v("mIndexer.keyAt(section): " + mIndexer.keyAt(section));
        LogUtils.v("mIndexer.valueAt(section): " + mIndexer.valueAt(section));
        LogUtils.v("mIndexer.indexOfKey(section): " + mIndexer.indexOfKey(section));
        LogUtils.v("mIndexer.indexOfValue(section): " + mIndexer.indexOfValue(section));
        // keyAt は スクロールバーのつまみが一致するがスクロールすると突き抜けていく
        int position = mIndexer.keyAt(section);
        LogUtils.e("position: " + position);
        return position;
    }

    @Override
    public int getSectionForPosition(int position){
        LogUtils.i("position: " + position);
        LogUtils.w("mIndexer.get(position): " + mIndexer.get(position));
        LogUtils.w("mIndexer.keyAt(position): " + mIndexer.keyAt(position));
        LogUtils.w("mIndexer.valueAt(position): " + mIndexer.valueAt(position));
        LogUtils.w("mIndexer.indexOfKey(position): " + mIndexer.indexOfKey(position));
        LogUtils.w("mIndexer.indexOfValue(position): " + mIndexer.indexOfValue(position));
        int section = mIndexer.get(position);
        LogUtils.e("section: " + section);
        return section;
    }
}
