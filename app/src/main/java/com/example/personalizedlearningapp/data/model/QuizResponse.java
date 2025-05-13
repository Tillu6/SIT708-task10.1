package com.example.personalizedlearningapp.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QuizResponse {
    @SerializedName("quiz")
    private List<QuizItem> quiz;

    public List<QuizItem> getQuiz() {
        return quiz;
    }
}
