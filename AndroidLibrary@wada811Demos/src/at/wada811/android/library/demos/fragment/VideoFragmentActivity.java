package at.wada811.android.library.demos.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import at.wada811.android.library.demos.R;
import at.wada811.android.library.demos.fragment.VideoFragment.VideoCallback;
import at.wada811.android.library.demos.fragment.VideoFragment.VideoCallbackPicker;
import at.wada811.utils.LogUtils;

public class VideoFragmentActivity extends FragmentActivity implements VideoCallbackPicker {

    private VideoFragment mVideoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                if(getSupportFragmentManager().findFragmentByTag(VideoFragment.TAG) == null){
                    LogUtils.d();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.fragment, mVideoFragment, VideoFragment.TAG);
                    transaction.commit();
                }else{
                    LogUtils.d();
                }
            }
        });

        mVideoFragment = VideoFragment.newInstance();
        Bundle args = new Bundle();
        args.putInt(VideoFragment.KEY_RES_ID, R.raw.video);
//        String filePath = "/storage/emulated/0/DevCamera/2013-09-20_21-25_01.mp4";
//        args.putString(VideoFragment.KEY_FILE_PATH, filePath);
        mVideoFragment.setArguments(args);
    }

    @Override
    public VideoCallback getVideoCallback(){
        return new VideoCallback(){
            @Override
            public void onActivityCreated(){
                LogUtils.d();
//                String filePath = "/storage/emulated/0/DevCamera/2013-09-20_21-25_01.mp4";
                try{
//                    mVideoFragment.setDateSource(filePath);
                    mVideoFragment.setVideoAutoPlay(true);
                    mVideoFragment.setDisplayWidth(640);
                    mVideoFragment.setDisplayHeight(480);
//                    mVideoFragment.prepare();
                }catch(Exception e){
                    e.printStackTrace();
                    LogUtils.e(e);
                }
            }

            @Override
            public void onPrepared(){
                LogUtils.d();
            }

            @Override
            public void onCompletion(){
                LogUtils.d();
                if(getSupportFragmentManager().findFragmentByTag(VideoFragment.TAG) != null){
                    LogUtils.d();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.remove(mVideoFragment);
                    transaction.commit();
                }
            }
        };
    }
}
