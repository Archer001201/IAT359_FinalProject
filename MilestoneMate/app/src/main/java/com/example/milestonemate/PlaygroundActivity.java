package com.example.milestonemate;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PlaygroundActivity extends AppCompatActivity {
    MyDatabase db;
    private ImageView characterImage;
    private String uid;
    private List<String> characterList, giftList;
    private RecyclerView giftRecyclerView;
    private RecyclerView.Adapter giftAdapter;
    private RecyclerView.LayoutManager giftLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playground);
        db = new MyDatabase(this);

        characterImage = findViewById(R.id.characterImage);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "0");
        characterList = db.getItemsByUidAndType(uid, Constants.CHARACTER);
        giftList = db.getItemsByUidAndType(uid, Constants.GIFT);

        if (characterList.size() > 0){
            String characterImagePath = db.getItemImageByName(characterList.get(0), Constants.ITEM_IMAGE_ONE);

            try {
                // 获取AssetManager
                AssetManager assetManager = getAssets();

                // 从assets目录下打开图片文件流
                InputStream is = assetManager.open(characterImagePath);

                // 使用BitmapFactory从InputStream创建Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                // 设置Bitmap到ImageView
                characterImage.setImageBitmap(bitmap);

                // 关闭InputStream
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                // 处理异常，可能是文件未找到等情况
            }
        }

        DisplayGiftList();
    }

    private void DisplayGiftList(){
        List<GiftSlot> giftSlots = db.getGiftListByUid(uid);
        giftRecyclerView = findViewById(R.id.giftList);
        giftRecyclerView.setHasFixedSize(true);
        giftLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        giftRecyclerView.setLayoutManager(giftLayoutManager);
        giftAdapter = new GiftRecyclerView(giftSlots, this);
        giftRecyclerView.setAdapter(giftAdapter);
    }
}
