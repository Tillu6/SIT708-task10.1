package com.example.personalizedlearningapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningapp.API.ApiClient;
import com.example.personalizedlearningapp.model.Question;
import com.example.personalizedlearningapp.model.LlmQuestion;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private String taskTitle, taskDesc;
    private String username, avatarUri;
    private TextView tvTaskTitle, tvTaskDesc;
    private RecyclerView recyclerView;
    private Button btnSubmit;
    private ProgressBar progressBar;

    private List<Question> questionList = new ArrayList<>();
    private QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // Bind layout elements to variables
        tvTaskTitle = findViewById(R.id.tvTask_Title);
        tvTaskDesc = findViewById(R.id.tvTask_Desc);
        recyclerView = findViewById(R.id.newsRecycler);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);

        // Extract task and user information from intent
        Intent intent = getIntent();
        taskTitle = intent.getStringExtra("taskTitle");
        taskDesc = intent.getStringExtra("taskDesc");
        username = intent.getStringExtra("username");
        avatarUri = intent.getStringExtra("avatar_uri");

        // Display task title and description
        tvTaskTitle.setText(taskTitle);
        tvTaskDesc.setText(taskDesc);

        // Set up RecyclerView for displaying questions
        questionAdapter = new QuestionAdapter(questionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(questionAdapter);

        // Fetch quiz questions from the LLM API
        fetchQuestionsFromLLM(taskTitle);

        Log.d("------- TaskActivity", "Topic for API request: " + taskTitle);

        // Handle quiz submission
        btnSubmit.setOnClickListener(v -> {
            boolean hasAnswered = false;

            // Evaluate each question
            for (Question q : questionList) {
                if (q.selectedOption != -1) {
                    hasAnswered = true;
                    int correctIndex = getCorrectIndex(q);
                    if (q.selectedOption == correctIndex) {
                        q.response = "✅ Correct! Well done!";
                    } else {
                        q.response = "❌ Incorrect. Correct answer is: " + q.correctAnswer;
                    }
                } else {
                    q.response = "❌ No option selected.";
                }
            }

            // Ensure at least one question is answered
            if (!hasAnswered) {
                Toast.makeText(this, "Please answer at least one question before submitting.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Navigate to ResultActivity with quiz data
            Intent resultIntent = new Intent(this, ResultActivity.class);
            resultIntent.putExtra("questions", new ArrayList<>(questionList));
            resultIntent.putExtra("user_id", getIntent().getIntExtra("user_id", -1));
            resultIntent.putExtra("taskTitle", taskTitle);
            resultIntent.putExtra("taskDesc", taskDesc);
            resultIntent.putExtra("username", username);
            resultIntent.putExtra("avatar_uri", avatarUri);
            startActivity(resultIntent);
            finish();
        });
    }

    // Fetch quiz questions from the LLM backend
    private void fetchQuestionsFromLLM(String topic) {
        progressBar.setVisibility(View.VISIBLE); // Show loading indicator

        Log.d("------- API_REQUEST", "Request URL: http://10.0.2.2:5000/getQuiz?topic=" + Uri.encode(topic));

        ApiClient.fetchQuizFromLLM(topic, new ApiClient.OnQuizResponse() {
            @Override
            public void onSuccess(List<LlmQuestion> questions) {
                runOnUiThread(() -> {
                    Log.d("API_RESPONSE", "Received " + questions.size() + " questions");
                    progressBar.setVisibility(View.GONE); // Hide loading indicator

                    // Convert LLM response into local Question objects
                    for (LlmQuestion q : questions) {
                        String[] opts = q.options.toArray(new String[0]);
                        Question local = new Question(q.question, opts);

                        int correctIndex = correctLetterToIndex(q.correct_answer);
                        if (correctIndex >= 0 && correctIndex < opts.length) {
                            local.correctAnswer = opts[correctIndex];
                        } else {
                            local.correctAnswer = "";
                        }
                        questionList.add(local);
                    }

                    questionAdapter.notifyDataSetChanged(); // Refresh UI
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE); // Hide loading indicator
                    Toast.makeText(TaskActivity.this, "Failed to load questions: " + errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    // Get the index of the correct answer text in options[]
    private int getCorrectIndex(Question q) {
        for (int i = 0; i < q.options.length; i++) {
            if (q.options[i].equals(q.correctAnswer)) {
                return i;
            }
        }
        return -1;
    }

    // Convert letter (A/B/C/D) to index (0/1/2/3)
    private int correctLetterToIndex(String letter) {
        switch (letter.toUpperCase()) {
            case "A": return 0;
            case "B": return 1;
            case "C": return 2;
            case "D": return 3;
            default: return -1;
        }
    }
}
