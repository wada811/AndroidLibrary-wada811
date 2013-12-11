package at.wada811.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import java.util.ArrayList;

public class SensorFragment extends Fragment implements SensorEventListener {

    public static final String TAG = SensorFragment.class.getSimpleName();
    private static final String KEY_SENSOR_TYPES = "KEY_SENSOR_TYPES";
    private SensorCallback mCallback;
    private SensorManager mSensorManager;
    private float x, y, z = 0;

    public static interface SensorCallbackProvider {
        public SensorCallback getSensorCallback();
    }

    public static interface SensorCallback {

        public void onLinearAccelerationSensorChanged(float x, float y, float z);

    }

    public static SensorFragment newInstance(ArrayList<Integer> sensorTypes){
        SensorFragment fragment = new SensorFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(KEY_SENSOR_TYPES, sensorTypes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        if(activity instanceof SensorCallbackProvider == false){
            throw new ClassCastException(activity.getLocalClassName() + " must implements " + SensorCallbackProvider.class.getSimpleName());
        }
        SensorCallbackProvider picker = (SensorCallbackProvider)activity;
        mCallback = picker.getSensorCallback();
        mSensorManager = (SensorManager)activity.getSystemService(Context.SENSOR_SERVICE);
        registerListener();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        unregisterListener();
    }

    private void registerListener(){
        if(mSensorManager != null){
            ArrayList<Integer> sensorTypes = getArguments().getIntegerArrayList(KEY_SENSOR_TYPES);
            for(int sensorType : sensorTypes){
                Sensor sensor = mSensorManager.getDefaultSensor(sensorType);
                mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    public void registerListener(Integer sensorType){
        ArrayList<Integer> sensorTypes = getArguments().getIntegerArrayList(KEY_SENSOR_TYPES);
        sensorTypes.add(sensorType);
        getArguments().putIntegerArrayList(KEY_SENSOR_TYPES, sensorTypes);
        if(mSensorManager != null){
            Sensor sensor = mSensorManager.getDefaultSensor(sensorType);
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void unregisterListener(){
        if(mSensorManager != null){
            ArrayList<Integer> sensorTypes = getArguments().getIntegerArrayList(KEY_SENSOR_TYPES);
            for(int sensorType : sensorTypes){
                Sensor sensor = mSensorManager.getDefaultSensor(sensorType);
                mSensorManager.unregisterListener(this, sensor);
            }
        }
    }

    public void unregisterListener(Integer sensorType){
        ArrayList<Integer> sensorTypes = getArguments().getIntegerArrayList(KEY_SENSOR_TYPES);
        sensorTypes.remove(sensorType);
        getArguments().putIntegerArrayList(KEY_SENSOR_TYPES, sensorTypes);
        if(mSensorManager != null){
            Sensor sensor = mSensorManager.getDefaultSensor(sensorType);
            mSensorManager.unregisterListener(this, sensor);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            x = (float)(x * 0.9 + event.values[0] * 0.1);
            y = (float)(y * 0.9 + event.values[1] * 0.1);
            z = (float)(z * 0.9 + event.values[2] * 0.1);
            mCallback.onLinearAccelerationSensorChanged(x, y, z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
        // do nothing
    }

}
