package com.example.final_project.utils;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class SensorService extends Service {

    private static final long MIN_ALERT_DELAY = 3000;
    private long lastTimeStamp = 0;

    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    @Override
    public void onCreate() {
        Log.d("check2", "onCreate Thread: " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("check2", "onStartCommand Thread: " + Thread.currentThread().getName());
        MySignal.getInstance().toast("service starting");

        startSample();

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    private void startSample() {
        Log.d("check2", "startSample Thread: " + Thread.currentThread().getName());

        SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor mySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mySensorManager.registerListener(
                sensorEventListener,
                mySensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            mGravity = event.values.clone();
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt(x*x + y*y + z*z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            createBroadcastReceiver(mAccel > 3);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    };

    private void createBroadcastReceiver(boolean val) {
        //Intent intent = new Intent(Constants.ACT_SEN);
        //intent.putExtra(Constants.EXTRA_ACCELEROMETER, val);
        //sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("check2", "onDestroy");
        MySignal.getInstance().toast("service done");
    }

    private void doLongProcess() {
        Log.d("check2", "doLongProcess Thread: " + Thread.currentThread().getName());
        Log.d("check2", "A");
        int x = 0;
        int y = 0;
        for (int i = 0; i < 100000; i++) {
            y = 0;
            for (int j = 0; j < 10000; j++) {
                x = 2;
                y = x + j;
            }
        }
        Log.d("check2", "B");
        stopSelf();
    }
}