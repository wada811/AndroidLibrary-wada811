package at.wada811.android.library.demos.fragment;

import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import at.wada811.android.library.demos.R;
import at.wada811.app.fragment.SensorFragment;
import at.wada811.app.fragment.SensorFragment.SensorCallback;
import at.wada811.app.fragment.SensorFragment.SensorCallbackProvider;
import at.wada811.utils.AndroidUtils;
import at.wada811.utils.LogUtils;
import java.util.ArrayList;

/**
 * [Android]端末が振られたことを加速度センサーから検出する | DevAchieve
 * http://wada811.blogspot.com/2013/12/android-shake-detection.html
 */
public class SensorActivity extends FragmentActivity implements SensorCallbackProvider {

    final SensorActivity self = this;

    private static final int SPEED_THRESHOLD = 25;
    private static final int SHAKE_TIMEOUT = 500;
    private static final int SHAKE_DURATION = 1000;
    private static final int SHAKE_COUNT = 3;
    private int mShakeCount = 0;
    private long mLastTime = 0;
    private long mLastAccel = 0;
    private long mLastShake = 0;
    private float mLastX, mLastY, mLastZ = 0;

    private TextView mText1;
    private TextView mText2;
    private TextView mText3;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        ArrayList<Integer> sensorTypes = new ArrayList<Integer>();
        sensorTypes.add(Sensor.TYPE_LINEAR_ACCELERATION);
        SensorFragment sensorFragment = SensorFragment.newInstance(sensorTypes);
        getSupportFragmentManager().beginTransaction().add(sensorFragment, SensorFragment.TAG).commit();

        mText1 = (TextView)findViewById(R.id.text1);
        mText2 = (TextView)findViewById(R.id.text2);
        mText3 = (TextView)findViewById(R.id.text3);
    }

    @Override
    public SensorCallback getSensorCallback(){
        return new SensorCallback(){
            @Override
            public void onLinearAccelerationSensorChanged(float x, float y, float z){
                LogUtils.v("x: " + String.format("%+01.2f", x) + ", y: " + String.format("%+01.2f", y) + ", z: " + String.format("%+01.2f", z));
                int iX = (int)(x * 1000) / 100;
                int iY = (int)(y * 1000) / 100;
                int iZ = (int)(z * 1000) / 100;
                int dX = iX - (int)(mLastX * 1000) / 100;
                int dY = iY - (int)(mLastY * 1000) / 100;
                int dZ = iZ - (int)(mLastZ * 1000) / 100;
                mText1.setText("x: " + iX + "(" + dX + ")");
                mText2.setText("y: " + iY + "(" + dY + ")");
                mText3.setText("z: " + iZ + "(" + dZ + ")");
                boolean isShaked = detectShake(x, y, z);
                if(isShaked){
                    AndroidUtils.showToast(self, "shaked!");
                }
            }
        };
    }

    public boolean detectShake(float x, float y, float z){
        boolean isShaked = false;
        // シェイク時間のチェック
        long now = System.currentTimeMillis();
        if(mLastTime == 0){
            mLastTime = now;
        }
        // SHAKE_TIMEOUT までに次の加速を検知しなかったら mShakeCount をリセット
        if(now - mLastAccel > SHAKE_TIMEOUT){
            mShakeCount = 0;
        }
        // 速度を算出する
        long diff = now - mLastTime;
        float speed = Math.abs(x + y + z - mLastX - mLastY - mLastZ) / diff * 10000;
        LogUtils.d("speed: " + speed);
        if(speed > SPEED_THRESHOLD){
            // mShakeCount の加算、SHAKE_COUNT を超えているかのチェック
            // 最後のシェイク時間から SHAKE_DURATION 経過しているかチェック
            if(++mShakeCount >= SHAKE_COUNT && now - mLastShake > SHAKE_DURATION){
                mLastShake = now;
                mShakeCount = 0;
                isShaked = true;
            }
            // SPEED_THRESHOLD を超える速度を検出した時刻をセット
            mLastAccel = now;
        }
        mLastTime = now;
        mLastX = x;
        mLastY = y;
        mLastZ = z;
        return isShaked;
    }

}
