package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private EditText etSUUsername,
            etEmail,
            etConfirmEmail,
            etSUPassword,
            etConfirmPassword,
            etPhone;
    private Button btnCreateAccount;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 1) wire up & init DB
        dbHelper        = new DBHelper(this);
        etSUUsername    = findViewById(R.id.etSUUsername);
        etEmail         = findViewById(R.id.etEmail);
        etConfirmEmail  = findViewById(R.id.etConfirmEmail);
        etSUPassword    = findViewById(R.id.etSUPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone         = findViewById(R.id.etPhone);
        btnCreateAccount= findViewById(R.id.btnCreateAccount);

        btnCreateAccount.setOnClickListener(v -> {
            String user  = etSUUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String email2= etConfirmEmail.getText().toString().trim();
            String pass  = etSUPassword.getText().toString().trim();
            String pass2 = etConfirmPassword.getText().toString().trim();

            // --- basic validation ---
            if (user.isEmpty()) {
                etSUUsername.setError("Username required");
                return;
            }
            if (!email.equals(email2)) {
                etConfirmEmail.setError("Emails must match");
                return;
            }
            if (!pass.equals(pass2)) {
                etConfirmPassword.setError("Passwords must match");
                return;
            }

            // 2) insert into your DB
            boolean ok = dbHelper.insertUser(user, pass);
            if (!ok) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3) all good → move on to InterestsActivity
            Intent i = new Intent(SignUpActivity.this, InterestsActivity.class);
            startActivity(i);
            finish();  // so user can’t back→overwrite
        });
    }
}
