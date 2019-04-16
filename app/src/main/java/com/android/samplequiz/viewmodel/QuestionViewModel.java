package com.android.samplequiz.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.samplequiz.model.DataWrapper;
import com.android.samplequiz.model.Question;
import com.android.samplequiz.repository.QuestionRepository;

import javax.inject.Inject;

public class QuestionViewModel extends ViewModel {

    private int questionCorrect;
    private QuestionRepository repository;

    @Inject
    public QuestionViewModel(QuestionRepository repository) {
        this.repository = repository;
    }


    public MutableLiveData<DataWrapper<Question>> getQuestionLiveData(){
        return repository.getQuestionMutableLiveData();
    }

    public void loadQuestion(){
        repository.loadQuestion();
    }

    public MutableLiveData<Boolean> getAnswer(String answer, int questionId) {
        return repository.getAnswer(answer, questionId);
    }
}
