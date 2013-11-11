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

public class LoaderListItem {

    private String mImageUrl;
    private String mThumbnailUrl;
    private String mImageTitle;
    private int    mImageWidth;
    private int    mImageHeight;
    private String mPageUrl;

    public LoaderListItem() {

    }

    public void setImageUrl(String imageUrl){
        mImageUrl = imageUrl;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl){
        mThumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailUrl(){
        return mThumbnailUrl;
    }

    public void setImageTitle(String imageTitle){
        mImageTitle = imageTitle;
    }

    public String getImageTitle(){
        return mImageTitle;
    }

    public void setImageWidth(int imageWidth){
        mImageWidth = imageWidth;
    }

    public int getImageWidth(){
        return mImageWidth;
    }

    public void setImageHeight(int imageHeight){
        mImageHeight = imageHeight;
    }

    public int getImageHeight(){
        return mImageHeight;
    }

    public void setPageUrl(String pageUrl){
        mPageUrl = pageUrl;
    }

    public String getPageUrl(){
        return mPageUrl;
    }
}
