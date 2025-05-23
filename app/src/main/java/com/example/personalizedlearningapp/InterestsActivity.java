package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Collections;

public class InterestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_interests);

        // apply system-bar insets as padding
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
                    return insets;
                }
        );

        Button btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // build & shuffle interests
        ArrayList<String> interests = new ArrayList<>();
        Collections.addAll(interests,
                "Web Development","Javascript","PHP","Linear Algebra","Discrete Math",
                "Python","Data Science","AI","Machine Learning","Cyber Security",
                "Networks","C++","Cloud Computing","Database Management","DevOps",
                "Linux","Web Design","C#","Software Engineering","Game Dev",
                "UI/UX","Blockchain","AR","VR","Cryptography",
                "Data Mining","Robotics","Quantum Computing","Bioinformatics","Parallel Computing"
        );
        Collections.shuffle(interests);

        // hook up the RecyclerView with FlexboxLayoutManager
        RecyclerView recycler = findViewById(R.id.recyclerView);
        FlexboxLayoutManager flm = new FlexboxLayoutManager(this);
        flm.setJustifyContent(JustifyContent.CENTER);
        recycler.setLayoutManager(flm);

        InterestsAdapter adapter = new InterestsAdapter(this, interests);
        recycler.setAdapter(adapter);
    }
}
