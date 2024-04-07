package com.example.milestonemate;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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

public class CharacterRecyclerView extends RecyclerView.Adapter<CharacterRecyclerView.ViewHolder> {
    private List<CharacterSlot> characterSlots;
    private Context context;
    private OnItemClickListener listener;

    public CharacterRecyclerView(List<CharacterSlot> slots, Context context, OnItemClickListener listener){
        characterSlots = slots;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usercharacter_slot, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getNameText().setText(characterSlots.get(position).getName());
        try {
            // 获取AssetManager
            AssetManager assetManager = context.getAssets();

            // 从assets目录下打开图片文件流
            InputStream is = assetManager.open(characterSlots.get(position).getImagePath());

            // 使用BitmapFactory从InputStream创建Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            // 设置Bitmap到ImageView
            holder.getCharacterImage().setImageBitmap(bitmap);

            // 关闭InputStream
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常，可能是文件未找到等情况
        }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("TAG", "onClick: " + context.getClass().getName());
//
//            }
//        });

        holder.itemView.setOnClickListener(v -> listener.onItemClick(holder.getTextFromNameTextView()));
    }

    @Override
    public int getItemCount() {
        return characterSlots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameText;
        private ImageView characterImage;

        public ViewHolder(View view, OnItemClickListener listener){
            super(view);
            nameText = view.findViewById(R.id.userCharacterName);
            characterImage = view.findViewById(R.id.userCharacterImage);

            view.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    listener.onItemClick(String.valueOf(position));
                }
            });
        }

        public TextView getNameText(){return nameText;}

        public ImageView getCharacterImage(){return characterImage;}

        public String getTextFromNameTextView() {
            return nameText.getText().toString();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String data);
    }
}
