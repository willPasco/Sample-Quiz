package com.android.samplequiz.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.samplequiz.model.DataWrapper;
import com.android.samplequiz.model.Question;
import com.android.samplequiz.repository.QuestionRepository;

import javax.inject.Inject;

public class QuestionViewModel extends ViewModel {

    private int correctPoints;
    private int questionCount;
    private QuestionRepository repository;

    @Inject
    public QuestionViewModel(QuestionRepository repository) {
        this.repository = repository;
    }


    public MutableLiveData<DataWrapper<Question>> getQuestionLiveData(){
        return repository.getQuestionMutableLiveData();
    }

    public MutableLiveData<Boolean> getAnswerLivedata(){
        return repository.getAnswerLiveData();
    }

    public void loadQuestion(){
        questionCount++;
        repository.loadQuestion();
    }

    public void loadAnswer(String answer, int questionId) {
        repository.loadAnswer(answer, questionId);
    }

    public void increaseCorrectPoint(){
        correctPoints ++;
    }

    public int getCorrectPoints(){
        return correctPoints;
    }

    public int getQuestionCount() {
        return questionCount;
    }
}
