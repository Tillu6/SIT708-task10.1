package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;    // ← import TextView
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvSignUp;    // ← add field
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PersonalizedLearningApp);
        setContentView(R.layout.activity_main);

        dbHelper   = new DBHelper(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin   = findViewById(R.id.btnLogin);
        tvSignUp   = findViewById(R.id.tvSignUp);    // ← bind it

        btnLogin.setOnClickListener(v -> {
            String user = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (user.isEmpty()) {
                etUsername.setError("Required");
                return;
            }
            if (pass.isEmpty()) {
                etPassword.setError("Required");
                return;
            }

            if (dbHelper.checkUser(user, pass)) {
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                i.putExtra("USERNAME", user);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        // ← add this:
        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        });
    }
}
