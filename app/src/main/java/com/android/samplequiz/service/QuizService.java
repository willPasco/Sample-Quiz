package com.android.samplequiz.service;

import com.android.samplequiz.model.AnswerRequest;
import com.android.samplequiz.model.AnswerResponse;
import com.android.samplequiz.model.Question;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface QuizService {
    public static final int INTERNAL_ERROR = 500;
    public static final int REQUEST_OK = 200;

    @GET("/question")
    Call<Question> loadQuestion();

    @POST("/answer")
    Call<AnswerResponse> getAnswer(@Body AnswerRequest request, @Query("questionId") int questionId);
}
