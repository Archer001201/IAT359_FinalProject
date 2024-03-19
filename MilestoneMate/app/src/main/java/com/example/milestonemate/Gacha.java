package com.example.milestonemate;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Gacha extends AppCompatActivity
{

    private SensorManager sensorManager;
    private SensorListener sensorListener;
    private Sensor accelerometer;

    private TextView gachaInfo;
    private TextView gachaResult;
    private Button gachaGo;
    private boolean shakeInitiated = false;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gacha_page);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorListener = new SensorListener();


        gachaInfo = findViewById(R.id.gacha_info);
        gachaResult = findViewById(R.id.gacha_result);
        gachaGo = findViewById(R.id.gacha_button);


        gachaGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gachaInfo.setText("Shake Shake");
                shakeInitiated = true;
                sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_UI);

            }
        });

    }

    public class SensorListener implements SensorEventListener
    {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (shakeInitiated) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;
                if (acceleration > 4) { //根据需要调整
                    shakeInitiated = false;
                    sensorManager.unregisterListener(this);
                    int randomNumber = new Random().nextInt(5) + 1;
                    gachaResult.setText(String.valueOf(randomNumber));
                    gachaInfo.setText("Congratulations");
                }
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorListener);
        }
    }

    protected void onResume() {
        super.onResume();
        if (shakeInitiated && sensorManager != null) {
            sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }
}
