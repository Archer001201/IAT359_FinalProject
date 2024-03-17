package com.example.milestonemate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    MyDatabase db;
    private EditText usernameInput;
    private EditText emailInput;
    private EditText passwordInput1;
    private EditText passwordInput2;
    private Button signupButton;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new MyDatabase(this);
        context = this;

        setContentView(R.layout.activity_signup);

        usernameInput = findViewById(R.id.username);
        emailInput = findViewById(R.id.email);
        passwordInput1 = findViewById(R.id.password1);
        passwordInput2 = findViewById(R.id.password2);
        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password1 = passwordInput1.getText().toString().trim();
                String password2 = passwordInput2.getText().toString().trim();

                if (!password1.equals(password2)){
                    Toast.makeText(context, "invalid password", Toast.LENGTH_LONG).show();
                    passwordInput1.setText("");
                    passwordInput2.setText("");
                }
                else{
                    db.addNewUser(username, email, password1);
                    Toast.makeText(context, "Created new user", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
