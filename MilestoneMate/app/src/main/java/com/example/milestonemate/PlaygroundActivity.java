package com.example.milestonemate;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
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

        giftRecyclerView = findViewById(R.id.giftList);

        characterImage = findViewById(R.id.characterImage);
        characterImage.setOnDragListener(new MyDragListener());

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
        //giftRecyclerView = findViewById(R.id.giftList);
        giftRecyclerView.setHasFixedSize(true);
        giftLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        giftRecyclerView.setLayoutManager(giftLayoutManager);

        //giftAdapter = new GiftRecyclerView(giftSlots, this);
        // 使用自定义适配器 GiftAdapter 替换原来的 GiftRecyclerView
        giftAdapter = new GiftAdapter(this, giftSlots); // 传递this作为context
        giftRecyclerView.setAdapter(giftAdapter);
    }

    class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder> {
        private List<GiftSlot> giftSlots;
        private Context context; // 添加Context成员变量
        // 修改构造函数来接收Context和GiftSlot列表
        GiftAdapter(Context context, List<GiftSlot> giftSlots) {
            this.context = context;
            this.giftSlots = giftSlots;
        }

        @NonNull
        @Override
        public GiftAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_slot, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GiftAdapter.ViewHolder holder, int position) {
            GiftSlot slot = giftSlots.get(position);
            // 加载图片
            try {
                // 获取AssetManager
                AssetManager assetManager = context.getAssets();
                // 从assets目录读取图片
                InputStream is = assetManager.open(slot.getImagePath());
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                holder.imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // 处理异常，例如文件未找到
                e.printStackTrace();
            }

            // 为imageView设置触摸监听器以启动拖动操作
            holder.imageView.setOnTouchListener((view, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    return true;
                }
                return false;
            });
        }

        @Override
        public int getItemCount() {
            return giftSlots.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView; // 确保变量名称与你的使用保持一致

            ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.giftImage); // 与你布局中ImageView的ID匹配
            }
        }
    }




    class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    // 当图片拖放到characterImage上时执行的操作
                    // 例如：隐藏拖动的视图或更新数据库等
                    // 这里可以实现具体的判定逻辑
                    View view = (View) event.getLocalState();
                    view.setVisibility(View.INVISIBLE); // 或者执行其他逻辑
                    break;
                // 可以根据需要处理其他事件
            }
            return true;
        }
    }
}
