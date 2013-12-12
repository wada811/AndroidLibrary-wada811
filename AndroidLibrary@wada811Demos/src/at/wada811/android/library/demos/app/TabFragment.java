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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.wada811.android.library.demos.R;
import at.wada811.utils.DisplayUtils;

public class TabFragment extends Fragment {

    public static final String TAG = TabFragment.class.getSimpleName();
    private TabFragmentCallback mCallback;

    public static interface TabFragmentCallbackProvider {

        public TabFragmentCallback getTabCallback();

    }

    public static interface TabFragmentCallback {

        public void onAttach(TabFragment tabFragment);

        public void onDetach(TabFragment tabFragment);

    }

    public static TabFragment newInstance(){
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        if(activity instanceof TabFragmentCallbackProvider == false){
            throw new ClassCastException(activity.getLocalClassName() + " must implements " + TabFragmentCallbackProvider.class.getSimpleName());
        }
        TabFragmentCallbackProvider provider = (TabFragmentCallbackProvider)activity;
        mCallback = provider.getTabCallback();
        mCallback.onAttach(this);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallback.onDetach(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_page, container, false);
        TextView textView = (TextView)view.findViewById(R.id.text);
        textView.setText(getTag());
        textView.setTextSize(Math.min(DisplayUtils.getWidth(getActivity()) / 16, DisplayUtils.getHeight(getActivity()) / 16));
        return view;
    }

}
