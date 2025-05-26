package com.example.personalizedlearningapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningapp.model.Question;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<Question> questionList;

    public HistoryAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each history card
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question question = questionList.get(position);

        // Set the question number and title
        String qNum = (position + 1) + ". Question " + (position + 1);
        holder.tvNumber.setText(qNum);
        holder.tvTitle.setText(question.questionTitle);

        // Expand the first question by default
        if (position == 0) {
            question.isExpanded = true;
        }

        // Show/hide question content based on expansion state
        holder.tvTitle.setVisibility(question.isExpanded ? View.VISIBLE : View.GONE);
        holder.radioGroup.setVisibility(question.isExpanded ? View.VISIBLE : View.GONE);

        // Control expand button visibility
        if (position == 0 || question.isExpanded) {
            holder.expandBtn.setVisibility(View.GONE); // Hide button if already expanded or first item
        } else {
            holder.expandBtn.setVisibility(View.VISIBLE); // Show button for collapsed items
        }

        // Clear old radio buttons before re-rendering
        holder.radioGroup.removeAllViews();

        for (int i = 0; i < question.options.length; i++) {
            RadioButton rb = new RadioButton(holder.itemView.getContext());
            rb.setText(question.options[i]);
            rb.setEnabled(false); // Disable selection (read-only mode)

            // Color logic: red for wrong selection, green for correct answer, white otherwise
            if (question.selectedOption == i && !question.options[i].equals(question.correctAnswer)) {
                rb.setTextColor(Color.RED);
            } else if (question.options[i].equals(question.correctAnswer)) {
                rb.setTextColor(Color.GREEN);
            } else {
                rb.setTextColor(Color.WHITE);
            }

            // Mark selected option
            if (question.selectedOption == i) rb.setChecked(true);

            holder.radioGroup.addView(rb);
        }

        // Set timestamp and topic info
        holder.tvTimestamp.setText(question.timestamp != null ? question.timestamp : "Unknown");
        holder.tvTopic.setText(question.taskTitle != null ? question.taskTitle : "Unknown");

        // Expand/collapse button functionality
        holder.expandBtn.setOnClickListener(v -> {
            question.isExpanded = !question.isExpanded;
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return questionList == null ? 0 : questionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber, tvTitle, tvTimestamp, tvTopic;
        RadioGroup radioGroup;
        ImageView expandBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvHistory_Question_Number);
            tvTitle = itemView.findViewById(R.id.tvHistory_Question_Title);
            radioGroup = itemView.findViewById(R.id.History_radioGroupOptions);
            expandBtn = itemView.findViewById(R.id.btnExpand);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
            tvTopic = itemView.findViewById(R.id.tv_topic);
        }
    }
}
