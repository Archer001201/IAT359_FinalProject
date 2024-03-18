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
    private EditText titleInput;
    private EditText dateInput;
    private Button submitButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new MyDatabase(this);

        setContentView(R.layout.activity_add_new_todo);
        titleInput = findViewById(R.id.title);
        dateInput = findViewById(R.id.dueDate);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleInput.getText().toString().trim();
                String date = dateInput.getText().toString().trim();
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String uid = sharedPreferences.getString("uid", "0");

                db.addNewTodo(uid, title, date);

                Intent intent = new Intent(AddNewTodoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
