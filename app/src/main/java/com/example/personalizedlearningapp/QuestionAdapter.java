package com.example.personalizedlearningapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
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

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private final List<Question> questionList;

    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_card, parent, false);
        return new ViewHolder(view);
    }

    // Bind data to the views inside the card layout
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question question = questionList.get(position);
        String qNum = (position + 1) + ". Question " + (position + 1);
        holder.number.setText(qNum);
        holder.title.setText(question.questionTitle);

        Log.d("DEBUG_QUESTION", "Setting title: " + question.questionTitle);

        // Automatically expand the first question for better UX
        if (position == 0) {
            question.isExpanded = true;
        }

        // Control visibility of title and options based on expansion state
        holder.title.setVisibility(question.isExpanded ? View.VISIBLE : View.GONE);
        holder.options.setVisibility(question.isExpanded ? View.VISIBLE : View.GONE);

        // Hide expand button for the first or already expanded item
        if (position == 0 || question.isExpanded) {
            holder.expandBtn.setVisibility(View.GONE);
        } else {
            holder.expandBtn.setVisibility(View.VISIBLE);
        }

        // Reset and populate radio buttons for options
        holder.options.removeAllViews();
        for (int i = 0; i < question.options.length; i++) {
            RadioButton rb = new RadioButton(holder.itemView.getContext());
            rb.setText(question.options[i]);
            rb.setTextColor(Color.WHITE);
            int index = i;

            // Restore previously selected answer if any
            if (question.selectedOption == i) {
                rb.setChecked(true);
            }

            rb.setOnClickListener(v -> question.selectedOption = index);
            holder.options.addView(rb);
        }

        // Toggle expand/collapse when the expand icon is clicked
        holder.expandBtn.setOnClickListener(v -> {
            question.isExpanded = !question.isExpanded;
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    // ViewHolder to hold references to UI elements
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, title;
        RadioGroup options;
        ImageView expandBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.tvQuestion_Number);
            title = itemView.findViewById(R.id.tvQuestion_Title);
            options = itemView.findViewById(R.id.radioGroupOptions);
            expandBtn = itemView.findViewById(R.id.btnExpand);
        }
    }
}
