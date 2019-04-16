package com.android.samplequiz.di;

import com.android.samplequiz.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {ModelModule.class,
        ViewModelModule.class,
        RepositoryModule.class,
        NetworkModule.class})
@Singleton
public interface MainComponent {

    void inject(MainActivity mainActivity);
}
