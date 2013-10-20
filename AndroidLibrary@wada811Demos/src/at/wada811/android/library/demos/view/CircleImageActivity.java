package at.wada811.android.library.demos.view;

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
import at.wada811.view.CircleImageView;

public class CircleImageActivity extends FragmentActivity {

    final CircleImageActivity self   = this;
    private int               mIndex = 0;
    private int               mCount = 14;
    private CircleImageView   mCircleImageView;
    private TextView          mLayoutParamText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_image);

        Button button = (Button)findViewById(R.id.button);
        mLayoutParamText = (TextView)findViewById(R.id.text);
        final LinearLayout container = (LinearLayout)findViewById(R.id.container);
        addView(container, mIndex++);
        button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                LogUtils.d("mIndex: " + mIndex);
                addView(container, mIndex);
                mIndex = ++mIndex % mCount;
            }
        });
    }

    private void addView(LinearLayout container, int index){
        if(mCircleImageView != null){
            container.removeView(mCircleImageView);
            mCircleImageView = null;
        }
        mCircleImageView = new CircleImageView(self);
        mCircleImageView.setBackgroundColor(ColorUtils.getColor(self, R.color.blue_light));
        mCircleImageView.setImageResource(R.drawable.lena);
        container.addView(mCircleImageView, getLayoutParams(index));
    }

    private LayoutParams getLayoutParams(int index){
        LayoutParams layoutParams = null;
        switch(index){
            case 0:{
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                mLayoutParamText.setText("width: MATCH_PARENT, height: MATCH_PARENT");
                break;
            }
            case 1:{
                layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mLayoutParamText.setText("width: WRAP_CONTENT, height: WRAP_CONTENT");
                break;
            }
            case 2:{
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                mCircleImageView.setRadius(100);
                mLayoutParamText.setText("width: MATCH_PARENT, height: MATCH_PARENT, Radius: 100");
                break;
            }
            case 3:{
                layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mCircleImageView.setRadius(100);
                mLayoutParamText.setText("width: WRAP_CONTENT, height: WRAP_CONTENT, Radius: 100");
                break;
            }
            case 4:{
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                mCircleImageView.setRadius(555);
                mLayoutParamText.setText("width: MATCH_PARENT, height: MATCH_PARENT, Radius: 555");
                break;
            }
            case 5:{
                layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mCircleImageView.setRadius(555);
                mLayoutParamText.setText("width: WRAP_CONTENT, height: WRAP_CONTENT, Radius: 555");
                break;
            }
            case 6:{
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                mCircleImageView.setRadius(1000);
                mLayoutParamText.setText("width: MATCH_PARENT, height: MATCH_PARENT, Radius: 1000");
                break;
            }
            case 7:{
                layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mCircleImageView.setRadius(1000);
                mLayoutParamText.setText("width: WRAP_CONTENT, height: WRAP_CONTENT, Radius: 1000");
                break;
            }
            case 8:{
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                mCircleImageView.setCenterX(100);
                mCircleImageView.setCenterY(200);
                mCircleImageView.setRadius(300);
                mLayoutParamText.setText("width: MATCH_PARENT, height: MATCH_PARENT, Radius: 300, X: 100, Y: 200");
                break;
            }
            case 9:{
                layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mCircleImageView.setCenterX(100);
                mCircleImageView.setCenterY(200);
                mCircleImageView.setRadius(300);
                mLayoutParamText.setText("width: WRAP_CONTENT, height: WRAP_CONTENT, Radius: 300, X: 100, Y: 200");
                break;
            }
            case 10:{
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                mCircleImageView.setCenterX(-200);
                mCircleImageView.setCenterY(-300);
                mCircleImageView.setRadius(400);
                mLayoutParamText.setText("width: MATCH_PARENT, height: MATCH_PARENT, Radius: 400, X: -200, Y: -300");
                break;
            }
            case 11:{
                layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mCircleImageView.setCenterX(-200);
                mCircleImageView.setCenterY(-300);
                mCircleImageView.setRadius(400);
                mLayoutParamText.setText("width: WRAP_CONTENT, height: WRAP_CONTENT, Radius: 400, X: -200, Y: -300");
                break;
            }
            case 12:{
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                mCircleImageView.setCenterX(300);
                mCircleImageView.setCenterY(400);
                mLayoutParamText.setText("width: MATCH_PARENT, height: MATCH_PARENT, X: 300, Y: 400");
                break;
            }
            case 13:{
                layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mCircleImageView.setCenterX(300);
                mCircleImageView.setCenterY(400);
                mLayoutParamText.setText("width: WRAP_CONTENT, height: WRAP_CONTENT, X: 300, Y: 400");
                break;
            }
            default:
                break;
        }
        return layoutParams;
    }
}
