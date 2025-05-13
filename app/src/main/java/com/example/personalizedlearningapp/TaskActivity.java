// LLM Backend API Source: https://github.com/sit3057082025/BackendApiLLM_T6.1D
package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.personalizedlearningapp.data.model.QuizItem;
import com.example.personalizedlearningapp.data.model.QuizResponse;
import com.example.personalizedlearningapp.data.network.ApiService;
import com.example.personalizedlearningapp.data.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity {
    private CardView    cardQ1, cardQ2, cardQ3;
    private TextView    tvQuestion1, tvQuestion2, tvQuestion3;
    private RadioGroup  rgAnswers1, rgAnswers2, rgAnswers3;
    private ImageButton btnNext1, btnNext2;
    private View        btnSubmit;  // can be Button or any clickable view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // bind views
        cardQ1      = findViewById(R.id.cardQ1);
        cardQ2      = findViewById(R.id.cardQ2);
        cardQ3      = findViewById(R.id.cardQ3);
        tvQuestion1 = findViewById(R.id.tvQuestion1);
        tvQuestion2 = findViewById(R.id.tvQuestion2);
        tvQuestion3 = findViewById(R.id.tvQuestion3);
        rgAnswers1  = findViewById(R.id.rgAnswers1);
        rgAnswers2  = findViewById(R.id.rgAnswers2);
        rgAnswers3  = findViewById(R.id.rgAnswers3);
        btnNext1    = findViewById(R.id.btnNext1);
        btnNext2    = findViewById(R.id.btnNext2);
        btnSubmit   = findViewById(R.id.btnSubmit);

        // hide all until questions load
        cardQ1.setVisibility(View.GONE);
        cardQ2.setVisibility(View.GONE);
        cardQ3.setVisibility(View.GONE);

        // Fetch quiz from LLM API
        ApiService api = RetrofitClient.getApi();
        api.getQuiz("basic concepts").enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setupQuiz(response.body().getQuiz());
                } else {
                    Toast.makeText(TaskActivity.this,
                            "Error fetching quiz: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                Toast.makeText(TaskActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupQuiz(List<QuizItem> quiz) {
        if (quiz.size() < 3) {
            Toast.makeText(this, "Not enough questions returned.", Toast.LENGTH_LONG).show();
            return;
        }

        // Q1
        QuizItem q1 = quiz.get(0);
        tvQuestion1.setText(q1.getQuestion());
        rgAnswers1.removeAllViews();
        for (String opt : q1.getOptions()) {
            RadioButton rb = new RadioButton(this);
            rb.setText(opt);
            rgAnswers1.addView(rb);
        }

        // Q2
        QuizItem q2 = quiz.get(1);
        tvQuestion2.setText(q2.getQuestion());
        rgAnswers2.removeAllViews();
        for (String opt : q2.getOptions()) {
            RadioButton rb = new RadioButton(this);
            rb.setText(opt);
            rgAnswers2.addView(rb);
        }

        // Q3
        QuizItem q3 = quiz.get(2);
        tvQuestion3.setText(q3.getQuestion());
        rgAnswers3.removeAllViews();
        for (String opt : q3.getOptions()) {
            RadioButton rb = new RadioButton(this);
            rb.setText(opt);
            rgAnswers3.addView(rb);
        }

        // show first card
        cardQ1.setVisibility(View.VISIBLE);

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

            // collect answers
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
