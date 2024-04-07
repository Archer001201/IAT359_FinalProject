package com.example.milestonemate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

public class DetailTodoActivity extends AppCompatActivity
{
    MyDatabase db;
    private TextView todoDetailName, todoDetailTime, todoDetailState;
    private String title, date, state, id, description;
    private Button deleteButton, submitButton;
    private ImageButton cameraButton;

    private TextView describeText;

    private ImageView imageView;
    private String imagePath;
    private ActivityResultLauncher<Intent> takePictureLauncher;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail_page);
        db = new MyDatabase(this);

        todoDetailName = findViewById(R.id.todo_detail_info_name);
        todoDetailTime = findViewById(R.id.todo_detail_info_time);
        todoDetailState = findViewById(R.id.todo_detail_state);

        imageView = findViewById(R.id.imageView3);

        describeText = findViewById(R.id.todo_info);


        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        date = intent.getStringExtra("date");
        state = intent.getStringExtra("state");
        id = intent.getStringExtra("todo_id");
        description = intent.getStringExtra("description");
        imagePath = intent.getStringExtra("image_path");
        updateImageView(imagePath);

        //这里我只想到显示title的方法，时间的话要改todoslot class的变量，我没动。然后我想着用key搜索不知道行不行，不知道怎么弄了，我现在不知道怎么把database里面的东西用key提出来。

        todoDetailName.setText(title);
        todoDetailTime.setText(date);
        todoDetailState.setText(state);
        describeText.setText(description);

        cameraButton = findViewById(R.id.cameraButton);
        submitButton = findViewById(R.id.todo_detail_info_submit_stateChange);
        deleteButton = findViewById(R.id.todo_delete_button);

        //Set camera launcher
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if (imagePath != null && !imagePath.isEmpty()) {
                                updateImageView(imagePath);
                            }
                            Intent data = result.getData();
                        }
                    }
                });

        //set click listener
        cameraButton.setOnClickListener(v -> {
            requestCameraPermission();
        });

        submitButton.setOnClickListener(v -> {
            if (state.equals(Constants.INCOMPLETE)){
                state = Constants.COMPLETE;
                todoDetailState.setText(state);

                description = describeText.getText().toString();
                db.updateTodoById(id, state, description, imagePath);
                addRewardPoints();
                goToMainActivity();
            }
        });

        deleteButton.setOnClickListener(v -> {
            //db.deleteTodoById(id);
            //goToMainActivity();
            showDeleteConfirmationDialog();

        });
    }

    private void addRewardPoints(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");
        String email = sharedPreferences.getString("email", "");
        int currentRewardPoints = Integer.parseInt(db.getValueByEmail(email, Constants.REWARD_POINT));
        int finalRewardPoints = currentRewardPoints + 10;
        db.updateRewardPointsByUid(uid, finalRewardPoints);
    }

    private void goToMainActivity(){
        Intent intent_toMain = new Intent(this, MainActivity.class);
        startActivity(intent_toMain);
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
                Log.d("DetailTodoActivity", "Camera permission granted - launching camera");
                dispatchTakePictureIntent();
            } else {
                // 权限被拒绝，可以展示一些提示给用户
                Log.d("DetailTodoActivity", "Camera permission denied");
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // 创建文件URI
            Uri photoURI = null;
            try {
                photoURI = createImageFileUri();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (photoURI != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureLauncher.launch(takePictureIntent);
            }
        }
    }

    private Uri createImageFileUri() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        imagePath = image.getAbsolutePath();

        return FileProvider.getUriForFile(this, "your.package.fileprovider", image);
    }

    private void updateImageView(String imagePath) {

        if (imageView.getWidth() == 0 || imageView.getHeight() == 0) {
            return;
        }
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            updateImageView(imagePath);
        }
    }

    private void showDeleteConfirmationDialog() {
        // 创建AlertDialog.Builder实例
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this todo?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { // 将参数名从id更改为which
                // 用户确认删除，执行删除操作
                db.deleteTodoById(DetailTodoActivity.this.id); // 明确使用DetailTodoActivity的id成员变量
                // 跳转到主界面
                goToMainActivity();
                // 提示信息
                Toast.makeText(DetailTodoActivity.this, "Todo deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // 用户取消删除，关闭对话框，不进行任何操作
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // 创建并显示AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



}
