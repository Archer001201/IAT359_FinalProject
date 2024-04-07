package com.example.milestonemate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddNewTodoActivity extends AppCompatActivity {
    private MyDatabase db;
    private EditText describeText;
    private EditText titleInput;
    private EditText dateInput;
    private Button submitButton;


    // Initializes the activity
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize database helper and set up the UI layout
        db = new MyDatabase(this);


        setContentView(R.layout.activity_add_new_todo);
        titleInput = findViewById(R.id.title);
        dateInput = findViewById(R.id.dueDate);
        submitButton = findViewById(R.id.submitButton);
        describeText = findViewById(R.id.addNewDetail);
        // Set up a click listener for the submit button to add a new todo item
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleInput.getText().toString().trim();
                String date = dateInput.getText().toString().trim();
                String description = describeText.getText().toString().trim(); // 获取描述文本
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String uid = sharedPreferences.getString("uid", "0");

                db.addNewTodo(uid, title, date,description);
                // Add new todo item to the database and navigate back to the main activity
                Intent intent = new Intent(AddNewTodoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
