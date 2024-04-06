package com.example.milestonemate;

import android.content.Context;
import android.content.SharedPreferences;
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
    private MyDatabase db;
    private SensorManager sensorManager;
    private SensorListener sensorListener;
    private Sensor accelerometer;

    private TextView gachaInfo;
    private TextView gachaResult;
    private Button gachaGo;
    private boolean shakeInitiated = false;
    private TextView rewardPointsText;
    private String uid;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gacha_page);
        db = new MyDatabase(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorListener = new SensorListener();


        gachaInfo = findViewById(R.id.gacha_info);
        gachaResult = findViewById(R.id.gacha_result);
        gachaGo = findViewById(R.id.gacha_button);
        rewardPointsText = findViewById(R.id.rewardPoint);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "0");

        displayRewardPoints();


        gachaGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gachaInfo.setText("Shake Shake");
                shakeInitiated = true;
                sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
                updateRewardPoints();
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

    private void displayRewardPoints(){
        int currentRewardPoints = db.getValueByUid(uid, Constants.REWARD_POINT);
        String formattedText = getString(R.string.reward_points, currentRewardPoints);
        rewardPointsText.setText(formattedText);
    }

    private void updateRewardPoints(){
        int currentRewardPoints = db.getValueByUid(uid, Constants.REWARD_POINT);
        currentRewardPoints -= 10;
        db.updateRewardPointsByUid(uid, currentRewardPoints);
        displayRewardPoints();
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
