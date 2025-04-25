package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {
    private TextView     tvHelloName, tvTasksDue, tvTaskTitle, tvTaskDesc;
    private CardView     cardTask;
    private ImageButton  btnGoToTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvHelloName = findViewById(R.id.tvHelloName);
        tvTasksDue  = findViewById(R.id.tvTasksDue);
        tvTaskTitle = findViewById(R.id.tvTaskTitle);
        tvTaskDesc  = findViewById(R.id.tvTaskDesc);
        cardTask    = findViewById(R.id.cardTask);
        btnGoToTask = findViewById(R.id.btnGoToTask);

        // get the username extra (might be null if you came from Sign-up flow)
        String user = getIntent().getStringExtra("USERNAME");
        if (user == null || user.isEmpty()) {
            user = "Student";  // fallback
        }
        tvHelloName.setText("Hello, " + user);
        tvTasksDue.setText("You have 2 tasks due");

        // give your card a real title + description
        tvTaskTitle.setText("Personalized Quiz");
        tvTaskDesc.setText("This task will help you gauge your understanding of basic concepts. Tap the arrow to start.");

        btnGoToTask.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, TaskActivity.class))
        );
    }
}
