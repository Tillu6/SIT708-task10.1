package com.example.personalizedlearningapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.InterestViewHolder> {

    private final List<String> interests;           // All available interest topics
    private final List<String> selectedInterests;   // User-selected interests

    public InterestAdapter(List<String> interests, List<String> selectedInterests) {
        this.interests = interests;
        this.selectedInterests = selectedInterests;
    }

    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each interest item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interest, parent, false);
        return new InterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestViewHolder holder, int position) {
        String topic = interests.get(position);
        holder.btn.setText(topic);

        // Set visual state if this interest is selected
        holder.btn.setSelected(selectedInterests.contains(topic));

        // Handle click events for interest buttons
        holder.btn.setOnClickListener(v -> {
            if (selectedInterests.contains(topic)) {
                // Deselect topic
                selectedInterests.remove(topic);
                holder.btn.setAlpha(1f);  // Reset opacity
                holder.btn.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
            } else {
                // Allow max of 10 selected topics
                if (selectedInterests.size() < 10) {
                    selectedInterests.add(topic);
                    holder.btn.setAlpha(0.5f);  // Visually indicate it's selected
                    holder.btn.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                } else {
                    Toast.makeText(holder.itemView.getContext(),
                            "You can select up to 10 topics only.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    static class InterestViewHolder extends RecyclerView.ViewHolder {
        Button btn;

        public InterestViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btnInterest);
        }
    }
}
