package at.wada811.android.library.demos.widget;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import at.wada811.android.library.demos.R;
import at.wada811.utils.ColorUtils;
import at.wada811.utils.LogUtils;
import at.wada811.widget.CircleImageView;

public class CircleImageActivity extends FragmentActivity {

    final CircleImageActivity self = this;
    private int mIndex = 0;
    private int mCount = 14;
    private CircleImageView mCircleImageView;
    private TextView mLayoutParamText;
    private LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_image);

        Button button = (Button)findViewById(R.id.button);
        mLayoutParamText = (TextView)findViewById(R.id.text);
        mContainer = (LinearLayout)findViewById(R.id.container);
        mCircleImageView = (CircleImageView)findViewById(R.id.circle);
        button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                LogUtils.d("mIndex: " + mIndex);
                addView(mIndex);
                mIndex = ++mIndex % mCount;
            }
        });
    }

    private void addView(int index){
        if(mCircleImageView != null){
            mContainer.removeView(mCircleImageView);
            mCircleImageView = null;
        }
        mCircleImageView = new CircleImageView(this);
        mCircleImageView.setBackgroundColor(ColorUtils.getColor(this, R.color.blue_light));
        mCircleImageView.setImageResource(R.drawable.lena);
        mContainer.addView(mCircleImageView, getLayoutParams(index));
    }

    private LayoutParams getLayoutParams(int index){
        LayoutParams layoutParams = null;
        switch(index){
            case 0:{
                layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
                mLayoutParamText.setText("w: MATCH_PARENT, h: MATCH_PARENT");
                break;
            }
            case 1:{
                layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                mLayoutParamText.setText("w: WRAP_CONTENT, h: WRAP_CONTENT");
                break;
            }
            case 2:{
                layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
                mCircleImageView.setRadius(100);
                mLayoutParamText.setText("w: MATCH_PARENT, h: MATCH_PARENT, R: 100");
                break;
            }
            case 3:{
                layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                mCircleImageView.setRadius(100);
                mLayoutParamText.setText("w: WRAP_CONTENT, h: WRAP_CONTENT, R: 100");
                break;
            }
            case 4:{
                layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
                mCircleImageView.setRadius(300);
                mLayoutParamText.setText("w: MATCH_PARENT, h: MATCH_PARENT, R: 300");
                break;
            }
            case 5:{
                layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                mCircleImageView.setRadius(300);
                mLayoutParamText.setText("w: WRAP_CONTENT, h: WRAP_CONTENT, R: 300");
                break;
            }
            case 6:{
                layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
                mCircleImageView.setRadius(1000);
                mLayoutParamText.setText("w: MATCH_PARENT, h: MATCH_PARENT, R: 1000");
                break;
            }
            case 7:{
                layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                mCircleImageView.setRadius(1000);
                mLayoutParamText.setText("w: WRAP_CONTENT, h: WRAP_CONTENT, R: 1000");
                break;
            }
            case 8:{
                layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
                mCircleImageView.setCenterX(100);
                mCircleImageView.setCenterY(200);
                mCircleImageView.setRadius(300);
                mLayoutParamText.setText("w: MATCH_PARENT, h: MATCH_PARENT, R: 300, X: 100, Y: 200");
                break;
            }
            case 9:{
                layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                mCircleImageView.setCenterX(100);
                mCircleImageView.setCenterY(200);
                mCircleImageView.setRadius(300);
                mLayoutParamText.setText("w: WRAP_CONTENT, h: WRAP_CONTENT, R: 300, X: 100, Y: 200");
                break;
            }
            case 10:{
                layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
                mCircleImageView.setCenterX(-200);
                mCircleImageView.setCenterY(-300);
                mCircleImageView.setRadius(400);
                mLayoutParamText.setText("w: MATCH_PARENT, h: MATCH_PARENT, R: 400, X: -200, Y: -300");
                break;
            }
            case 11:{
                layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                mCircleImageView.setCenterX(-200);
                mCircleImageView.setCenterY(-300);
                mCircleImageView.setRadius(400);
                mLayoutParamText.setText("w: WRAP_CONTENT, h: WRAP_CONTENT, R: 400, X: -200, Y: -300");
                break;
            }
            case 12:{
                layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
                mCircleImageView.setCenterX(200);
                mCircleImageView.setCenterY(300);
                mLayoutParamText.setText("w: MATCH_PARENT, h: MATCH_PARENT, X: 200, Y: 300");
                break;
            }
            case 13:{
                layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                mCircleImageView.setCenterX(200);
                mCircleImageView.setCenterY(300);
                mLayoutParamText.setText("w: WRAP_CONTENT, h: WRAP_CONTENT, X: 200, Y: 300");
                break;
            }
            default:
                break;
        }
        return layoutParams;
    }
}
