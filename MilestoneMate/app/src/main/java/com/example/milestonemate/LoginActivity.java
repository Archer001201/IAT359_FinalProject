package com.example.milestonemate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private MyDatabase db;
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new MyDatabase(this);
        context = this;

        setContentView(R.layout.activity_login);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (!db.checkUserExists(email)){
                    Toast.makeText(context, "invalid email", Toast.LENGTH_LONG).show();
                }
                else{
                    String pw = db.getValueByEmail(email, Constants.PASSWORD);
                    if (!password.equals(pw)){
                        Toast.makeText(context, "wrong password", Toast.LENGTH_LONG).show();
                        passwordInput.setText("");
                    }
                    else{
                        String uid = db.getValueByEmail(email, Constants.UID);
                        String username = db.getValueByEmail(email, Constants.USERNAME);

                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("uid", uid);
                        editor.putString("username", username);
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
