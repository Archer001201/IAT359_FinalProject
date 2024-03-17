package com.example.milestonemate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_page);

        //go to login page
        findViewById(R.id.enterLoginButton).setOnClickListener(view -> {
            Intent intent = new Intent(StartPageActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        //go to signup page
        findViewById(R.id.enterSignupButton).setOnClickListener(view -> {
            Intent intent = new Intent(StartPageActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}
