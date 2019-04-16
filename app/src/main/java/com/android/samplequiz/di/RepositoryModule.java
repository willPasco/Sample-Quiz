package com.android.samplequiz.di;

import com.android.samplequiz.model.Question;
import com.android.samplequiz.repository.QuestionRepository;
import com.android.samplequiz.service.QuizService;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    public QuestionRepository provideQuestionRepository(QuizService service){
        return new QuestionRepository(service);
    }
}
