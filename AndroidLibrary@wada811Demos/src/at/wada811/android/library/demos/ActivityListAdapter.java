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

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ActivityListAdapter extends ArrayAdapter<ActivityInfo> {

    private LayoutInflater mLayoutInflater;

    public ActivityListAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view;
        if(convertView == null){
            view = mLayoutInflater.inflate(R.layout.list_single_text, parent, false);
        }else{
            view = convertView;
        }
        ActivityInfo item = getItem(position);
        TextView activityNameView = (TextView)view.findViewById(R.id.text);
        String activityName = item.name.replace(item.packageName + ".", "");
        activityNameView.setText(activityName);
        return view;
    }
}
