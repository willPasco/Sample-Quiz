package com.android.samplequiz.di;

import com.android.samplequiz.model.DataWrapper;
import com.android.samplequiz.model.Question;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {

    @Provides
    public Question provideQuestion(){
        return new Question();
    }

    @Provides
    public DataWrapper provideDataWrapper(){
        return new DataWrapper();
    }
}
