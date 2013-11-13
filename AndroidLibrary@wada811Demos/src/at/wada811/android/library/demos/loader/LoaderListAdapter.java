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
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import at.wada811.android.library.demos.R;
import at.wada811.utils.LogUtils;

public class LoaderListAdapter extends ArrayAdapter<LoaderListItem> {

    private Context        mContext;
    private LayoutInflater mLayoutInflater;
    private LoaderManager  mLoaderManager;
    private Handler        mHandler;

    private SparseIntArray mLoaderIds;

    static final String    KEY_POSITION  = "KEY_POSITION";
    static final String    KEY_IMAGE_URL = "KEY_IMAGE_URL";

    public LoaderListAdapter(Context context, LoaderManager loaderManager) {
        super(context, android.R.layout.simple_list_item_1);
        mContext = context;
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLoaderManager = loaderManager;
        mHandler = new Handler();
        mLoaderIds = new SparseIntArray();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        View view = convertView;
        if(view == null){
            view = mLayoutInflater.inflate(R.layout.fragment_loader_list_item, null);
            ImageView imageView = (ImageView)view.findViewById(R.id.image);
            TextView titleView = (TextView)view.findViewById(R.id.title);
            TextView urlView = (TextView)view.findViewById(R.id.url);
            holder = new ViewHolder();
            holder.imageView = imageView;
            holder.titleView = titleView;
            holder.urlView = urlView;
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        final ViewHolder viewHolder = holder;
        final LoaderListItem item = (LoaderListItem)getItem(position);
        Bitmap bitmap = LoaderListFragment.sImageCache.get(item.getThumbnailUrl());
        if(bitmap == null){
            holder.imageView.setImageBitmap(bitmap);
            Bundle args = new Bundle();
            args.putInt(KEY_POSITION, position);
            args.putString(KEY_IMAGE_URL, item.getThumbnailUrl());
            // LoaderListFragmentとLoaderIdが被るのでオフセットを設定する
            int offset = 100;
            mLoaderIds.put(position, position + offset);
            mLoaderManager.initLoader(mLoaderIds.get(position), args, new LoaderCallbacks<Bitmap>(){
                @Override
                public Loader<Bitmap> onCreateLoader(int id, Bundle args){
                    LogUtils.d("LoaderId: " + id);
                    return new AsyncImageDownloadLoader(mContext, args);
                }

                @Override
                public void onLoadFinished(Loader<Bitmap> loader, final Bitmap data){
                    LogUtils.d("LoaderId: " + loader.getId());
                    LogUtils.d("data: " + (data == null ? data : data.toString()));
                    mHandler.post(new Runnable(){
                        @Override
                        public void run(){
                            viewHolder.imageView.setImageBitmap(data);
                        }
                    });
                }

                @Override
                public void onLoaderReset(Loader<Bitmap> loader){
                    LogUtils.i();
                }
            });
        }
        holder.titleView.setText(item.getImageTitle());
        holder.urlView.setText(item.getImageUrl());
        return view;

    }

    static class ViewHolder {
        ImageView imageView;
        TextView  titleView;
        TextView  urlView;
    }

    public void destroyAllLoaders(){
        for(int i = 0; i < mLoaderIds.size(); i++){
            mLoaderManager.destroyLoader(mLoaderIds.get(i));
        }
    }

}
