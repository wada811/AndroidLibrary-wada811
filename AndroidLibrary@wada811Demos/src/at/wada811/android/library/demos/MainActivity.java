package at.wada811.android.library.demos;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import at.wada811.app.dialog.ProgressDialogFragment;

public class MainActivity extends FragmentActivity {

    private int                    mProgress;
    private ProgressDialogFragment mProgressDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressDialogFragment = newProgressDialogFragment();
        mProgress = mProgressDialogFragment.getProgress();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                if(mProgressDialogFragment.getMax() == mProgressDialogFragment.getProgress()){
                    timer.cancel();
                    mProgressDialogFragment.dismiss();
                }else if(mProgressDialogFragment.getMax() == mProgressDialogFragment.getSecondaryProgress()){
                    mProgressDialogFragment.incrementProgressBy(1);
                }else if(!mProgressDialogFragment.isIndeterminate()){
                    mProgressDialogFragment.incrementSecondaryProgressBy(1);
                }else if(mProgressDialogFragment.getMax() == mProgress){
                    mProgressDialogFragment.setIndeterminate(false);
                }else{
                    mProgress++;
                }
            }
        }, 0, 1000);
    }

    private ProgressDialogFragment newProgressDialogFragment(){
        ProgressDialogFragment progressDialogFragment = ProgressDialogFragment.find(getSupportFragmentManager());
        if(progressDialogFragment == null){
            progressDialogFragment = ProgressDialogFragment.newInstance(ProgressDialogFragment.STYLE_HORIZONTAL);
            progressDialogFragment.setIndeterminate(true);
            progressDialogFragment.setProgress(0);
            progressDialogFragment.setSecondaryProgress(0);
            progressDialogFragment.setMax(5);
            progressDialogFragment.show(getSupportFragmentManager());
        }
        return progressDialogFragment;
    }
}
