package com.example.milestonemate;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GiftRecyclerView extends RecyclerView.Adapter<GiftRecyclerView.ViewHolder> {
    private List<GiftSlot> giftSlots;
    private Context context;

    public GiftRecyclerView(List<GiftSlot> slots, Context context){
        giftSlots = slots;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftRecyclerView.ViewHolder holder, int position) {
        holder.getAmountText().setText(String.valueOf(giftSlots.get(position).getAmount()));
        holder.getNameText().setText(giftSlots.get(position).getName());
        try {
            // 获取AssetManager
            AssetManager assetManager = context.getAssets();

            // 从assets目录下打开图片文件流
            InputStream is = assetManager.open(giftSlots.get(position).getImagePath());

            // 使用BitmapFactory从InputStream创建Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            // 设置Bitmap到ImageView
            holder.getGiftImage().setImageBitmap(bitmap);

            // 关闭InputStream
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常，可能是文件未找到等情况
        }
    }

    @Override
    public int getItemCount() {
        return giftSlots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView amountText;
        private TextView nameText;
        private ImageView giftImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amountText = itemView.findViewById(R.id.giftAmount);
            nameText = itemView.findViewById(R.id.giftName);
            giftImage = itemView.findViewById(R.id.giftImage);
        }

        public TextView getAmountText(){return amountText;}

        public TextView getNameText(){return nameText;}

        public ImageView getGiftImage(){return giftImage;}
    }
}
