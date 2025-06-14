package com.example.study;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginButton;

    // Default username and password
    private static final String DEFAULT_USERNAME = "moni";
    private static final String DEFAULT_PASSWORD = "1004";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input from EditText fields
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // Check if username and password match the default
                if (username.equals(DEFAULT_USERNAME) && password.equals(DEFAULT_PASSWORD)) {
                    // If credentials are correct, go to the VideoActivity
                    Intent intent = new Intent(LoginActivity.this, VideoActivity.class);
                    startActivity(intent);
                    finish(); // Close the login activity so the user cannot go back
                } else {
                    // Show a toast message if credentials are incorrect
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
