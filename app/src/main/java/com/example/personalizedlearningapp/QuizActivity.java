package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import pl.droidsonroids.gif.GifImageView;        // now resolved!
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_QUIZ_ID             = "extra_quiz_id";
    public static final String EXTRA_QUIZ_LOAD_RESULTS   = "extra_quiz_load_results";

    private AuthManager authManager;
    private TextView    tvQuizTopic;
    private Button      btnSubmitQuiz;
    private GifImageView gifSpinner;
    private ImageButton btnGoBack;
    private Quiz        selectedQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_quiz);

        // apply system‐bar insets as padding on root
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
                    return insets;
                }
        );

        // getIntent checks
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_QUIZ_ID)) {
            finish(); return;
        }
        int quizID      = intent.getIntExtra(EXTRA_QUIZ_ID, -1);
        boolean loadResults = intent.getBooleanExtra(EXTRA_QUIZ_LOAD_RESULTS, false);
        if (quizID == -1) {
            finish(); return;
        }

        // bind views
        tvQuizTopic   = findViewById(R.id.tvQuizTopic);
        btnSubmitQuiz = findViewById(R.id.btnSubmitQuiz);
        gifSpinner    = findViewById(R.id.gifSpinner);
        btnGoBack     = findViewById(R.id.btnGoBack);

        // show spinner while loading
        gifSpinner.setVisibility(View.VISIBLE);

        // back arrow
        btnGoBack.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // submit / finish quiz
        btnSubmitQuiz.setOnClickListener(v -> {
            if (selectedQuiz == null) return;

            if (loadResults) {
                // just go home
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            }

            // ensure all answered
            boolean unanswered = false;
            for (QuizQuestion q : selectedQuiz.questions) {
                if (q.usersGuess.isEmpty()) {
                    unanswered = true;
                    break;
                }
            }
            if (unanswered) {
                Toast.makeText(this,
                        getString(R.string.err_answer_all), Toast.LENGTH_LONG).show();
                return;
            }

            // save every answer
            for (QuizQuestion q : selectedQuiz.questions) {
                authManager.saveUsersGuess(selectedQuiz, q);
            }

            // reload as results page
            Intent refresh = new Intent(this, QuizActivity.class);
            refresh.putExtra(EXTRA_QUIZ_ID, quizID);
            refresh.putExtra(EXTRA_QUIZ_LOAD_RESULTS, true);
            startActivity(refresh);
            finish();
        });

        authManager = new AuthManager(this);

        // placeholder list until network returns
        ArrayList<QuizQuestion> questions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ArrayList<String> opts = new ArrayList<>();
            opts.add(""); opts.add(""); opts.add(""); opts.add("");
            questions.add(new QuizQuestion("Loading…", opts, ""));
        }
        RecyclerView recycler = findViewById(R.id.recyclerView);
        recycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        QuizQuestionAdapter adapter =
                new QuizQuestionAdapter(this, questions);
        recycler.setAdapter(adapter);

        // fetch quizzes from server
        RetrofitClient.getInstance()
                .getAPI()
                .getUsersQuizzes(authManager.getToken())
                .enqueue(new Callback<ResponsePost>() {
                    @Override
                    public void onResponse(
                            Call<ResponsePost> call, Response<ResponsePost> response
                    ) {
                        if (!response.isSuccessful() || response.body()==null) {
                            // handle error…
                            return;
                        }
                        ArrayList<Quiz> all = new ArrayList<>(
                                QuizParser.parseQuizzes(QuizActivity.this, response.body().message)
                        );
                        // find our quiz by ID
                        for (Quiz q : all) {
                            if (q.id == quizID) {
                                selectedQuiz = q;
                                break;
                            }
                        }
                        if (selectedQuiz == null) {
                            Toast.makeText(QuizActivity.this,
                                    R.string.err_quiz_not_found, Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }

                        // hide spinner, show real data
                        gifSpinner.setVisibility(View.GONE);

                        // header text
                        if (loadResults) {
                            tvQuizTopic.setText(
                                    getString(R.string.results_header,
                                            selectedQuiz.getFormattedTopic())
                            );
                            btnSubmitQuiz.setText(R.string.go_back_home);
                        } else {
                            tvQuizTopic.setText(
                                    getString(R.string.quiz_header,
                                            selectedQuiz.getFormattedTopic())
                            );
                        }

                        // swap in real questions
                        questions.clear();
                        questions.addAll(selectedQuiz.questions);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ResponsePost> call, Throwable t) {
                        Toast.makeText(QuizActivity.this,
                                R.string.err_network, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
