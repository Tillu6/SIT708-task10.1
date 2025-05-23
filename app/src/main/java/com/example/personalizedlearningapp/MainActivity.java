package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningapp.API.AuthManager;
import com.example.personalizedlearningapp.API.RetrofitClient;
import com.example.personalizedlearningapp.API.models.ResponsePost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvText;
    private Button btnGenerateNewQuiz;
    private ImageButton btnProfile;
    private RecyclerView recycler;
    private TasksAdapter adapter;

    private AuthManager authManager;
    private ArrayList<Quiz> quizzes;
    private ArrayList<String> interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // enable edge-to-edge rendering
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_main);

        // apply system-bar insets as padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
            return insets;
        });

        // bind views
        tvText             = findViewById(R.id.textView);
        btnGenerateNewQuiz = findViewById(R.id.btnGenerateNewQuiz);
        btnProfile         = findViewById(R.id.btnProfile);
        recycler           = findViewById(R.id.tasksRecyclerView);

        // check login
        authManager = new AuthManager(this);
        if (authManager.getToken() == null || !authManager.isTokenValid()) {
            startActivity(new Intent(this, AccountLoginActivity.class));
            finish();
            return;
        }

        // check interests
        interests = authManager.getInterests();
        if (interests == null || interests.size() < 3) {
            startActivity(new Intent(this, InterestsActivity.class));
            finish();
            return;
        }

        // welcome text
        String userName = authManager.getJwtProperty("username");
        tvText.setText("Welcome back,\n" + userName + "!");

        // profile button
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });

        // setup RecyclerView
        quizzes = new ArrayList<>();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TasksAdapter(this, quizzes);
        recycler.setAdapter(adapter);

        // load existing quizzes
        RetrofitClient.getInstance()
                .getAPI()
                .getUsersQuizzes(authManager.getToken())
                .enqueue(new Callback<ResponsePost>() {
                    @Override
                    public void onResponse(Call<ResponsePost> call, Response<ResponsePost> resp) {
                        if (!resp.isSuccessful() || resp.body() == null) return;
                        quizzes.addAll(QuizParser.parseQuizzes(MainActivity.this, resp.body().message));
                        Collections.reverse(quizzes);
                        Collections.sort(quizzes, Comparator.comparing(Quiz::userHasAttempted));
                        if (quizzes.size() < 3) generateNewQuiz();
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure(Call<ResponsePost> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });

        // “Generate New Quiz” click
        btnGenerateNewQuiz.setOnClickListener(v -> generateNewQuiz());
    }

    private void generateNewQuiz() {
        // … your existing “pick random topic, placeholder, enqueue new quiz” logic …
    }
}
