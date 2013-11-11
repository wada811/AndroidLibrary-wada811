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

import android.content.Context;
import android.os.Bundle;
import at.wada811.loader.AbstractAsyncLoader;
import at.wada811.utils.LogUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

public class AsyncImageSearchLoader extends AbstractAsyncLoader<Void> {

    private Bundle                  mBundle;
    private JsonHttpResponseHandler mHandler;

    public AsyncImageSearchLoader(Context context, Bundle bundle, JsonHttpResponseHandler handler) {
        super(context);
        mBundle = bundle;
        mHandler = handler;
    }

    @Override
    public Void loadInBackground(){
        int startIndex = mBundle.getInt(LoaderListFragment.KEY_START_INDEX);
        final String searchUrl = mBundle.getString(LoaderListFragment.KEY_SEARCH_URL);
        LogUtils.d(startIndex + ": " + searchUrl);

        SyncHttpClient httpClient = new SyncHttpClient();
        httpClient.setMaxRetriesAndTimeout(3, 5000);
        httpClient.get(searchUrl, mHandler);
        deliverResult(null);
        return null;
    }

}
