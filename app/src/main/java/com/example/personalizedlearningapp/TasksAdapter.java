package com.example.personalizedlearningapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private final ArrayList<Quiz> tasks;
    private final Context context;

    public TasksAdapter(Context context, ArrayList<Quiz> tasks) {
        this.tasks = tasks;
        this.context = context;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_task_fragment, parent, false);
        return new TasksViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TasksViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout rlTaskView;
        private final TextView tvQuizTitle;
        private final TextView tvQuizDescription;
        private final TextView tvCompletedStatus;
        private final Button btnAttemptQuiz;
        private final View layoutSpinner;
        private final TextView tvSpinnerText;
        private final ImageView gifSpinner;
        private final ShimmerFrameLayout shimmer;
        private final Context context;

        public TasksViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context            = ctx;
            rlTaskView         = itemView.findViewById(R.id.rlTaskView);
            tvQuizTitle        = itemView.findViewById(R.id.tvTitle);
            tvQuizDescription  = itemView.findViewById(R.id.tvDescription);
            tvCompletedStatus  = itemView.findViewById(R.id.tvCompletedStatus);
            btnAttemptQuiz     = itemView.findViewById(R.id.btnStartQuiz);
            layoutSpinner      = itemView.findViewById(R.id.layoutSpinner);
            tvSpinnerText      = itemView.findViewById(R.id.tvSpinnerText);
            gifSpinner         = itemView.findViewById(R.id.gifSpinner);
            shimmer            = itemView.findViewById(R.id.shimmer_view_container);
        }

        public void bind(Quiz quiz) {
            // hide everything by default
            shimmer.stopShimmer();
            layoutSpinner.setVisibility(View.GONE);
            gifSpinner.setVisibility(View.GONE);
            tvSpinnerText.setVisibility(View.GONE);
            btnAttemptQuiz.setVisibility(View.VISIBLE);
            tvCompletedStatus.setVisibility(View.GONE);

            if ("GENERATING QUIZ...".equals(quiz.topic)) {
                // show shimmer + spinner
                tvQuizTitle.setText("");
                tvQuizDescription.setText("");
                btnAttemptQuiz.setVisibility(View.GONE);

                shimmer.startShimmer();
                layoutSpinner.setVisibility(View.VISIBLE);
                gifSpinner.setVisibility(View.VISIBLE);
                tvSpinnerText.setVisibility(View.VISIBLE);

            } else {
                // normal loaded state
                shimmer.stopShimmer();
                String topic = quiz.getFormattedTopic();
                tvQuizTitle.setText(topic);
                tvQuizDescription.setText(
                        context.getString(R.string.ai_quiz_about_x, topic)
                );

                if (quiz.userHasAttempted()) {
                    btnAttemptQuiz.setText(R.string.view_results);
                    btnAttemptQuiz.setBackgroundColor(
                            Color.parseColor("#FFFF02C8")
                    );
                    btnAttemptQuiz.setTextColor(Color.WHITE);
                    tvCompletedStatus.setVisibility(View.VISIBLE);
                }
            }

            btnAttemptQuiz.setOnClickListener(v -> {
                Intent i = new Intent(context, QuizActivity.class);
                i.putExtra(QuizActivity.EXTRA_QUIZ_ID, quiz.id);
                if (quiz.userHasAttempted()) {
                    i.putExtra(QuizActivity.EXTRA_QUIZ_LOAD_RESULTS, true);
                }
                context.startActivity(i);
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            });
        }
    }
}
