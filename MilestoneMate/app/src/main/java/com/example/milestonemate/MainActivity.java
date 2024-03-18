package com.example.milestonemate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new MyDatabase(this);

        //Get user's login state
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        String email = sharedPreferences.getString("email", "");
        if (!db.checkUserExists(email)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
        }
        //If is not login, turn to login/signup page
        if (!isLoggedIn) {
            Intent intent = new Intent(this, StartPageActivity.class);
            startActivity(intent);
            finish();
        }

        //Display current date
        TextView dateTextView = findViewById(R.id.dateText);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        dateTextView.setText(currentDate);
        //Display welcome message and username
        TextView welcomeTextView = findViewById(R.id.welcomeText);
        String username = sharedPreferences.getString("username", "username");
        welcomeTextView.setText(getString(R.string.welcome_text, username));

        //Listen click event on Add New button
        Button addNewButton = findViewById(R.id.addNewButton);
        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNewTodoActivity.class);
                startActivity(intent);
            }
        });
    }
}