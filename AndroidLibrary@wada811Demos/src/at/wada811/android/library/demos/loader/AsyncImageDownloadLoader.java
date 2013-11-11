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
import android.graphics.Bitmap;
import android.os.Bundle;
import at.wada811.loader.AbstractAsyncLoader;
import at.wada811.utils.BitmapUtils;
import at.wada811.utils.LogUtils;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

public class AsyncImageDownloadLoader extends AbstractAsyncLoader<Bitmap> {

    private Bundle mBundle;

    public AsyncImageDownloadLoader(Context context, Bundle bundle) {
        super(context);
        mBundle = bundle;
    }

    @Override
    public Bitmap loadInBackground(){
        int position = mBundle.getInt(LoaderListAdapter.KEY_POSITION);
        final String imageUrl = mBundle.getString(LoaderListAdapter.KEY_IMAGE_URL);
        LogUtils.d(position + ": " + imageUrl);

        SyncHttpClient httpClient = new SyncHttpClient();
        httpClient.setMaxRetriesAndTimeout(3, 5000);
        httpClient.get(imageUrl, new BinaryHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, byte[] binaryData){
                super.onSuccess(statusCode, binaryData);
                Bitmap bitmap = BitmapUtils.createBitmapFromByteArray(binaryData);
                LoaderListFragment.sImageCache.put(imageUrl, bitmap);
            }
        });
        Bitmap bitmap = LoaderListFragment.sImageCache.get(imageUrl);
        deliverResult(bitmap);
        return bitmap;
    }

}
