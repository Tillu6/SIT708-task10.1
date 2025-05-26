package com.example.personalizedlearningapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InterestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> interests;
    private List<String> selectedInterests = new ArrayList<>();
    private Button btnNext;
    private DatabaseHelper dbHelper;
    private InterestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        recyclerView = findViewById(R.id.recyclerViewInterests);
        btnNext = findViewById(R.id.btnNext);
        dbHelper = new DatabaseHelper(this);

        interests = Arrays.asList(
                "Algorithms", "Data Structures", "AI", "Android", "Security",
                "Web Dev", "Testing", "Cloud", "UI/UX", "Databases");

        // Setup Flexbox layout for dynamic width buttons
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexWrap(FlexWrap.WRAP); // Allow line wrapping
        layoutManager.setJustifyContent(JustifyContent.FLEX_START); // Align to start
        layoutManager.setAlignItems(AlignItems.FLEX_START); // Align items at top

        recyclerView.setLayoutManager(layoutManager);

        adapter = new InterestAdapter(interests, selectedInterests);
        recyclerView.setAdapter(adapter);

        // Handle "Next" button click
        btnNext.setOnClickListener(v -> {
            // Ensure at least one interest is selected
            if (selectedInterests.isEmpty()) {
                Toast.makeText(this, "Select at least 1 interest", Toast.LENGTH_SHORT).show();
                return;
            }

            int userId = getIntent().getIntExtra("user_id", -1);
            if (userId == -1) {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                return;
            }

            // Retrieve additional user details from database
            String username = getUserFieldById(userId, "username");
            String email = getUserFieldById(userId, "email");
            String password = getUserFieldById(userId, "password");
            String phone = getUserFieldById(userId, "phone");
            String avatarUri = getUserFieldById(userId, "avatar_uri");

            // Save selected interests to the database
            for (String interest : selectedInterests) {
                dbHelper.getWritableDatabase().execSQL(
                        "INSERT INTO interests (user_id, interest) VALUES (?, ?)",
                        new Object[]{userId, interest});
            }

            // Navigate to DashboardActivity with user details and interests
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("username", username);
            intent.putExtra("email", email);
            intent.putExtra("avatar_uri", avatarUri);
            intent.putStringArrayListExtra("interests", new ArrayList<>(selectedInterests));

            startActivity(intent);
            finish();
        });
    }

    /**
     * Estimate the optimal number of columns based on item length and screen width.
     */
    private int getSuggestedColumnCount(List<String> items) {
        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int paddingPerItem = 80;

        int longest = 0;
        for (String s : items) {
            if (s.length() > longest) longest = s.length();
        }

        int estimatedItemWidth = (int) (longest * 20) + paddingPerItem;
        return Math.max(2, Math.min(3, totalWidth / estimatedItemWidth));
    }

    /**
     * Get the most recently registered user ID.
     */
    private int getLastUserId() {
        int userId = -1;
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT MAX(id) as id FROM users", null);
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
        }
        return userId;
    }

    /**
     * Get a specific user field (e.g., username, email) by user ID.
     */
    private String getUserFieldById(int userId, String fieldName) {
        Cursor c = dbHelper.getReadableDatabase().rawQuery(
                "SELECT " + fieldName + " FROM users WHERE id = ?", new String[]{String.valueOf(userId)});
        if (c.moveToFirst()) {
            String value = c.getString(0);
            c.close();
            return value;
        }
        return "";
    }
}
