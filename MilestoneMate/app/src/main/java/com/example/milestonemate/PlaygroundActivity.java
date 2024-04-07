package com.example.milestonemate;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
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
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ArrayAdapter<String> drawerAdapter;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playground);
        db = new MyDatabase(this);

        characterImage = findViewById(R.id.characterImage);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.left_drawer);
        button = findViewById(R.id.button);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "0");
        characterList = db.getItemsByUidAndType(uid, Constants.CHARACTER);
        giftList = db.getItemsByUidAndType(uid, Constants.GIFT);

        setupCharacterImage();
        setupDrawer();
        displayGiftList();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerList);
            }
        });
    }

    private void setupCharacterImage() {
        if (characterList.size() > 0) {
            String characterImagePath = db.getItemImageByName(characterList.get(0), Constants.ITEM_IMAGE_ONE);
            try {
                AssetManager assetManager = getAssets();
                InputStream is = assetManager.open(characterImagePath);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                characterImage.setImageBitmap(bitmap);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupDrawer() {
        // 示例数据，替换为您自己的抽屉列表项
        String[] drawerItems = new String[]{"Item 1", "Item 2", "Item 3"};
        drawerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerItems);
        drawerList.setAdapter(drawerAdapter);
        // 为抽屉列表项设置点击监听器
        drawerList.setOnItemClickListener((parent, view, position, id) -> {
            // TODO: 在这里处理点击事件
            drawerLayout.closeDrawer(drawerList);
        });
    }

    private void displayGiftList() {
        List<GiftSlot> giftSlots = db.getGiftListByUid(uid);
        giftRecyclerView = findViewById(R.id.giftList);
        giftRecyclerView.setHasFixedSize(true);
        giftLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        giftRecyclerView.setLayoutManager(giftLayoutManager);
        giftAdapter = new GiftRecyclerView(giftSlots, this);
        giftRecyclerView.setAdapter(giftAdapter);
    }
}
