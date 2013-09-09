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
package at.wada811.app.dialog;

import jp.co.noxi.app.NXProgressDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ProgressDialogFragment extends NXProgressDialog {

    public static ProgressDialogFragment newInstance(int style){
        ProgressDialogFragment f = new ProgressDialogFragment();
        f.mStyle = style;
        return f;
    }

    public static ProgressDialogFragment find(FragmentManager fm){
        return (ProgressDialogFragment)fm.findFragmentByTag(COMMIT_TAG);
    }

    @Override
    public void show(FragmentManager manager, String tag){
        super.show(manager, COMMIT_TAG);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag){
        return super.show(transaction, COMMIT_TAG);
    }

    public int getMax(){
        return mMax;
    }

    public int getProgress(){
        return mProgressVal;
    }

    public void incrementProgressBy(int diff){
        mProgressVal += diff;
        setProgress(mProgressVal);
    }

    public int getSecondaryProgress(){
        return mSecondaryProgressVal;
    }

    public void incrementSecondaryProgressBy(int diff){
        mSecondaryProgressVal += diff;
        setSecondaryProgress(mSecondaryProgressVal);
    }

    public boolean isIndeterminate(){
        return mIndeterminate;
    }
}
