package com.example.admin.mysensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sm;
    private TextView xTextView,yTextView,zTextView;
    private TextView gxTextView,gyTextView,gzTextView;
    private TextView mxTextView,myTextView,mzTextView;
    private TextView rxTextView,ryTextView,rzTextView;

    private static final int MATRIX_SIZE = 16;
    float[] in = new float[MATRIX_SIZE];
    float[] out = new float[MATRIX_SIZE];
    float[] I = new float[MATRIX_SIZE];
    float[] o = new float[3];
    float[] a = new float[3];
    float[] m = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        xTextView = (TextView)findViewById(R.id.xTextView);
        yTextView = (TextView)findViewById(R.id.yTextView);
        zTextView = (TextView)findViewById(R.id.zTextView);
        gxTextView = (TextView)findViewById(R.id.gxTextView);
        gyTextView = (TextView)findViewById(R.id.gyTextView);
        gzTextView = (TextView)findViewById(R.id.gzTextView);
        mxTextView = (TextView)findViewById(R.id.mxTextView);
        myTextView = (TextView)findViewById(R.id.myTextView);
        mzTextView = (TextView)findViewById(R.id.mzTextView);
        rxTextView = (TextView)findViewById(R.id.rxTextView);
        ryTextView = (TextView)findViewById(R.id.ryTextView);
        rzTextView = (TextView)findViewById(R.id.rzTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //加速度
        Sensor Accele = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, Accele, SensorManager.SENSOR_DELAY_UI);
        //ジャイロ
        Sensor Gyro = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sm.registerListener(this, Gyro, SensorManager.SENSOR_DELAY_UI);
        //磁力
        Sensor Magnet = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sm.registerListener(this, Magnet, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sm != null) {
            sm.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        switch( event.sensor.getType() ) {
            case Sensor.TYPE_ACCELEROMETER: // 加速度
                xTextView.setText("加速度X = " + event.values[0]);
                yTextView.setText("加速度Y = " + event.values[1]);
                zTextView.setText("加速度Z = " + event.values[2]);
                a = event.values.clone();
                break;
            case Sensor.TYPE_GYROSCOPE: // ジャイロ
                gxTextView.setText("ジャイロX = " + event.values[0]);
                gyTextView.setText("ジャイロY = " + event.values[1]);
                gzTextView.setText("ジャイロZ = " + event.values[2]);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD: // 磁力
                mxTextView.setText("磁力X = " + event.values[0]);
                myTextView.setText("磁力Y = " + event.values[1]);
                mzTextView.setText("磁力Z = " + event.values[2]);
                m = event.values.clone();
                break;
        }
        if (a != null && m != null) {
            SensorManager.getRotationMatrix(in, I, a, m);
            SensorManager.remapCoordinateSystem(in, SensorManager.AXIS_X, SensorManager.AXIS_Z, out);
            SensorManager.getOrientation(out, o);

            rxTextView.setText(String.valueOf(("roll = "+ (o[2] * 180 / 3.1415 ))));
            ryTextView.setText(String.valueOf(("pitch = " + (o[1] * 180 / 3.1415 ))));
            rzTextView.setText(String.valueOf(("yaw = " + (o[0] * 180 / 3.1415 ))));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
