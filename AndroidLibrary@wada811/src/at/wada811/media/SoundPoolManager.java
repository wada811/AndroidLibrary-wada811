/*
 * Copyright 2014 wada811<at.wada811@gmail.com>
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
package at.wada811.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.util.SparseIntArray;

public class SoundPoolManager {

    private SoundPool mSoundPool;
    private Context mContext;
    SparseIntArray mLoadedSoundIds;
    private int mSoundIdToPlay;

    private static final int NUM_MEDIA_SOUND_STREAMS = 1;
    private static final int SOUND_NOT_LOADED = -1;

    public SoundPoolManager(Context context) {
        mContext = context;
        mSoundPool = new SoundPool(NUM_MEDIA_SOUND_STREAMS, AudioManager.STREAM_SYSTEM, 0);
        mSoundPool.setOnLoadCompleteListener(mLoadCompleteListener);
        mLoadedSoundIds = new SparseIntArray();
        mSoundIdToPlay = SOUND_NOT_LOADED;
    }

    /**
     * Preload a sound effect.
     * 
     * @param resId
     * @see #play(int)
     */
    public synchronized void load(int resId){
        if(mLoadedSoundIds.get(resId, SOUND_NOT_LOADED) == SOUND_NOT_LOADED){
            int loadedSoundId = mSoundPool.load(mContext, resId, 1);
            mLoadedSoundIds.put(resId, loadedSoundId);
        }
    }

    /**
     * Play a sound effect.
     * 
     * @param resId
     * @see #load(int)
     */
    public synchronized void play(int resId){
        int soundId = mLoadedSoundIds.get(resId, SOUND_NOT_LOADED);
        if(soundId == SOUND_NOT_LOADED){
            mSoundIdToPlay = mSoundPool.load(mContext, resId, 1);
            mLoadedSoundIds.put(resId, mSoundIdToPlay);
        }else{
            int play = mSoundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    private SoundPool.OnLoadCompleteListener mLoadCompleteListener = new SoundPool.OnLoadCompleteListener(){
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status){
            if(status == 0){
                if(mSoundIdToPlay == sampleId){
                    soundPool.play(sampleId, 1.0f, 1.0f, 0, 0, 1.0f);
                    mSoundIdToPlay = SOUND_NOT_LOADED;
                }
            }else{
                Log.e(SoundPoolManager.class.getSimpleName(), "Unable to load sound for playback (status: " + status + ")");
            }
        }
    };

    /**
     * Free up all audio resources used by this SoundPoolManager instance. Do
     * not call any other methods on a SoundPoolManager instance after calling
     * release().
     */
    public void release(){
        if(mLoadedSoundIds == null){
            mLoadedSoundIds.clear();
            mLoadedSoundIds = null;
        }
        if(mSoundPool != null){
            mSoundPool.release();
            mSoundPool = null;
        }
    }

}
