// LoginActivity.java
package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private LottieAnimationView lottieLogin;
    private EditText            etUsername, etPassword;
    private Button              btnLogin;
    private TextView            tvSignUp;
    private DBHelper            db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // wire up
        lottieLogin = findViewById(R.id.lottieLogin);
        etUsername  = findViewById(R.id.etUsername);
        etPassword  = findViewById(R.id.etPassword);
        btnLogin    = findViewById(R.id.btnLogin);
        tvSignUp    = findViewById(R.id.tvSignUp);

        db = new DBHelper(this);

        // on login click
        btnLogin.setOnClickListener(v -> {
            String user = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (user.isEmpty()) {
                etUsername.setError("Username required");
                return;
            }
            if (pass.isEmpty()) {
                etPassword.setError("Password required");
                return;
            }

            // check credentials
            if (db.checkUser(user, pass)) {
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                i.putExtra("USERNAME", user);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        // sign-up link
        tvSignUp.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class))
        );
    }
}
