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

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import at.wada811.utils.LogUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoaderListFragment extends ListFragment implements OnScrollListener {

    static LruCache<String, Bitmap> sImageCache;
    private LoaderListAdapter       mLoaderListAdapter;
    private String                  mKeyword;

    static final String             KEY_START_INDEX = "KEY_START_INDEX";
    static final String             KEY_SEARCH_URL  = "KEY_SEARCH_URL";

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setEmptyText("キーワードを入力して下さい。");
        mLoaderListAdapter = new LoaderListAdapter(getActivity(), getLoaderManager());
        setListAdapter(mLoaderListAdapter);
        getListView().setOnScrollListener(this);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mKeyword = null;
        if(sImageCache != null){
            sImageCache.evictAll();
            sImageCache = null;
        }
        if(mLoaderListAdapter != null){
            mLoaderListAdapter.clear();
            mLoaderListAdapter.destroyAllLoaders();
            mLoaderListAdapter = null;
        }
    }

    public void init(){
        mKeyword = null;
        if(sImageCache != null){
            sImageCache.evictAll();
            sImageCache = null;
        }
        sImageCache = new LruCache<String, Bitmap>(10);
        if(mLoaderListAdapter != null){
            mLoaderListAdapter.clear();
            mLoaderListAdapter.destroyAllLoaders();
        }
        setListShown(false);
    }

    /**
     * キーワードで画像を検索する
     * 
     * @param keyword キーワード
     */
    public void searchImageWithKeyword(String keyword){
        if(keyword == null || keyword.equals("")){
            return;
        }
        mKeyword = keyword;
        String url = createSearchUrl(keyword);
        searchImageWithUrl(url);
    }

    /**
     * 検索URLを生成する
     * 
     * @param keyword 検索キーワード
     * @return url 検索URL
     */
    private String createSearchUrl(String keyword){
        String url = "https://ajax.googleapis.com/ajax/services/search/images";
        RequestParams params = new RequestParams();
        params.put("v", "1.0");
        params.put("q", keyword);
        params.put("rsz", "8");
        params.put("start", String.valueOf(mLoaderListAdapter.getCount()));
        return AsyncHttpClient.getUrlWithQueryString(true, url, params);
    }

    /**
     * URLで画像を検索する
     * 
     * @param url 画像検索URL
     */
    private void searchImageWithUrl(String url){
        LogUtils.i(url);
        Loader<Object> currentLoader = getLoaderManager().getLoader(mLoaderListAdapter.getCount());
        if(currentLoader != null && currentLoader.isStarted()){
            // 実行中はキャンセル
            LogUtils.i("canceled!");
            return;
        }
        Bundle args = new Bundle();
        args.putInt(LoaderListFragment.KEY_START_INDEX, mLoaderListAdapter.getCount());
        args.putString(LoaderListFragment.KEY_SEARCH_URL, url);
        getLoaderManager().restartLoader(mLoaderListAdapter.getCount(), args, new LoaderCallbacks<Void>(){
            @Override
            public Loader<Void> onCreateLoader(int id, Bundle args){
                LogUtils.i("LoaderId: " + id);
                return new AsyncImageSearchLoader(getActivity(), args, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, JSONObject response){
                        super.onSuccess(statusCode, response);
                        LogUtils.d("statusCode: " + statusCode);
                        try{
                            int responseStatus = response.getInt("responseStatus");
                            LogUtils.d("responseStatus: " + responseStatus);
                            if(responseStatus != 200){
                                return;
                            }
                            JSONObject responseData = response.getJSONObject("responseData");
                            JSONArray results = responseData.getJSONArray("results");
                            ArrayList<LoaderListItem> resultsList = new ArrayList<LoaderListItem>(results.length());
                            for(int i = 0; i < results.length(); i++){
                                JSONObject result = results.getJSONObject(i);
                                LoaderListItem item = new LoaderListItem();
                                item.setImageUrl(result.getString("unescapedUrl"));
                                item.setThumbnailUrl(result.getString("tbUrl"));
                                item.setImageTitle(result.getString("titleNoFormatting"));
                                item.setImageWidth(Integer.valueOf(result.getString("width")));
                                item.setImageHeight(Integer.valueOf(result.getString("height")));
                                resultsList.add(item);
                            }
                            if(mLoaderListAdapter.getCount() == 0){
                                setListShown(true);
                            }
                            if(resultsList.size() > 0){
                                mLoaderListAdapter.addAll(resultsList);
                                getListView().invalidateViews();
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                            LogUtils.e(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable e, JSONObject errorResponse){
                        super.onFailure(statusCode, e, errorResponse);
                        LogUtils.e("statusCode: " + statusCode);
                        LogUtils.e(e);
                    }
                });
            }

            @Override
            public void onLoadFinished(Loader<Void> loader, Void data){
                LogUtils.i("LoaderId: " + loader.getId());
            }

            @Override
            public void onLoaderReset(Loader<Void> loader){
                LogUtils.i();
            }
        });
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){
        super.onListItemClick(listView, view, position, id);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState){
        switch(scrollState){
            case OnScrollListener.SCROLL_STATE_FLING:
                LogUtils.v("OnScrollListener.SCROLL_STATE_FLING");
                break;
            case OnScrollListener.SCROLL_STATE_IDLE:
                LogUtils.v("OnScrollListener.SCROLL_STATE_IDLE");
                break;
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                LogUtils.v("OnScrollListener.SCROLL_STATE_TOUCH_SCROLL");
                break;
            default:
                LogUtils.v("scrollState: " + scrollState);
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        LogUtils.v("firstVisibleItem: " + firstVisibleItem);
        LogUtils.v("visibleItemCount: " + visibleItemCount);
        LogUtils.v("totalItemCount: " + totalItemCount);
        if(totalItemCount == firstVisibleItem + visibleItemCount){
            // 一番下まで見えている
            if(firstVisibleItem == 0){
                // 初期状態で一番下まで見えている
            }
            searchImageWithKeyword(mKeyword);
        }
    }

}
