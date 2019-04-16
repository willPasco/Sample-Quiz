package com.android.samplequiz.repository;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.samplequiz.model.DataWrapper;
import com.android.samplequiz.model.Question;
import com.android.samplequiz.service.QuizService;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionRepository {

    private static final String TAG = "QuestionRepo";
    public static final int INTERNAL_ERROR = 500;
    private QuizService service;
    private MutableLiveData<DataWrapper<Question>> questionMutableLiveData;

    @Inject
    public QuestionRepository(QuizService service) {
        this.service = service;
        this.questionMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapper<Question>> loadQuestion() {
        Call<Question> call = service.loadQuestion();

        call.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(@NonNull Call<Question> call, @NonNull Response<Question> response) {
                if (response.body() != null) {

                    DataWrapper<Question> dataWrapper = new DataWrapper<>();
                    dataWrapper.setData(response.body());
                    dataWrapper.setCode(response.code());
                    questionMutableLiveData.setValue(dataWrapper);

                } else {

                    DataWrapper<Question> dataWrapper = new DataWrapper<>();
                    dataWrapper.setCode(INTERNAL_ERROR);
                    questionMutableLiveData.setValue(dataWrapper);

                }
            }

            @Override
            public void onFailure(@NonNull Call<Question> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());

                DataWrapper<Question> dataWrapper = new DataWrapper<Question>();
                dataWrapper.setCode(INTERNAL_ERROR);
                questionMutableLiveData.setValue(dataWrapper);
            }
        });
        return questionMutableLiveData;
    }
}
