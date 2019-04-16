package com.android.samplequiz.repository;

import android.util.Log;

import com.android.samplequiz.model.DataWrapper;
import com.android.samplequiz.model.Question;
import com.android.samplequiz.service.QuizService;

import javax.inject.Inject;

public class QuestionRepository {

    private QuizService service;
    private Question question;

    @Inject
    public QuestionRepository(QuizService service, Question question){
        this.service = service;
        this.question = question;
    }

    public DataWrapper loadQuestion(){
        Log.i("Arch test", "Work");
        return null;
    }
}
