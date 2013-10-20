package at.wada811.android.library.demos.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.wada811.android.library.demos.R;
import at.wada811.view.Tooltip;

public class TooltipActivity extends FragmentActivity {

    private Tooltip  mTooltip;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tooltip);

        mTextView = new TextView(this);
        mTextView.setBackgroundColor(Color.GREEN);
        mTooltip = new Tooltip(this);
        mTooltip.setContentView(mTextView);
        mTooltip.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mTooltip.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mTooltip.setAnimationStyle(R.style.tooltipAnimation);
        mTooltip.setDuration(3000);
//        mTooltip.setClippingEnabled(false);

        LinearLayout parent = (LinearLayout)findViewById(R.id.parent);
        parent.setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event){
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(mTooltip.isShowing()){
                            mTooltip.dismissImmediately();
                        }
                        mTextView.setText("x: " + event.getX() + ", y: " + event.getY());
                        mTooltip.showAtLocation(Gravity.NO_GRAVITY, (int)event.getX(), (int)event.getY());
                        break;
                }
                return false;
            }
        });
    }
}
