package at.wada811.android.library.demos.fragment;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import at.wada811.android.library.demos.R;
import at.wada811.app.dialog.ProgressDialogFragment;
import at.wada811.utils.LogUtils;

public class ProgressDialogFragmentActivity extends FragmentActivity {

    private int                    mProgress;
    private ProgressDialogFragment mProgressDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_dialog);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                mProgressDialogFragment = showProgressDialogFragment();
                mProgress = mProgressDialogFragment.getProgress();
                final Timer timer = new Timer();
                timer.schedule(new TimerTask(){
                    @Override
                    public void run(){
                        LogUtils.d("Progress: " + mProgressDialogFragment.getProgress());
                        LogUtils.d("SecondaryProgress: " + mProgressDialogFragment.getSecondaryProgress());
                        LogUtils.d("mProgress: " + mProgress);
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
        });
    }

    private ProgressDialogFragment showProgressDialogFragment(){
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
