package com.sah.seewaves;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

class MainAct extends AppCompatActivity implements SensorEventListener {

    private static String TAG = "MainAct";

    private SensorManager mSensorManager = null;
    private Sensor mAccelerometer = null;
    private long lastUpdate = 0;
    private Float last_x = 0.0f;
    private Float last_y = 0.0f;
    private Float last_z = 0.0f;
    private static int SHAKE_THRESHOLD = 10000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if (curTime - lastUpdate > 100) {
                long diffTime = curTime - lastUpdate;
                lastUpdate = curTime;

                float x = event.values[SensorManager.DATA_X];
                float y = event.values[SensorManager.DATA_Y];
                float z = event.values[SensorManager.DATA_Z];

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Log.d("sensor", "shake detected w/ speed: " + speed);
                    Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("accuracy", "nothing");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
