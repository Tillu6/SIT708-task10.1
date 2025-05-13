package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make sure this matches your layout filename:
        setContentView(R.layout.activity_home);

        // This ID must exist in activity_home.xml:
        Button btnStartQuiz = findViewById(R.id.btnStartQuiz);
        btnStartQuiz.setOnClickListener(v -> {
            // Launch the quiz screen
            startActivity(new Intent(HomeActivity.this, TaskActivity.class));
        });
    }
}
