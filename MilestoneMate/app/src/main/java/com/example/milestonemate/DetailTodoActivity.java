package com.example.milestonemate;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DetailTodoActivity extends AppCompatActivity
{
    private TextView todoDetailName, todoDetailTime;
    private String title,date;
    private Button state;

    private EditText describeText;

    private ImageView imageView;
    private String imagePath;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail_page);

        todoDetailName = findViewById(R.id.todo_detail_info_name);
        todoDetailTime = findViewById(R.id.todo_detail_info_time);

        imageView = findViewById(R.id.imageView3);

        describeText = findViewById(R.id.todo_detail_info_description);


        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        //这里我只想到显示title的方法，时间的话要改todoslot class的变量，我没动。然后我想着用key搜索不知道行不行，不知道怎么弄了，我现在不知道怎么把database里面的东西用key提出来。

        todoDetailName.setText(title);

        state = findViewById(R.id.todo_detail_info_submit_stateChange);

        //set click linstener
        state.setOnClickListener(v -> {
            //--------1 up
            // requestCameraPermission();
            //photo intent
            Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // start
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });


    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int PERMISSION_REQUEST_CAMERA = 2;

    private void requestCameraPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
        else
        {
            // 如果权限已经被授予，直接调用相机
            dispatchTakePictureIntent();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 如果权限被授予，再次调用相机
                dispatchTakePictureIntent();
            } else {
                // 权限被拒绝，可以展示一些提示给用户
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------
    // 两套相机调用，还没有完全弄完

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // 保存图片到外部存储
            String imagePath = saveImageToExternalStorage(imageBitmap);

            imageView.setImageBitmap(imageBitmap);

            this.imagePath = imagePath;
        }

    }
    //这里只是拍照，我在想如果要往数据库里加新东西那是不是就得和我上面那个问题一样要改todoslot，就这里照片和细节我没有往数据库里面写。
    //最好是额外做一个button去保存数据，这个就看你把，哦对，这个拍照的东西挺阴间的

    private String saveImageToExternalStorage(Bitmap bitmap)
    {
        //import photo and return uri for image
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "todo_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try
        {
            if(uri != null)
            {
                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                if (outputStream != null)
                {
                    outputStream.close();
                }
                return uri.toString();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
