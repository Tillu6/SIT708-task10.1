package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
// ← this is the only change you need:
//    replace android.widget.CardView with the androidx one
import androidx.cardview.widget.CardView;

public class TaskActivity extends AppCompatActivity {
    private CardView     cardQ1, cardQ2, cardQ3;
    private RadioGroup   rgAnswers1, rgAnswers2, rgAnswers3;
    private ImageButton  btnNext1, btnNext2;
    private Button       btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // wire up views
        cardQ1     = findViewById(R.id.cardQ1);
        cardQ2     = findViewById(R.id.cardQ2);
        cardQ3     = findViewById(R.id.cardQ3);
        rgAnswers1 = findViewById(R.id.rgAnswers1);
        rgAnswers2 = findViewById(R.id.rgAnswers2);
        rgAnswers3 = findViewById(R.id.rgAnswers3);
        btnNext1   = findViewById(R.id.btnNext1);
        btnNext2   = findViewById(R.id.btnNext2);
        btnSubmit  = findViewById(R.id.btnSubmit);

        // only show Q1 at start
        cardQ2.setVisibility(View.GONE);
        cardQ3.setVisibility(View.GONE);

        btnNext1.setOnClickListener(v -> {
            if (rgAnswers1.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please answer question 1", Toast.LENGTH_SHORT).show();
                return;
            }
            cardQ1.setVisibility(View.GONE);
            cardQ2.setVisibility(View.VISIBLE);
        });

        btnNext2.setOnClickListener(v -> {
            if (rgAnswers2.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please answer question 2", Toast.LENGTH_SHORT).show();
                return;
            }
            cardQ2.setVisibility(View.GONE);
            cardQ3.setVisibility(View.VISIBLE);
        });

        btnSubmit.setOnClickListener(v -> {
            if (rgAnswers3.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please answer question 3", Toast.LENGTH_SHORT).show();
                return;
            }

            // gather answers
            String a1 = ((RadioButton) findViewById(rgAnswers1.getCheckedRadioButtonId()))
                    .getText().toString();
            String a2 = ((RadioButton) findViewById(rgAnswers2.getCheckedRadioButtonId()))
                    .getText().toString();
            String a3 = ((RadioButton) findViewById(rgAnswers3.getCheckedRadioButtonId()))
                    .getText().toString();

            Intent i = new Intent(TaskActivity.this, ResultsActivity.class);
            i.putExtra("answer1", a1);
            i.putExtra("answer2", a2);
            i.putExtra("answer3", a3);
            startActivity(i);
            finish();
        });
    }
}
