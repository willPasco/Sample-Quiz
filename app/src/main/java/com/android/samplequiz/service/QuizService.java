package com.android.samplequiz.service;

import com.android.samplequiz.model.Question;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuizService {

    @GET("/question")
    Call<Question> loadQuestion();
}
