package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningapp.model.Question;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    private ResultAdapter resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Debug: log incoming intent data
        Log.d("----- DEBUG", "username: " + getIntent().getStringExtra("username"));
        Log.d("----- DEBUG", "avatar_uri: " + getIntent().getStringExtra("avatar_uri"));

        RecyclerView recyclerView = findViewById(R.id.newsRecycler);
        Button btnContinue = findViewById(R.id.btnContinue);

        // Retrieve passed question list from intent
        @SuppressWarnings("unchecked")
        List<Question> questions = (List<Question>) getIntent().getSerializableExtra("questions");
        if (questions == null) questions = new ArrayList<>();

        // Prepare result list for display in the recycler view
        List<String[]> results = new ArrayList<>();
        for (Question q : questions) {
            results.add(new String[]{q.questionTitle, q.response});
        }

        resultAdapter = new ResultAdapter(results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(resultAdapter);

        // Retrieve metadata and generate a timestamp
        int userId = getIntent().getIntExtra("user_id", -1);
        String taskTitle = getIntent().getStringExtra("taskTitle");
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        // Save question results to database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        for (Question q : questions) {
            String selectedText = (q.selectedOption != -1) ? q.options[q.selectedOption] : "";
            String[] opts = q.options;
            String optionA = opts.length > 0 ? opts[0] : "";
            String optionB = opts.length > 1 ? opts[1] : "";
            String optionC = opts.length > 2 ? opts[2] : "";
            String optionD = opts.length > 3 ? opts[3] : "";

            dbHelper.saveQuestionResult(
                    userId, taskTitle, q.questionTitle, q.correctAnswer, selectedText,
                    optionA, optionB, optionC, optionD,
                    timestamp
            );
        }

        // Continue to DashboardActivity after quiz completion
        List<Question> finalQuestions = questions;
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("taskTitle", taskTitle);
            intent.putExtra("taskDesc", getIntent().getStringExtra("taskDesc"));
            intent.putExtra("username", getIntent().getStringExtra("username"));
            intent.putExtra("avatar_uri", getIntent().getStringExtra("avatar_uri"));
            intent.putExtra("user_id", userId);
            intent.putExtra("questions", (ArrayList<Question>) finalQuestions); // Still passing questions for legacy compatibility
            startActivity(intent);
            finish();
        });

        // Back button handling – go back to the TaskActivity for retaking the quiz
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ResultActivity.this, TaskActivity.class);
                intent.putExtra("taskTitle", getIntent().getStringExtra("taskTitle"));
                intent.putExtra("taskDesc", getIntent().getStringExtra("taskDesc"));
                intent.putExtra("username", getIntent().getStringExtra("username"));
                intent.putExtra("avatar_uri", getIntent().getStringExtra("avatar_uri"));
                startActivity(intent);
                finish();
            }
        });
    }
}
