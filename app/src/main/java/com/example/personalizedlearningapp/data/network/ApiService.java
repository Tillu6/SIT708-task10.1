package com.example.personalizedlearningapp.data.network;

import com.example.personalizedlearningapp.data.model.Task;
import com.example.personalizedlearningapp.data.model.User;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    Call<User> login(@Body Map<String,String> creds);

    @POST("signup")
    Call<User> signup(@Body User user);

    @GET("task")
    Call<Task> getTask();

    @POST("submit")
    Call<Map<String,List<String>>> submitAnswers(@Body Map<String,String> answers);
}
