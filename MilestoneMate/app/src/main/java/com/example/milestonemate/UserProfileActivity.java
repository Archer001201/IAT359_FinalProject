package com.example.milestonemate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {
    private MyDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db = new MyDatabase(this);

        TextView usernameText = findViewById(R.id.username);
        TextView emailText = findViewById(R.id.email);
        TextView rewardPointText = findViewById(R.id.rewardPoint);


        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");
        usernameText.setText(username);
        emailText.setText(email);

        String rewardPoints = db.getValueByEmail(email, Constants.REWARD_POINT);
        rewardPointText.setText(rewardPoints);

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            Intent intent = new Intent(this, StartPageActivity.class);
            startActivity(intent);
            finish();
        });

        ImageButton calendarButton = findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(v -> {
            Intent intent  = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        Button gaChaForward = findViewById(R.id.GaCha);
        gaChaForward.setOnClickListener(v -> {
            Intent intent = new Intent(this, Gacha.class);
            startActivity(intent);
        });

        Button playground = findViewById(R.id.playground);
        playground.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlaygroundActivity.class);
            startActivity(intent);
        });
    }
}
