package com.example.personalizedlearningapp.data.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // must match your Flask host/port
    private static final String BASE_URL = "http://10.0.2.2:5000/";

    // 1. Build a custom OkHttpClient with extended timeouts
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // time to establish TCP
            .writeTimeout(60, TimeUnit.SECONDS)    // time to send request body
            .readTimeout(60, TimeUnit.SECONDS)     // time to wait for server response
            .build();

    // 2. Use that client in your Retrofit instance
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ApiService getApi() {
        return retrofit.create(ApiService.class);
    }
}
