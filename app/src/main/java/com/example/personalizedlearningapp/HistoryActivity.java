package com.example.personalizedlearningapp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningapp.model.Question;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Initialize UI components
        recyclerView = findViewById(R.id.historyRecyclerView);
        ImageView btnBack = findViewById(R.id.btnBack);

        // Set back button action
        btnBack.setOnClickListener(v -> finish());

        // Retrieve user ID and task title passed from the previous activity
        int userId = getIntent().getIntExtra("user_id", -1);
        String taskTitle = getIntent().getStringExtra("taskTitle");

        // Fetch question history from the database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Question> questions = dbHelper.getQuestionsForTask(userId, taskTitle);

        // Populate recycler view with historical question data
        adapter = new HistoryAdapter(questions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
