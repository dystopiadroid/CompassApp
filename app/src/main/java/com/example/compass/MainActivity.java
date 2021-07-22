package com.example.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    Sensor acc;
    Sensor mag;
    SensorManager sen;
    float[] acc_Read;
    float[] mag_Read;
    float azi;
    TextView angle;
    ImageView compass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sen = (SensorManager) getSystemService(SENSOR_SERVICE);
        acc = sen.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag = sen.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
        @Override
        protected void onResume(){
        super.onResume();
        sen.registerListener(this,acc,sen.SENSOR_DELAY_UI);
        sen.registerListener(this,mag,sen.SENSOR_DELAY_UI);
        }
        protected void onPause(){
        super.onPause();
        sen.unregisterListener(this,acc);
        sen.unregisterListener(this,mag);
        }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == acc.TYPE_ACCELEROMETER){
           acc_Read = event.values;
        }
        if(event.sensor.getType() == mag.TYPE_MAGNETIC_FIELD){
           mag_Read = event.values;
        }
        if(acc_Read != null && mag_Read != null){
            float[] r = new float[9];
            float[] i = new float[9];
            boolean success;
            success = sen.getRotationMatrix(r,i,acc_Read,mag_Read);
            if(success){
                float in_angle = 0f;
                angle = findViewById(R.id.Angle);
                compass = findViewById(R.id.compass);
                float[] orient = new float[3];
                sen.getOrientation(r,orient);
                azi = orient[0];
                float degree = (azi * 180)/3.14f;
                int idegree = Math.round(degree);
                if(idegree < 0){
                    idegree = 360 + idegree;
                }
                angle.setText(Integer.toString(idegree) + (char) 0x00B0 + " to absolute north");
                ObjectAnimator rot = ObjectAnimator.ofFloat(compass,"rotation",in_angle,-idegree);
                rot.setDuration(0);
                rot.start();
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
