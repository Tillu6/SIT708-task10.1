package com.example.personalizedlearningapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningapp.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    // Interface for handling task click events
    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    private final List<Task> taskList;
    private final OnTaskClickListener listener;
    private final int userId;

    // Constructor with task data, listener, and user ID
    public TaskAdapter(List<Task> taskList, OnTaskClickListener listener, int userId) {
        this.taskList = taskList;
        this.listener = listener;
        this.userId = userId;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.title.setText(task.title);
        holder.desc.setText(task.description);

        // Show task status and history button only if status is not empty
        if (!task.status.isEmpty()) {
            holder.status.setVisibility(View.VISIBLE);
            holder.btnHistory.setVisibility(View.VISIBLE);
            holder.status.setText(task.status);
        } else {
            holder.status.setVisibility(View.GONE);
            holder.btnHistory.setVisibility(View.GONE);
        }

        // Handle task card click events
        holder.itemView.setOnClickListener(v -> listener.onTaskClick(task));
        holder.arrow.setOnClickListener(v -> listener.onTaskClick(task));

        // Navigate to HistoryActivity when 'History' is clicked
        holder.btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), HistoryActivity.class);
            intent.putExtra("user_id", userId);      // Pass user ID for fetching history
            intent.putExtra("taskTitle", task.title); // Pass task title
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // ViewHolder class for task card layout
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, status, btnHistory;
        ImageView arrow;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTask_Title);
            desc = itemView.findViewById(R.id.tvTask_Desc);
            arrow = itemView.findViewById(R.id.ivArrow);
            status = itemView.findViewById(R.id.tvTask_Status);
            btnHistory = itemView.findViewById(R.id.btn_History);
        }
    }
}
