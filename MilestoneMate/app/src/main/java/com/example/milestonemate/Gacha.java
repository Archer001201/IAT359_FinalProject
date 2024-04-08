package com.example.milestonemate;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class Gacha extends AppCompatActivity
{
    private MyDatabase db;
    private SensorManager sensorManager;
    private SensorListener sensorListener;
    private Sensor accelerometer;

    private TextView gachaInfo;
    private TextView gachaResult, gaChaResult;
    private ImageView itemImage;
    private Button gachaGo;
    private boolean shakeInitiated = false;
    private TextView rewardPointsText;
    private String uid;
    private final float characterProbability = 0.3f;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gacha_page);
        db = new MyDatabase(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorListener = new SensorListener();


        gachaInfo = findViewById(R.id.gacha_info);
        gaChaResult = findViewById(R.id.gaCha_describe);
        gachaResult = findViewById(R.id.gacha_result);
        gachaGo = findViewById(R.id.gacha_button);
        rewardPointsText = findViewById(R.id.rewardPoint);
        itemImage = findViewById(R.id.itemImage);
        itemImage.setImageResource(R.drawable.gachaex);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "0");

        displayRewardPoints();


        gachaGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gachaResult.setText("");
                itemImage.setImageDrawable(null);
                if (db.getValueByUid(uid, Constants.REWARD_POINT) < 10) return;
                gachaInfo.setText("Shake Shake");
                gaChaResult.setText("Shake to explore a new character or item!");
                itemImage.setImageResource(R.drawable.shake);
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

                    float randNum_1 = new Random().nextFloat();
                    String type = Constants.GIFT;
                    if (randNum_1 <= characterProbability){
                        type = Constants.CHARACTER;
                    }
                    List<String> items = db.getItemNameByType(type);
                    Log.d("TAG", "onSensorChanged item size: " + items.size());

                    int randNum_2 = new Random().nextInt(items.size());
                    String result = items.get(randNum_2);
                    String imagePath = db.getItemImageByName(result, Constants.ITEM_IMAGE_ONE);
                    Log.d("TAG", "onSensorChanged: " + imagePath);

                    try {
                        // 获取AssetManager
                        AssetManager assetManager = getAssets();

                        // 从assets目录下打开图片文件流
                        InputStream is = assetManager.open(imagePath);

                        // 使用BitmapFactory从InputStream创建Bitmap
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        // 设置Bitmap到ImageView
                        itemImage.setImageBitmap(bitmap);

                        // 关闭InputStream
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        // 处理异常，可能是文件未找到等情况
                    }

                    gachaResult.setText(result);
                    gachaInfo.setText("Congratulations");

                    if (type.equals(Constants.CHARACTER)){
                        db.insertCharacter(uid, result);
                    }
                    else{
                        db.updateGiftTable(uid,result,true);
                    }
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
