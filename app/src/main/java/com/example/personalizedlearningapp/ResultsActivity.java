package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView tv1 = findViewById(R.id.tvRes1);
        TextView tv2 = findViewById(R.id.tvRes2);
        TextView tv3 = findViewById(R.id.tvRes3);
        Button   bc = findViewById(R.id.btnContinue);

        Intent i = getIntent();
        tv1.setText("1. " + i.getStringExtra("answer1"));
        tv2.setText("2. " + i.getStringExtra("answer2"));
        tv3.setText("3. " + i.getStringExtra("answer3"));

        bc.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }
}
