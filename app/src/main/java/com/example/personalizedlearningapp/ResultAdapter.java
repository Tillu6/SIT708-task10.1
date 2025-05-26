package com.example.personalizedlearningapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private final List<String[]> results;

    public ResultAdapter(List<String[]> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate each result card layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_card, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        // Bind question title and user's response to each item
        String[] pair = results.get(position);
        holder.tvTitle.setText(pair[0]);  // Set the question title
        holder.tvDesc.setText(pair[1]);   // Set the user's selected answer
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvResult_Title);  // Reference to the title TextView
            tvDesc = itemView.findViewById(R.id.tvResult_Desc);    // Reference to the answer TextView
        }
    }
}
