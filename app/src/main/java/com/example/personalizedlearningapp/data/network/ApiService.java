package com.example.personalizedlearningapp.data.network;

import com.example.personalizedlearningapp.data.model.QuizResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    /**
     * GET /getQuiz?topic=...
     * returns JSON: { "quiz": [ { "question": "...", "options": [...], "correct_answer": "A" }, … ] }
     */
    @GET("getQuiz")
    Call<QuizResponse> getQuiz(@Query("topic") String topic);

    /** Simple test endpoint to verify connectivity */
    @GET("test")
    Call<QuizResponse> runTest();
}
