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

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.wada811.android.library.demos.R;

public class TabViewPagerAdapter extends PagerAdapter {

    private LayoutInflater mInflater;

    public TabViewPagerAdapter(Context context) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View instantiateItem(ViewGroup container, int position){
        View page = mInflater.inflate(R.layout.layout_page, container, false);
        TextView textView = (TextView)page.findViewById(R.id.text);
        textView.setText("Tab " + position);
        container.addView(page);
        return page;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View)object);
    }

    @Override
    public int getCount(){
        return 4;
    }

    @Override
    public float getPageWidth(int position){
        return 1f;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return super.getPageTitle(position);
    }

    @Override
    public int getItemPosition(Object object){
        return super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return view == object;
    }

}
