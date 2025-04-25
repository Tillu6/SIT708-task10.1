package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import androidx.appcompat.app.AppCompatActivity;

public class InterestsActivity extends AppCompatActivity {
    private ChipGroup chipGroup;
    private Button btnNext;

    // The four topics to display, repeated in rows
    private final String[] TOPICS = {
            "Algorithms",
            "Data Structures",
            "Web Development",
            "Testing"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        chipGroup = findViewById(R.id.chipGroup);
        btnNext   = findViewById(R.id.btnNextInterests);

        // Dynamically add 5 rows × 4 chips = 20 chips total
        int repeats = 5;
        for (int i = 0; i < repeats; i++) {
            for (String topic : TOPICS) {
                Chip chip = new Chip(this, null, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Entry);
                chip.setText(topic);
                chip.setCheckable(true);
                chipGroup.addView(chip);
            }
        }

        btnNext.setOnClickListener(v -> {
            // TODO: collect selected topics if you need them:
            // List<String> selected = chipGroup.getCheckedChipIds().stream()
            //     .map(id -> ((Chip)findViewById(id)).getText().toString())
            //     .collect(Collectors.toList());
            // (then pass to your backend or next screen)

            // For now, just go on to HomeActivity:
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }
}
