package com.android.samplequiz.repository;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.samplequiz.model.AnswerResponse;
import com.android.samplequiz.model.AnswerRequest;
import com.android.samplequiz.model.DataWrapper;
import com.android.samplequiz.model.Question;
import com.android.samplequiz.service.QuizService;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.samplequiz.service.QuizService.INTERNAL_ERROR;

public class QuestionRepository {

    private static final String TAG = "QuestionRepo";
    private QuizService service;
    private MutableLiveData<DataWrapper<Question>> questionMutableLiveData;
    private MutableLiveData<Boolean> answerMutableLiveData;

    @Inject
    public QuestionRepository(QuizService service) {
        this.service = service;
        this.questionMutableLiveData = new MutableLiveData<>();
        this.answerMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapper<Question>> getQuestionMutableLiveData() {
        return questionMutableLiveData;
    }

    public void loadQuestion(){
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
    }

    public void loadAnswer(String answer, int questionId) {
        AnswerRequest request = new AnswerRequest(answer);

        Call<AnswerResponse> call = service.getAnswer(request, questionId);

        call.enqueue(new Callback<AnswerResponse>() {
            @Override
            public void onResponse(Call<AnswerResponse> call, Response<AnswerResponse> response) {
                if (response.body() != null) {
                    answerMutableLiveData.setValue(response.body().isResult());
                }
            }

            @Override
            public void onFailure(Call<AnswerResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<Boolean> getAnswerLiveData() {
        return answerMutableLiveData;
    }
}
